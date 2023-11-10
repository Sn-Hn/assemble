FROM adoptopenjdk/openjdk11:latest

ARG JAR_FILE=build/libs/assemble-api-0.0.1.jar
COPY ${JAR_FILE} assemble-api.jar


ENTRYPOINT ["java", "-jar", "/assemble-api.jar"]