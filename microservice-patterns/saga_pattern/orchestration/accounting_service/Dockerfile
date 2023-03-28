FROM gradle:7-jdk17 AS build
RUN gradle buildFatJar --no-daemon
WORKDIR /build/libs

FROM openjdk:17-oracle
COPY *fat.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
