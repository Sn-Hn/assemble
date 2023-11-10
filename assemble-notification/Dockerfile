FROM adoptopenjdk/openjdk11:latest

ARG JAR_FILE=build/libs/assemble-notification-0.0.1.jar
COPY ${JAR_FILE} assemble-notification.jar


ENTRYPOINT ["java", "-jar", "/assemble-notification.jar"]