#!/bin/bash

java -Dspring.data.mongodb.uri=$MONGODB_URI -jar target/event-app-0.0.1-SNAPSHOT.jar
