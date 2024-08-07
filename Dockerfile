FROM openjdk:17-jdk-slim
MAINTAINER salma
COPY target/order-management-api-0.0.1-SNAPSHOT.jar order-management-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "order-management-api-0.0.1-SNAPSHOT.jar"]
