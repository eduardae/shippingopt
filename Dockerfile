FROM openjdk:17-jdk-alpine
MAINTAINER edd
COPY dev-test-0.0.1-SNAPSHOT.jar dev-test-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/dev-test-0.0.1-SNAPSHOT.jar"]