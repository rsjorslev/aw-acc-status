FROM frolvlad/alpine-oraclejdk8:slim
# FROM openjdk:latest
VOLUME /tmp
ADD aw-acc-status-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
