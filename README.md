# Package Optimizer

A REST API that helps optimize value per package of items in a shipping system.

## Build Info

Built with Spring Boot (Spring version 4), JDK version 17. 

The project makes use of [Lombok](https://projectlombok.org/) and [Swagger](https://swagger.io/) with [springdoc-openapi](https://springdoc.org/).

The API documentation can be found at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## How to run

### Run with [Docker](https://www.docker.com/):

Open a terminal, position yourself in the root of the project and run the following commands (port 8080 must be free). 

It makes use of the provided jar. If you want to reflect changes on future image builds, you have to change the path in the Dockerfile, or otherwise copy the jar into the root.

`docker build --tag=dev-test:latest .`

`docker run -p8080:8080 dev-test:latest`

### Run with locally installed JDK:

Install the correct Java SDK following these [instructions](https://docs.oracle.com/en/java/javase/17/install/installation-guide.pdf), run `.\mvnw clean install` inside the root of the project and then run the compiled jar, or alternatively run `DevTestApplication` inside an IDE. 

### Run tests:
`.\mvnw clean test`
