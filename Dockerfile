FROM openjdk:17.0.1
COPY build/libs/pastestorage-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]