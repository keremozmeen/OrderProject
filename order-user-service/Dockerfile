FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app


COPY . .

RUN mvn clean install -DskipTests


FROM amazoncorretto:17-al2-jdk
WORKDIR /app

COPY --from=build /app/order-user-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]