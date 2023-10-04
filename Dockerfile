FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY /target/smartsplit-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "smartsplit-1.0.0.jar"]