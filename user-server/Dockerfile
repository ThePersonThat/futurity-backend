FROM maven:3.8.5-jdk-11-slim

WORKDIR app
COPY . .
RUN mvn dependency:go-offline

ENTRYPOINT ["mvn", "spring-boot:run"]