FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD target/admin-web-0.0.1-SNAPSHOT.jar fundrequest-admin.jar
RUN sh -c 'touch /fundrequest-admin.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fundrequest-admin.jar" ]
