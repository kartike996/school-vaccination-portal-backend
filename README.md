# 🧠 School Vaccination Portal – Backend

Spring Boot backend for managing student vaccination drives and records.

---

## 🚀 Features

- User signup & login (with dummy auth logic)
- Student registration, update, CSV bulk upload
- Vaccination drive creation, update, filters
- Dashboard metrics & reports
- MongoDB integration

---

## 🧱 Tech Stack

- Java 17+
- Spring Boot 3
- Spring Data MongoDB
- Lombok
- MongoDB

---

## ⚙️ Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/school_vaccination_data
server:
  port: 8081
```

---

## ▶️ Run the App

### Using Maven
```bash
./mvnw spring-boot:run
```

Backend runs at: [http://localhost:8081](http://localhost:8081)

---

## 📂 Key Endpoints

### Auth
- `POST /auth/login`
- `POST /auth/signup`

### Students
- `POST /status/students`
- `GET /status/students`
- `PUT /status/students/{id}`
- `POST /status/students/bulk-upload`

### Vaccination Drives
- `POST /status/drives`
- `GET /status/drives`
- `PUT /status/drives/{id}`

### Dashboard & Reports
- `GET /status/dashboard`
- `GET /status/report?vaccineName=...`

---

## 🗂 Package Structure

```
com.kartike.schoolvaccinationportal
├── controller
├── model
├── repository
├── service
└── SchoolVaccinationPortalApplication.java
```

---

## 📝 License

This project is part of an academic assignment (SE ZG503 - BITS Pilani).
