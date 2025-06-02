FROM maven:3.9.9 AS build
WORKDIR /app

COPY  pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src
RUN mvn clean package -DskipTests



FROM openjdk:22
WORKDIR /app

COPY --from=build /app/target/garden-shop-1.0-SNAPSHOT.jar /app/garden-shop-1.0-SNAPSHOT.jar

CMD ["java", "-jar","garden-shop-1.0-SNAPSHOT.jar"]

