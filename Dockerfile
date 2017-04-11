FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD core/target/core-0.0.1-SNAPSHOT.jar mvp-backend-core.jar
RUN sh -c 'touch /mvp-backend-core.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /mvp-backend-core.jar" ]
