# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/zenith-expense-tracker-0.0.1-SNAPSHOT.jar .

# Expose the port on which the application runs
EXPOSE 8080

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "zenith-expense-tracker-0.0.1-SNAPSHOT.jar"]
