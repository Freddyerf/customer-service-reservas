# Customer Service Application

This project is a Quarkus-based application that manages customer data, utilizing a PostgreSQL database for storage. It's containerized using Docker and Docker Compose for easy setup and deployment.


## Prerequisites

Before you begin, ensure you have the following installed on your machine:
- Docker
- Docker Compose

## Getting Started

Follow these steps to get the application running on your machine:

### 1. Clone the Repository

First, clone this repository to your local machine:

You can run your application in dev mode that enables live coding using:
```bash
git clone https://github.com/Freddyerf/customer-service-reservas.git
cd customer-service-reservas
```
### 2. Build and Run with Docker Compose

From the root directory of the project, run the following command to build and start the application along with the PostgreSQL database:
```bash
docker-compose up --build
```
This command builds the application and database images (if not already built) and starts the containers.

### 3. Accessing the Application

Once the containers are up and running, you can access the application at `http://localhost:8080`.


## Application Configuration

The application's database connection is configured through environment variables in `docker-compose.yml`. The default configuration is as follows:

- **Database URL**: jdbc:postgresql://db:5432/customerdb
- **Username**: myuser
- **Password**: mypass

You can modify these values directly in `docker-compose.yml` to match your desired configuration.

## Stopping the Application

To stop the application and remove the containers, use the following command: 
```bash
docker-compose down
```

Optionally, if you want to remove the volume containing the database data for a fresh start next time, you can add the `-v` flag:
```bash 
docker-compose down -v
```

## API Documentation

Explore the API documentation and try out the endpoints using Swagger UI: 

1. Start the application with `docker-compose up`.
2. Open a web browser and navigate to [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui).

This page provides interactive documentation, allowing you to try out the API endpoints directly.

**Note:** Adjust the URL based on your application's host and port.
