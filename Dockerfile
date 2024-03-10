FROM amazoncorretto:17

WORKDIR /source

COPY target/*.jar app.jar
COPY entrypoint.sh entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
