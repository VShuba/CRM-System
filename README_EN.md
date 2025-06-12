# CRM System for Gyms

🔗 **Live Swagger Documentation**:  
[http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html)

This is a team-developed CRM system designed for managing gym networks and branches. 
It enables centralized control of organizations, branches, and users with role-based access, full CRUD support, monitoring, and secure deployment on AWS.

---

## ⚙️ Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot (Security, Data JPA, Validation, Actuator)
- **Database**: PostgreSQL (prod), H2 (dev)
- **Authentication**: Basic Auth, JWT, OAuth2 (Google)
- **Docs**: Swagger/OpenAPI
- **Monitoring**: Prometheus, Grafana
- **CI/CD**: GitLab CI, Docker
- **Deployment**: AWS EC2

---

## 🗂️ Project Structure

src
├── main
│ ├── java/ua.shpp
│ │ ├── configuration # Security, CORS, Swagger configs
│ │ ├── constraints # Custom validations
│ │ ├── controller # REST endpoints: Branch, Organization, Auth
│ │ ├── dto # Clean DTO layer
│ │ ├── entity # JPA entities (Organization, Branch, User)
│ │ ├── exception # Global exception handling
│ │ ├── mapper # DTO <=> Entity mappers (MapStruct)
│ │ ├── model # Request filters and wrappers
│ │ ├── repository # Spring Data repositories
│ │ ├── security # Auth filters, token logic, roles
│ │ ├── service # Business logic
│ │ └── util # Utility classes
│ ├── resources
│ │ ├── application.properties
│ │ ├── data.sql
│ │ ├── logback-spring.xml
│ │ └── import-sheet-template.tsv
├── test # Unit and integration tests
├── .gitlab-ci.yml # CI/CD pipeline config

---

## 🔐 Security

- Basic Auth (for Swagger UI)
- JWT for API access
- OAuth2 (Google) integration for external authentication
- Authorization with `@PreAuthorize` using SpEL
- Role-based access control: `ADMIN`, `MANAGER`

---

## 📊 Monitoring

- Spring Boot Actuator + Prometheus for custom/system metrics
- Grafana dashboards for real-time monitoring
- Tracked metrics: request count, response time, user activity, system health

---

## 🔄 CI/CD Pipeline

- Configured via `.gitlab-ci.yml`
- Dockerized builds
- Automatic deployment to AWS EC2 on merge to `main`
- Optional Helm chart support
- SSH-based deploy using GitLab Runner

---

## 🧪 Testing

- Unit tests for services, controllers, and mappers
- Spring Boot Test, Mockito
- Covered: validation, pagination, security filters

---

## 🏗️ Features

- CRUD for all entities
- Custom validation annotations and global error handler
- Paginated and filterable endpoints
- Clean separation between Entities and DTOs
- Role management and permissions
- OpenAPI (Swagger) documentation
- OAuth2 login and secure JWT token management

---

## 📦 Deployment

- Running on AWS EC2 (Ubuntu)
- PostgreSQL hosted on local or RDS
- Docker used for packaging and deploying
- CI/CD integrated with GitLab

---

## 👥 Team

6 backend developers  
Workflow included: code review, GitLab issues, pair programming, infrastructure setup.  
Architecture was based on best practices: **Clean Architecture**, **SOLID**, **DRY**, clear separation of concerns.

---

## 📌 API Example

Swagger UI available here:  
[http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html)

