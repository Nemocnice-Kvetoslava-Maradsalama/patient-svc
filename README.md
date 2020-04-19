# Patient service
Part of NKM project

## Status
![Maven build](https://github.com/Nemocnice-Kvetoslava-Maradsalama/patient-svc/workflows/Maven%20build/badge.svg)

## Build
Prerequisites: java 1.8, maven

to build this service, simply execute `mvn package` and grab the resulting jar from the target folder. You can also run `docker-compose build` afterwards.

## API
After starting this service, swagger is available (by default) at `http://localhost:8080/swagger-ui.html`