FROM openjdk:10-jre-slim
ARG JAR_FILE=target/event-app-0.0.1-SNAPSHOT.jar
ARG DIR_APP=/usr/src/eventapp/
COPY ./${JAR_FILE} ${DIR_APP}
COPY eventbrite.yml ${DIR_APP}
COPY telegram.yml ${DIR_APP}
COPY jwt.yml ${DIR_APP}
WORKDIR ${DIR_APP}
EXPOSE 8080
CMD ["java", "-Dspring.data.mongodb.uri=mongodb://mongodb.eventapp.com/eventDB", "-jar", "event-app-0.0.1-SNAPSHOT.jar"]