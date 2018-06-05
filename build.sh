#!/usr/bin/env bash
BASEDIR=$(dirname "$0")
cd "$BASEDIR"
mvnw clean install -DskipTests=true
