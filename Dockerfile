FROM amazoncorretto:17
COPY target/*.jar danuma-lk-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","danuma-lk-0.0.1-SNAPSHOT.jar"]