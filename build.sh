#!/usr/bin/env bash
BASEDIR=$(dirname "$0")
cd "$BASEDIR"
mvn clean install -DskipTests=true
