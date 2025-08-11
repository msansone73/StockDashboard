#Dockerfile to java spring boot application
FROM openjdk:21-jdk-slim
# Set the working directory
WORKDIR /app
# Copy the jar file into the container
COPY target/*.jar app.jar
# Expose the port the app runs on
EXPOSE 8080

# Set environment variables
ENV STOCK_DATASOURCE_URL=""
ENV STOCK_DATASOURCE_PASSWORD=""


# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=${STOCK_DATASOURCE_URL}", "--spring.datasource.username=msansone", "--spring.datasource.password=${STOCK_DATASOURCE_PASSWORD}"]