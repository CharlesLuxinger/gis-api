FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package --batch-mode -DskipTests

FROM adoptopenjdk/openjdk11:jdk-11.0.9_11-alpine-slim AS release

COPY --from=build /build/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
