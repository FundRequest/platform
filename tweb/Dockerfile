FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD target/tweb-0.0.1-SNAPSHOT.jar fundrequest.jar
RUN sh -c 'touch /fundrequest.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fundrequest.jar" ]
