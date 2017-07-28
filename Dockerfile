FROM frolvlad/alpine-oraclejdk8:slim
EXPOSE 8080
CMD java -jar /maven/aw-acc-status-20170724-205418.7383dd9.jar
