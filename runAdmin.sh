#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
cd "$BASEDIR"
mvn clean install -DskipTests=true
java -Dspring.profiles.active="local" -jar ./admin-web/target/admin-web-0.0.1-SNAPSHOT.jar