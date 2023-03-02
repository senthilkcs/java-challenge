### Summary
This is an API for CRUD operations on employee data stored in an H2 database. The API provides endpoints for GET, POST, DELETE, and PUT operations. Access to the API is controlled by role-based authorization and requires an API Key (JWT Token) for access.

### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html

> The REST API endpoints are protected and require an `API Key (JWT token)`.
> You can generate a JWT Token in Swagger UI via `JWT Token Generator` API post end point.
> To generate a token, provide any one of below username and password in a JSON POST request and retrieve the generated JWT token in the response body.
> Copy the value from `Bearer.......` and use it as the API Key value to access the Employee API via "Authorize" in Swagger UI.

> The H2 database automatically create default users with respective roles, and passwords are stored as Bcrypt encrypted hash values.

> Username/Password details as below and refer the role details in h2 database or data.sql file in resources directory.
>  - `admin`/`admin` 
>  - `user1`/`user1`
>  - `user2`/`user2`


- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.

#### Restrictions
- use java 8


### What I did:

- Updated Swagger document details using Swagger Config.
- Added unit test cases.
- Implemented caching logic for database calls using in-memory Caffeine cache.
- Protected controller endpoints based on user role using Spring Security JWT tokens.
  - JWT token generation API endpoint was created for testing purposes.
- Created custom exception handler and API responses.
- Logback was included for logging.
- Data validation was added for the employee POST/PUT API endpoints.

### What I Would Have Done If I Had More Time:

- Include a Spring profile-based configuration file.
- Include more logging in all classes.
- Write integration tests and additional unit test cases.
- Containerize using Docker/Podman.
- Include a CI/CD pipeline to test, build, and deploy the application.
- Include Spring Boot Actuator Monitoring.

### My Experience in Java
- I have 16 years of experience in Java and have been using Spring Boot for the past 4 years.