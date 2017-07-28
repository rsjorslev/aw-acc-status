FROM frolvlad/alpine-oraclejdk8:slim
EXPOSE 8080
COPY target/*.jar /maven/app.jar
CMD java -jar /maven/app.jar
