FROM maven:3.9.6-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/screensage-web-server-0.0.1-SNAPSHOT.jar screensage-web-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "screensage-web-server.jar"]
