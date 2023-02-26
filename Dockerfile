FROM openjdk:17.0.1

ARG JAR_FILE

RUN echo $JAR_FILE

COPY $JAR_FILE app.jar

ENTRYPOINT ["java","-jar","/app.jar"]