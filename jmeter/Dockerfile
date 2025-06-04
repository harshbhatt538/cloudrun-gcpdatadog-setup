FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y wget
RUN wget https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.6.3.tgz && \
    tar -xvzf apache-jmeter-5.6.3.tgz && \
    rm apache-jmeter-5.6.3.tgz
WORKDIR /apache-jmeter-5.6.3
ENTRYPOINT ["./bin/jmeter.sh"]
