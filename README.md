# SCORING

An HTTP-based mini game back-end in Java which registers score points for different users, with the capability to return the current user position and high score list.

## Technologies

- Java 8
- SpringBoot
- Swagger
- H2 Database (embedded)
- JUnit/Mockito
- Lombok (for boilerplate)

## Usage
To start the application, run the following command

```shell
> mvn spring-boot:run
```

## Swagger docs

After starting the application, the swagger access to the exposed endpoints could be accessed via:

http://localhost:8080/swagger-ui.html#

## H2

The embedded in-memory database could be accessed via

http://localhost:8080/h2

```
username: sa
password: password
```

## Unit Testing
To test the application run the following command

```shell
> mvn test
```

