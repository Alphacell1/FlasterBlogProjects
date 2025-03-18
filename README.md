# Flaster Blog Project

A simple blog application using Spring Boot (Java 17+), Angular (v15+), JWT-based security, role-based access (Author/Reader), and a PostgreSQL database.

Architecture overview diagram

[ Angular App ] --(HTTP/JSON + JWT)--> [ Spring Boot / REST ] --(JPA)--> [ PostgreSQL DB ]


## Key Features
- Secure signup & login with JWT
- Role-based access (Author, Reader)
- Blog management (create, edit, delete) for Authors
- Comments for both Readers & Authors
- Like/Dislike functionality
- User profiles

## 1) Prerequisites
- Java 17 (or higher)
- Node.js + npm (for Angular)
- PostgreSQL (or any database, but we used PostgreSQL)

## 2) Environment Variables
The following environment variables are required:

- `DB_URL` (e.g. `jdbc:postgresql://localhost:5432/flaster_blog_db`)
- `DB_USERNAME` (e.g. `postgres`)
- `DB_PASSWORD` (your DB password)
- `JWT_SECRET` (32+ character secret for JWT)

For example, on Windows Command Prompt:
setx DB_URL "jdbc:postgresql://localhost:5432/flaster_blog_db"
setx DB_USERNAME "postgres"
setx DB_PASSWORD "mysecret"
setx JWT_SECRET "Px9z4kG7F1c8fC6g9s2hW8s3bL7d5fT!"


## 3) Backend Setup
1. Open project in IntelliJ
2. Make sure environment variables are set
3. Run `mvn clean install`
4. Start Spring Boot app: `mvn spring-boot:run`
   - The app listens on port 5000 by default

## 4) Frontend Setup
1. `cd frontend` (the Angular project folder)
2. `npm install`
3. `ng serve --proxy-config proxy.conf.json`
4. Open `http://localhost:4200`

## 5) Testing
- Create an account, log in, see Blog list (if role is Author, can create, edit, delete)
- Comments, likes/dislikes available if user is authenticated
- Profile page at `http://localhost:4200/profile`

## API Documentation
Swagger is enabled at `[GET] /swagger-ui/index.html`

## 6) Technology Stack why

- **Spring Boot 3.4.2**: Simplifies REST API with embedded Tomcat
- **Spring Data JPA**: For database persistence with PostgreSQL
- **PostgreSQL**: Relational database
- **Angular 15+**: Frontend SPA
- **JWT (Json Web Tokens)**: Secures endpoints with role-based access
- **Swagger / OpenAPI**: Automatic API documentation
- **Maven**: Build and dependency management
- **IntelliJ IDEA**: Primary IDE
- **sweetalert2** : Easier and more professional Dialogs in Angular






