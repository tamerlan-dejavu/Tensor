# Stage 1: build with Maven
FROM eclipse-temurin:17-jdk-alpine AS builder
RUN apk add --no-cache maven
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: minimal runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/cipher-api/target/cipher-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
