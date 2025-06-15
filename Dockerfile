# Use a Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set workdir and copy the project files
WORKDIR /app
COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]