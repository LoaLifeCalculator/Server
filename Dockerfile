# 1. Base image with JDK 21
FROM eclipse-temurin:21-jdk

# 2. Set working directory inside container
WORKDIR /app

# 3. Copy the built JAR into the container
COPY build/libs/*.jar app.jar

# 4. Expose the port your app runs on (Spring default is 8080)
EXPOSE 8080

# 5. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
