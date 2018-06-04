#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
cd "$BASEDIR"
mvn clean install -DskipTests=true
cd tweb
mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=local"