FROM fundrequestio/alpine-jdk8-slim

VOLUME /tmp
ADD target/tweb-0.0.1-SNAPSHOT.jar fundrequest.jar
ADD libs/dd-java-agent.jar dd-java-agent.jar
RUN sh -c 'touch /fundrequest.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -javaagent:/dd-java-agent.jar -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC -jar /fundrequest.jar" ]
