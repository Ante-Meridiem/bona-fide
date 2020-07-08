FROM alpine
ADD target/bona-fide.jar bona-fide.jar
EXPOSE 9002
ENTRYPOINT ["java", "-Dserver.port=9002", "-jar", "bona-fide.jar"]
