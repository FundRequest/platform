FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD target/rest-api-0.0.1-SNAPSHOT.jar mvp-backend-api.jar
RUN sh -c 'touch /mvp-backend-api.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /mvp-backend-api.jar" ]
