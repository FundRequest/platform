#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    docker build -t fundrequestio/platform:"$TRAVIS_BRANCH" tweb;
    docker build -t fundrequestio/adminweb:"$TRAVIS_BRANCH" admin-web;
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin;
    docker push fundrequestio/platform:"$TRAVIS_BRANCH";
    docker push fundrequestio/adminweb:"$TRAVIS_BRANCH";
fi