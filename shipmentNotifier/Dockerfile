FROM openjdk:17
LABEL maintainer="test@billie.io"
VOLUME /tmp

EXPOSE 8080
ARG JAR_FILE=target/shipmentNotifier-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]