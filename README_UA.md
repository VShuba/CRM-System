# CRM System for Gyms

🔗 **Жива Swagger-документація**:  
[http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html)

Це CRM-система, розроблена командою для керування мережею спортивних клубів і філій. Вона дозволяє централізовано контролювати організації, філії та користувачів з розмежуванням доступу за ролями, повною підтримкою CRUD-операцій, моніторингом і безпечним розгортанням на AWS.

---

## ⚙️ Технологічний стек

- **Мова**: Java 21  
- **Фреймворк**: Spring Boot (Security, Data JPA, Validation, Actuator)  
- **База даних**: PostgreSQL (продакшн), H2 (розробка)  
- **Аутентифікація**: Basic Auth, JWT, OAuth2 (Google)  
- **Документація**: Swagger / OpenAPI  
- **Моніторинг**: Prometheus, Grafana  
- **CI/CD**: GitLab CI, Docker  
- **Деплоймент**: AWS EC2  

---

## 🗂️ Структура проєкту

```text
src
├── main
│   ├── java/ua.shpp
│   │   ├── configuration          # Security, CORS, Swagger configs
│   │   ├── constraints            # Custom validations
│   │   ├── controller             # REST endpoints: Branch, Organization, Auth
│   │   ├── dto                    # Clean DTO layer
│   │   ├── entity                 # JPA entities (Organization, Branch, User)
│   │   ├── exception              # Global exception handling
│   │   ├── mapper                 # DTO <=> Entity mappers (MapStruct)
│   │   ├── model                  # Request filters and wrappers
│   │   ├── repository             # Spring Data repositories
│   │   ├── security               # Auth filters, token logic, roles
│   │   ├── service                # Business logic
│   │   └── util                   # Utility classes
│   ├── resources
│   │   ├── application.properties
│   │   ├── data.sql
│   │   ├── logback-spring.xml
│   │   └── import-sheet-template.tsv
├── test                          # Unit and integration tests
├── .gitlab-ci.yml                # CI/CD pipeline config
```

## 🔐 Безпека

- Basic Auth (для Swagger UI)  
- JWT для API-доступу  
- OAuth2 (Google) для зовнішньої авторизації  
- Авторизація з `@PreAuthorize` через SpEL  
- Розмежування доступу за ролями: `ADMIN`, `MANAGER`  

---

## 📊 Моніторинг

- Spring Boot Actuator + Prometheus для збору метрик  
- Grafana для дашбордів у реальному часі  
- Метрики: кількість запитів, час відповіді, активність користувачів, стан системи  

---

## 🔄 CI/CD

- Налаштовано через `.gitlab-ci.yml`  
- Збірка через Docker  
- Автоматичне розгортання на AWS EC2 при мержі в `main`  
- Підтримка Helm-чартів (опційно)  
- Розгортання по SSH через GitLab Runner  

---

## 🧪 Тестування

- Юніт-тести для сервісів, контролерів і маперів  
- Spring Boot Test, Mockito  
- Покриття: валідація, пагінація, безпека  

---

## 🏗️ Функціонал

- CRUD для всіх сутностей  
- Кастомні валідаційні анотації та глобальний обробник помилок  
- Пагінація та фільтрація в ендпойнтах  
- Чітке розділення Entity/DTO  
- Управління ролями та доступами  
- Swagger-документація  
- OAuth2 логін і безпечна робота з JWT  

---

## 📦 Деплоймент

- Запуск на AWS EC2 (Ubuntu)  
- PostgreSQL локально або через RDS  
- Docker для пакування та деплойменту  
- Інтеграція з GitLab CI  

---

## 👥 Команда

6 бекенд-розробників  
Робочий процес включав: code review, GitLab issues, парне програмування, налаштування інфраструктури.  
Архітектура побудована на принципах **Clean Architecture**, **SOLID**, **DRY**, чітке розділення відповідальностей.

---

## 📌 Приклад API

Swagger UI доступний за адресою:  
[http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-56-228-80-43.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html)
