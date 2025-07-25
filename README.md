
```markdown
# Employee Management System

A full-stack role-based employee management system for Admins and Employees, built with **Spring Boot**, **MySQL**, and **React TypeScript**. It supports onboarding, profile setup, leave requests, work logs, and performance reports.

---

## 🗂️ Project Structure

```

Employee-Management--System/
│
├── backend/              # Spring Boot + MySQL backend
│   └── src/
│
├── frontend/             # React + TypeScript frontend
│   └── src/
│
├── README.md
└── .gitignore

````

---

## ✅ Features

### Authentication
- Login using `username` and `password`
- JWT-based token auth
- Forgot password with email reset
- Role-based access control (ADMIN / EMPLOYEE)

### Admin Functionality
- Register new employees
- View all employees
- View and export work logs
- Approve or reject leave requests
- Send leave notification emails
- Get report for specific employee or full team

### Employee Functionality
- First login requires profile setup
- Submit daily work logs (task, hours, comments)
- Apply for leave
- View personal leave status and work history

---

## ⚙️ Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security + JWT
- JPA + Hibernate
- MariaDB / MySQL
- JavaMailSender
- Maven

### Frontend
- React + TypeScript
- React Query
- Axios
- Tailwind CSS
- React Router DOM

---

## 🔌 Database Setup

Create the DB in MySQL or MariaDB:

```sql
CREATE DATABASE EmployeeManagementSystem;
````

### `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/EmployeeManagementSystem?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔐 Email Configuration

For password resets and leave notifications:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 🔧 How to Run the Project

### 1. Clone the Repo

```bash
git clone https://github.com/austinendlovu/Employee-Management--System.git
cd Employee-Management--System
```

---

## 🖥️ Running Backend

```bash
cd backend
./mvnw spring-boot:run
```

---

## 🧑‍💻 Running Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🌐 API Endpoints

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`
* `POST /api/auth/forgot-password`
* `POST /api/auth/reset-password`

### Admin

* `POST /api/admin/employees/register`
* `GET /api/admin/employees`
* `GET /api/admin/worklogs`
* `GET /api/admin/worklogs/employee/{id}`
* `GET /api/admin/worklogs/report/{id}?from=YYYY-MM-DD&to=YYYY-MM-DD`
* `GET /api/admin/worklogs/report/all`
* `GET /api/admin/leaves`
* `PUT /api/admin/leaves/{id}/approve`
* `PUT /api/admin/leaves/{id}/reject`

### Employee

* `POST /api/employee/profile/setup`
* `GET /api/employee/profile`
* `POST /api/employee/worklogs/submit`
* `GET /api/employee/worklogs`
* `POST /api/employee/leaves/apply`
* `GET /api/employee/leaves`

---

## 📸 Frontend Pages

### Admin Views

* Login Page
* Employee Registration
* Work Log Viewer
* Leave Management
* Performance Reports

### Employee Views

* Profile Setup
* Dashboard
* Work Log Submission
* Leave Application
* Leave Status Viewer

---

## 🧪 Development Tips

* Use `Postman` to test backend routes
* Use `.env` in frontend to set API base URL
* Always start the backend before the frontend

---

## 🧑 Author

**Austine Ndlovu**
GitHub: [austinendlovu](https://github.com/austinendlovu)

---

## 📄 License

MIT License

