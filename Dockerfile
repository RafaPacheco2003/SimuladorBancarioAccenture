FROM maven:3.9-eclipse-temurin-17-alpine
WORKDIR /app

COPY pom.xml .
COPY . .

EXPOSE 8080
CMD ["mvn", "spring-boot:run"]