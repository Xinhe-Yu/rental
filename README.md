# Chatop Rental Back-end app

The **Chatop Rental Backend Application** is a RESTful API developed using the **Spring Framework**. It serves as the backend for the Chatop Rental platform, supporting core functionalities such as managing rental listings, user authentication, and data storage. This backend application is tightly integrated with a **MySQL** database, ensuring efficient data persistence and retrieval.

## 1. Installation Guide

Ensure that you have :
- Java 17 (check it with `java -version`)
- Apache Maven 3.x.x (check it with `mvn -v`)
- MySQL (version 8.0.39 or higher is recommended, please also check if it is running and accessible, `sudo systemctl status mysql` for Linux, `brew services list | grep mysql` for macOS brew and directly open `services.msc` on Windows.)

After clone the rpository, you can follow the steps to install and run the application:
1. Install Dependencies with Maven
```bash
mvn clean install
```

2. Configure `application.properties`

Locate the `env_template` file. Make a copy in the same folder and rename it `.env`. Add your MySQL root's username and password.

3. Initialize the Database Schema
With MySQL installed and running, load the initial database schema:
```bash
mysql -u root -p < ./src/chatop_rental_schema.sql
```
- You will be prompted for the MySQL root password.

4. Run the Application
Once the configuration and database setup are complete, start the application using Maven:
```bash
mvn spring-boot:run
```
The application will start on the port 3001 (localhost). should see output in the terminal indicating the application has started successfully.
After starting the application, you can view the API documentation at:

API Documentation: http://localhost:3001/api/swagger-ui/index.html

**Test endpoints with JWT Token :**

1. Get a JWT Token by endpoint POST /auth/register or POST /auth/login

2. Paste the JWT Token in the input box after clicking "Authorize" at the top right corner of the page

## 2. Libraries and Packages Overview
This section provides an overview of the main libraries and packages used in the Chatop Rental backend application. Each package contributes essential functionality to ensure security, data management, API documentation, and secure handling of sensitive information.

### Spring Security with OAuth2 and JWT
Provides robust authentication and authorization mechanisms for the entire application.

### Spring Data JPA and MySQL Connector
Simplifies database interaction by managing Object-Relational Mapping (ORM) between Java objects and database tables.

### Springdoc OpenAPI
Generates interactive API documentation.

### Java-Dotenv
Protects sensitive information by enabling secure configuration with environment variables.
