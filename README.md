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

In the project’s `src/main/resources directory`, locate the `application.properties` file. Configure the following properties to match your MySQL setup:
```properties
properties# Database connection details
spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=root
spring.datasource.password=<YOUR_PASSWORD>
```

3. Initialize the Database Schema
With MySQL installed and running, load the initial database schema:
```bash
mysql -u root -p chatop < ./src/chatop_rental_schema.sql
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
