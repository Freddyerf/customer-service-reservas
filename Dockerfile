# Stage 1: Build the application with Eclipse Temurin JDK
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy the Maven Wrapper script and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn/
COPY src src/

# Ensure mvnw is executable
RUN chmod +x ./mvnw

# Build the project without running tests
RUN ./mvnw clean package -DskipTests -Dquarkus.package.type=uber-jar

# Stage 2: Create the runtime image with Eclipse Temurin JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/customer-service-reservas-1.0.0-SNAPSHOT-runner.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
