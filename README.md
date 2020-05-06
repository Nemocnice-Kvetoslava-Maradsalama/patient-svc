# Patient service
Part of NKM project

## Status
![Maven build](https://github.com/Nemocnice-Kvetoslava-Maradsalama/patient-svc/workflows/Maven%20build/badge.svg)

## Building
Prerequisites: jdk (java 8), apache maven

To build this service, simply execute `mvn package -DskipTests` and grab the resulting jar from the target folder. You can also run `docker-compose build` afterwards.

## Running
You can start this service in docker by executing `docker-compose up`. This requires that a docker network named `nemocnice-network` is created beforehand. 

## API
After starting this service, swagger UI is available (by default) at `http://localhost:8080/swagger-ui.html`