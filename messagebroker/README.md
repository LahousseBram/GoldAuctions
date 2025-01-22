# MSS Broker Environment

## Overview

This repository sets up a WebSocket server integrated with a RabbitMQ message broker. It listens to a specified RabbitMQ queue and broadcasts messages to connected WebSocket clients. It also provides an HTTP endpoint to verify the server status.

## Prerequisites

## Key Files
index.js: The main application file that sets up the WebSocket server and RabbitMQ integration.

## Troubleshooting
1. RabbitMQ Connection Issues:

- Check if the URL environment variable is correctly configured.
- Ensure RabbitMQ is running and accessible.
2. WebSocket Clients Not Receiving Messages:

- Verify that the queue specified in QUEUE is active and receiving messages.
- Confirm clients are connected and ready to receive data.


### Environment Variables
Ensure the following environment variables are set before starting the server:

- **`URL`**: RabbitMQ connection URL  
  Example:  
  `amqps://nrchltxo:RzG_S_WoSb_NGhtOI5YciIuVofbHyOBe@dog.lmq.cloudamqp.com/nrchltxo`

- **`QUEUE`**: The name of the RabbitMQ queue to listen to  
  Example:  
  `GoldAuctions`

If these variables are not set, the application defaults to:
- `URL`: `amqp://localhost`
- `QUEUE`: `default_queue`

## How It Works

1. **WebSocket Server**:  
   - Starts a WebSocket server to handle real-time communication with clients.
   - Accepts WebSocket connections and manages a pool of clients.

2. **RabbitMQ Integration**:  
   - Connects to a RabbitMQ broker.
   - Consumes messages from the specified queue.
   - Broadcasts each message to all connected WebSocket clients.

3. **Idle Timeout**:  
   - If no messages are received for 1 hour, the RabbitMQ connection is closed to save resources.

4. **Echo Functionality**:  
   - Any message sent by a WebSocket client is echoed back as a confirmation.


## Installation

1. **Clone the repository**:
 ```bash
 git clone [https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/messagebroker](https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/messagebroker)


## Install Dependencies:
```bash
npm install