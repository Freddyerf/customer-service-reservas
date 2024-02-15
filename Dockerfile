# Stage 1: Build the application with Eclipse Temurin JDK
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy your application's source
COPY pom.xml .
COPY src src/


# Build the project without running tests
RUN mvn clean package -DskipTests -Dquarkus.package.type=uber-jar

# Stage 2: Create the runtime image with Eclipse Temurin JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/customer-service-reservas-1.0.0-runner.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
