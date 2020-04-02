FROM openjdk:8-jdk-alpine

ENV APP_PORT=8080

ENV PG_HOST=localhost
ENV PG_PORT=5432
ENV PG_DB=patient
ENV PG_USER=postgres
ENV PG_PASS=

ENV EUREKA_HOST=localhost
ENV EUREKA_PORT=8761

WORKDIR /usr/app

COPY ./target/patient*.jar ./patient-svc.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./patient-svc.jar"]