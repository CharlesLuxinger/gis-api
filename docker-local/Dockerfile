FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package --batch-mode

FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine AS release

COPY --from=build /build/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
