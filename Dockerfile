FROM amazoncorretto:17

WORKDIR /app

COPY target/danuma-lk-0.0.1-SNAPSHOT.jar /app/danuma-lk.jar

EXPOSE 8080

CMD ["java", "-jar", "danuma-lk.jar"]
