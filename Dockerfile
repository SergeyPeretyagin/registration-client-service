FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /usr/src/amusic-new-backend

COPY . .

RUN mvn clean install -Dmaven.test.skip
#RUN mvn package

#app
FROM openjdk:17-alpine3.14

#WORKDIR /app

COPY --from=builder /usr/src/amusic-new-backend/target/user-service*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

