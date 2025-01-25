# ForoHub

ForoHub is a backend application built using Spring Boot to manage a forum platform. This project provides a REST API for handling CRUD operations with authentication and authorization features to ensure data security.

## Features

- **CRUD Operations:** Manage forum topics, responses, and users.
- **Authentication and Authorization:** Secure access with Spring Security.
- **Database Integration:** Uses MySQL as the database.
- **Flyway Migrations:** Ensures database schema consistency.
- **Validation:** Ensures data integrity and business rule compliance.

## Technologies Used

- **Programming Language:** Java 23
- **Framework:** Spring Boot 3.4.2
- **Dependencies:**
  - Lombok
  - Spring Web
  - Spring Boot DevTools
  - Spring Data JPA
  - Flyway Migration
  - MySQL Driver
  - Validation
  - Spring Security
- **Database:** MySQL
- **Build Tool:** Maven

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 23
- MySQL
- Maven

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/JairChavez21/forohub.git
   cd forohub
   ```

2. Configure the database:
   - Create a MySQL database named `foro_hub`.
   - Update the `application.properties` file with your database username and password:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/foro_hub
     spring.datasource.username=YOUR_USERNAME
     spring.datasource.password=YOUR_PASSWORD
     ```

3. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application:
   - The API will be available at `http://localhost:8080`.

## API Endpoints

### Topics
- **GET /topics**: Retrieve all topics.
- **POST /topics**: Create a new topic.
- **PUT /topics/{id}**: Update an existing topic.
- **DELETE /topics/{id}**: Delete a topic.

### Users
- **GET /users**: Retrieve all users.
- **POST /users**: Register a new user.

### Responses
- **GET /responses**: Retrieve all responses.
- **POST /responses**: Create a response to a topic.

## Security

The application uses Spring Security for authentication and authorization. Ensure you configure the `api.security.secret` property in the `application.properties` file to secure your API.

## Database Schema

For a detailed view of the database schema, refer to the `schema.sql` file or the Flyway migration scripts.

## Contributions

Contributions are welcome! Feel free to fork this repository and submit a pull request with your improvements.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Author

Developed by [Jair Cedillo Chávez](https://github.com/JairChavez21).
