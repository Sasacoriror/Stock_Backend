FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/Stocks-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT["Java","-jar","/app.jar"]