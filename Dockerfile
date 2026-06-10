# syntax=docker/dockerfile:1

# 1. Build the jar inside the image (so `docker compose up` works with no local Maven build)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml checkstyle.xml checkstyle-suppressions.xml ./
# Pre-fetch dependencies first for better layer caching
RUN mvn -B dependency:go-offline
COPY src/ src/
RUN mvn -B clean package -DskipTests

# 2. Split the fat jar into cacheable layers
FROM eclipse-temurin:17-jdk AS layers
WORKDIR /application
COPY --from=build /build/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# 3. Minimal runtime image
FROM eclipse-temurin:17-jdk
WORKDIR /application
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
