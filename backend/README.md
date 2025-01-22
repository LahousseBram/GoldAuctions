# Spring Boot Backend Project

## Overview
This is a Spring Boot-based backend application designed to handle business logic and data persistence. It connects to a MySQL database and uses JWT for authentication. The project is configured to automatically create database tables based on the application's entity definitions.

## Prerequisites
- Java 17 or higher
- Maven 3.8+ or Gradle
- MySQL Server 8.x

## Setup Instructions

### Clone the Repositories
1. Clone the backend repository:
   ```bash
   git clone https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/backend
   ```

2. Clone the frontend repository:
   ```bash
   git clone https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/frontend
   ```

3. Refer to the documentation for additional details:
   [Project Documentation](https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/documentation#)

### Set Up the Frontend
2. Configure the environment variables:
   - Set up the following environment variables in your development environment:
     ```bash
     DB_NAME=<your_database_name>
     DB_USERNAME=<your_database_username>
     DB_PASSWORD=<your_database_password>
     JWT_SECRET_KEY=<your_secret_key>
     ```

3. Create a MySQL database:
   - Create a database with the name specified in the `DB_NAME` environment variable.
   - **Note:** Do not create any tables; Spring Boot will handle the table creation automatically based on entity definitions.

4. Build the application:
   ```bash
   ./mvnw clean package
   ```
   or
   ```bash
   ./gradlew build
   ```

5. Run the application:
   ```bash
   java -jar target/<application_name>.jar
   ```

Alternatively, you can run the project directly from your IDE.

6. Access the application:
   - The application will be available at `http://localhost:8080` by default.

## Dependencies
The project uses the following dependencies:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- MySQL Connector Java
- JSON Web Token (JWT) library

## Key Features
- Automatic table creation based on entity classes.
- Secure authentication using JWT.
- RESTful API endpoints for data operations.
