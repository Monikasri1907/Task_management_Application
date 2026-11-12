# Task Management Application

A beginner-friendly full-stack task manager using Java 17, Spring Boot, Spring Security, MySQL, HTML, CSS and JavaScript.

## Features
- User registration and login (passwords are BCrypt hashed)
- Each user can see and manage only their own tasks
- Create, view, update and delete tasks
- Task status: Pending, In Progress, Completed
- Optional due date
- Responsive web page

## Requirements
- JDK 17 or later
- Maven 3.6+
- MySQL Server 8+

## 1. Create the database
Open MySQL Workbench or the MySQL command line and run:

```sql
CREATE DATABASE task_manager;
```

## 2. Set your MySQL password
Open `src/main/resources/application.properties` and replace:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

with your actual MySQL root password. Do not share this password.

## 3. Run the application
Open a terminal in the project folder and run:

```bash
mvn spring-boot:run
```

Then open http://localhost:8080

## 4. Use the application
1. Select Register and create a username/password.
2. Return to Login and sign in.
3. Add a task, choose its status and optional due date.
4. Use Edit or Delete to manage tasks.
5. Log out when finished.

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create an account |
| POST | `/api/auth/login` | Sign in (HTTP Basic authentication) |
| GET | `/api/auth/me` | Current signed-in user |
| POST | `/api/auth/logout` | Sign out |
| GET | `/api/tasks` | List your tasks |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update your task |
| DELETE | `/api/tasks/{id}` | Delete your task |

## Important learning note
This is a local learning/demo project. It disables CSRF for simplicity. Before deploying publicly, enable CSRF protection, use HTTPS, move database credentials to environment variables, configure secure cookie settings, and add stronger validation/error handling.
