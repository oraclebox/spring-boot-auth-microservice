FROM anapsix/alpine-java:8
RUN mkdir /application
RUN mkdir -p /application/logs
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar /application/api-gateway.jar
COPY application.yml /application/application.yml
WORKDIR /application
ENTRYPOINT ["java","-jar"]
CMD ["api-gateway.jar"]
EXPOSE 9000
