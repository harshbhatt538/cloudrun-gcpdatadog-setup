FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the Datadog serverless-init binary
COPY --from=datadog/serverless-init:1 /datadog-init /app/datadog-init

# Add the Datadog Java tracer
ADD 'https://dtdg.co/latest-java-tracer' /app/dd-java-agent.jar

# Copy the application JAR
COPY target/*.jar app.jar

# Set the entrypoint to use datadog-init with verbose output
ENTRYPOINT ["/app/datadog-init"]

# Run the application with the Datadog Java agent, explicitly setting the trace agent URL
CMD ["java", "-javaagent:/app/dd-java-agent.jar", "-Ddd.logs.injection=true", "-Ddd.trace.sample.rate=1.0", "-Ddd.trace.agent.url=http://localhost:8126", "-jar", "app.jar"]
