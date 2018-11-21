FROM maven:3-jdk-8-slim
ARG DIR_APP=/usr/src/eventapp/
WORKDIR ${DIR_APP}
COPY . .
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8080