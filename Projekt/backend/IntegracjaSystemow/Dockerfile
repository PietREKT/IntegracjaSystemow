#Buduj aplikację
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#Buduj obraz
FROM openjdk:23-jdk

#Dodatkowy użytkownik dla bezpieczeństwa
RUN groupadd spring && useradd -g spring spring

RUN mkdir -p /data && chown -R spring:spring /data

USER spring:spring

WORKDIR /../spring

COPY --from=builder /../app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]