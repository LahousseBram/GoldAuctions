const express = require('express');
const amqp = require('amqplib');
const WebSocket = require('ws');
const http = require('http');

class MessageListener {
    constructor() {
        this.app = express();

        this.app.get('/', (req, res) => {
            res.send('WebSocket Server is Running');
        });

        this.server = http.createServer(this.app);
        this.wss = new WebSocket.Server({
            server: this.server,
            verifyClient: (info, done) => {
                console.log('Verify Client:', info.req.headers);
                done(true);
            }
        });

        this.clients = new Set();
        this.rabbitMQConnection = null;

        this.wss.on('connection', (ws, req) => {
            console.log('New WebSocket client connected');
            console.log('Connection headers:', req.headers);

            this.clients.add(ws);

            ws.on('message', (message) => {
                console.log('Received message from client:', message.toString());
                ws.send(JSON.stringify({
                    type: 'echo',
                    data: message.toString()
                }));
            });

            ws.on('close', () => {
                console.log('WebSocket client disconnected');
                this.clients.delete(ws);
            });
        });

        this.wss.on('error', (error) => {
            console.error('WebSocket Server Error:', error);
        });
    }

    async connectRabbitMQ() {
        try {
            if (this.rabbitMQConnection) {
                console.log('RabbitMQ connection already established');
                return;
            }

            this.rabbitMQConnection = await amqp.connect(process.env.URL || 'amqp://localhost');
            const channel = await this.rabbitMQConnection.createChannel();
            const queueName = process.env.QUEUE || 'default_queue';

            await channel.assertQueue(queueName, { durable: true });

            console.log(`Waiting for messages in queue: ${queueName}`);

            let inactivityTimeout = null;

            const resetInactivityTimeout = () => {
                if (inactivityTimeout) {
                    clearTimeout(inactivityTimeout);
                }
                inactivityTimeout = setTimeout(() => {
                    console.log('No messages for 1 hour. Closing RabbitMQ connection.');
                    this.rabbitMQConnection.close();
                    this.rabbitMQConnection = null;
                }, 60 * 60 * 1000);
            };

            resetInactivityTimeout();

            channel.consume(queueName, (msg) => {
                if (msg !== null) {
                    const content = msg.content.toString();
                    console.log('Received message:', content);

                    this.clients.forEach((client) => {
                        if (client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'message',
                                data: content
                            }));
                        }
                    });

                    channel.ack(msg);
                    resetInactivityTimeout();
                }
            });

            this.rabbitMQConnection.on('close', () => {
                console.log('RabbitMQ connection closed');
                if (inactivityTimeout) clearTimeout(inactivityTimeout);
                this.rabbitMQConnection = null;
            });

        } catch (error) {
            console.error('RabbitMQ Connection Error:', error);
            this.rabbitMQConnection = null;
            setTimeout(() => this.connectRabbitMQ(), 5000);
        }
    }

    start(port = 3001) {
        this.server.listen(port, '0.0.0.0', () => {
            console.log(`Server running on port ${port}`);
        });

        this.connectRabbitMQ();
    }
}

const messageListener = new MessageListener();
messageListener.start();
