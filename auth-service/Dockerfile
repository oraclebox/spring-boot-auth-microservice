FROM anapsix/alpine-java:8
RUN mkdir /application
RUN mkdir -p /application/logs
COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /application/auth-service.jar
COPY application.yml /application/application.yml
WORKDIR /application
ENTRYPOINT ["java","-jar"]
CMD ["auth-service.jar"]
EXPOSE 9000
