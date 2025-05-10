FROM amazoncorretto:11-alpine-jdk
RUN mkdir -p /app/db && chmod a+rwx /app/db  # Добавьте права на запись
COPY target/restarate.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]