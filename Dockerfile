#Dockerfile to java spring boot application
FROM openjdk:21-jdk-slim
# Set the working directory
WORKDIR /app
# Copy the jar file into the container
COPY target/*.jar app.jar
# Expose the port the app runs on
EXPOSE 8080

# Set environment variables
ENV STOCK_DASHBOARD_DATASOURCE=""
ENV STOCK_DASHBOARD_DATASOURCE_PASS=""


# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=${STOCK_DASHBOARD_DATASOURCE}", "--spring.datasource.username=msansone", "--spring.datasource.password=${STOCK_DASHBOARD_DATASOURCE_PASS}"]