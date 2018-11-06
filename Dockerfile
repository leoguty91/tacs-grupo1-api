FROM openjdk:10-jre-slim
ARG DIR_APP=/usr/src/eventapp/
COPY eventbrite.yml ${DIR_APP}
COPY telegram.yml ${DIR_APP}
COPY jwt.yml ${DIR_APP}
WORKDIR ${DIR_APP}
EXPOSE 8080
