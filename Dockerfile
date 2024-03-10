FROM amazoncorretto:17

WORKDIR /source

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/source/app.jar"]
