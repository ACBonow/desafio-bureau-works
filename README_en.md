
# Technical Challenge - FullStack
## Objective
The main objective of the system is to provide a comprehensive platform for managing translator and document information, facilitating efficient interaction and control of these data in a scalable manner.

## Considerations
A system was developed to manage translators and documents, with the following functionalities:
- Frontend internationalization - option for Portuguese or English
- CRUD operations for translators
- CRUD operations for documents
- Uploading documents via CSV file
- Uploading translators via CSV file

The system was developed using Java 21 with Spring Boot on the backend and Vue.js on the frontend.

The system was designed to be simple and user-friendly, with an intuitive interface.

Although not ideal, I chose not to include authentication and authorization, as the focus of the challenge was to develop a functional, not complex system.

- It is allowed to register translators with the same email, due to an example in the CSV file, although it is recommended that the email be unique.
- It is allowed to register documents, via CSV or individually, without a location. When this occurs, the system will generate a location using OpenAI's AI.
- Since there was no rule to set the responsible translator for a document, a random methodology was used, where the system will randomly select a translator for each document, if the translator has the source language equal to the document's language.
- In the CSV files, there was no standardization for the language abbreviation. Example: "pt-br," "pt_br," and "pt_BR" are considered the same language. The system will convert to the ISO standard "pt-BR."
- If there is no translator available for the document's language, the system will generate an exception and inform the user.
- A translator can only be deleted if no documents are associated with this translator.

## How to Run the Project
Clone the repository: https://github.com/ACBonow/desafio-bureau-works.git
Configure the Database
- Create a database in PostgreSQL named "translator_manager."
- Create a user with the name "postgres" and password "postgres."
- If necessary, update the credentials in the backend's application.properties file.

application.properties

    spring.datasource.url=jdbc:postgresql://localhost:5432/translator_manager  
    spring.datasource.username=postgres  
    spring.datasource.password=postgres  


Running the Backend

    cd desafio-bureau-works/translator-manager-backend  
    mvn clean install  
    mvn spring-boot:run  


- Backend available at: http://localhost:8080
- Access the API documentation at: http://localhost:8080/swagger-ui/index.html

Running the Frontend

    cd desafio-bureau-works/translator-manager-frontend  
    npm install  
    npm run serve  


- Frontend available at: http://localhost:8081
- If port 8081 is already in use, Vue CLI will suggest an alternative port. You can access the application at the new suggested port, but you will need to update the CORS allowed URL on the backend.

    netstat -ano | findstr :8081  
    taskkill /PID <PID> /F  

Where <PID> must be replaced by the process ID.


Paths for Test Files
- desafio-bureau-works/translator-manager-backend/src/main/resources/documents.csv
- desafio-bureau-works/translator-manager-backend/src/main/resources/translators.csv

Technologies Used
- Java 21 - https://www.oracle.com/java/technologies/downloads/#java21
- Spring Boot 3.4.4
- Maven 3.9.9 - https://maven.apache.org/download.cgi
- PostgreSQL 17 - https://www.postgresql.org/download/
- Node.js 22.14.0 - https://nodejs.org/en/download/
- Vue.js 3

Backend Dependencies
- spring-boot-starter: Basic configuration for Spring Boot, provides support for quickly bootstrapping Spring applications.
- spring-boot-starter-web: Includes dependencies for creating REST APIs and web applications.
- spring-boot-starter-data-jpa: Support for data persistence using JPA (Java Persistence API) and database integration.
- spring-boot-starter-validation: Provides support for data validation using annotations like @Valid and @NotNull.
- spring-boot-starter-test: Dependency for testing, including JUnit, Mockito, and Spring testing tools.
- postgresql: JDBC driver for connecting the application to the PostgreSQL database.
- lombok: Library that reduces boilerplate code in Java classes by automatically generating getters, setters, constructors, etc.
- springdoc-openapi-starter-webmvc-ui: Automatically generates API documentation in OpenAPI/Swagger format, enabling interactive visualization and testing.

Frontend Dependencies
- @vueuse/core: A collection of utilities based on Vue 3 that make development easier, such as reactive hooks and helpful functions for state management and interactions.
- axios: A library for making HTTP requests. Widely used for communication with APIs, such as sending and receiving data from the server.
- bootstrap: A popular CSS framework for creating responsive and styled interfaces. It provides ready-to-use components like buttons, forms, and layouts.
- bootstrap-icons: A set of icons compatible with Bootstrap that can be used to add visual elements to the project.
- core-js: A library that provides polyfills for compatibility with older browsers, ensuring modern JavaScript features work in legacy environments.
- vue: The main framework used in the project. It is a JavaScript library for building reactive user interfaces and reusable components.
- vue-i18n: A library for internationalization (i18n) in Vue projects. It allows translating text and adapting content for different languages.
- vue-router: Vue's official router, used for managing navigation between pages or components in a Vue application.

Database
- PostgreSQL: A relational and object-relational database management system.



