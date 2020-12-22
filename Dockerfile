FROM openjdk:8-jre-alpine

MAINTAINER oviok

ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /opt/my-event/

COPY ./target/java-${APP_VERSION}.jar /opt/my-event/my-event.jar

# use the app default configured port  by ${server.port}, running with -p ${server.port}:${server.port}
# EXPOSE 8086

ENTRYPOINT ["sh", "-c", "java -jar /opt/my-event/my-event.jar"]