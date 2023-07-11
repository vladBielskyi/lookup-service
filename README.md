# Project Readme

This project is a web service that retrieves information about the issuing bank based on a card number.

## Requirements

To run this project, make sure you have the following installed:

- Java 17
- Spring Boot 3.1.1
- Maven build tool
- PostgreSQL RDBMS

## Installation

1. Clone the repository to your local machine.
2. Navigate to the project directory.

## Setting up the Database

1. Make sure you have PostgreSQL installed and running.
2. Create a new PostgreSQL database for the project, or start docker-compose file from Docker directory
3. Configure the database connection details in the `application.properties` file located in the `src/main/resources`
   directory.
4. Uncomment property spring.jpa.hibernate.ddl-auto=create to create tables

## REST API Documentation

available on path /swagger-ui/index.html

## Web UI

The project includes a simple HTML page with an input field for entering a card number and a "Search" button.

To access the UI, navigate to the following address: `/infos`
