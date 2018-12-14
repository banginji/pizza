# pizza application

### How to run the application
- Navigate to root of the project
- Run the command `./gradlew clean build bootRun`

### How to invoke the APIs
- When the application is running navigate to `http://localhost:8080/swagger-ui.html` in a browser to use swagger for invoking the apis
- For endpoints starting with `/v1/admin/` basic authentication is required which is `ADMIN:ADMIN`

### Data
Data is set up in the different repositories in `PizzaApplication`

### Difference between Java and Groovy
The application is written in Groovy. One major difference between Java and Groovy for Object wrappers is Groovy automatically gives setters and getters and we do not have to explicitly define them.
