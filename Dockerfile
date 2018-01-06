FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/support-1.0-SNAPSHOT.jar /app

EXPOSE 8088

CMD ["java", "-jar", "support-1.0-SNAPSHOT.jar"]
