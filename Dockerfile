FROM maven:3-jdk-8-slim
ARG DIR_APP=/usr/src/eventapp/
COPY . ${DIR_APP}
WORKDIR ${DIR_APP}
RUN mvn clean package -Dmaven.test.skip=true
EXPOSE 8080