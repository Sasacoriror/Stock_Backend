FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress
COPY src ./src
RUN mvn clean package --no-transfer-progress

FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

