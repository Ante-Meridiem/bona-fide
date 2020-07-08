#Using Linux 
FROM alpine
ADD target/bona-fide.jar bona-fide.jar

#Install JDK
RUN apk add openjdk8

EXPOSE 9002
ENTRYPOINT ["java", "-Dserver.port=9002", "-jar", "bona-fide.jar"]
