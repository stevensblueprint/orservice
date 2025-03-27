FROM maven:3.8.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/orservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
