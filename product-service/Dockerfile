FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
COPY dd-java-agent.jar dd-java-agent.jar
ENTRYPOINT ["java", "-javaagent:/app/dd-java-agent.jar", "-Ddd.service=product-service", "-Ddd.env=prod", "-Ddd.trace.agent.url=unix:///var/run/datadog/apm.socket", "-Ddd.logs.injection=true", "-Ddd.trace.sample.rate=1.0", "-jar", "app.jar"]
