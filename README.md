# Smart Contact Manager (SCM)

<p align="center">
  A modern, secure, and responsive contact management web application built with Java and Spring Boot.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-Framework-6DB33F?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Tailwind%20CSS-Styling-06B6D4?style=for-the-badge&logo=tailwindcss" alt="Tailwind CSS">
</p>

---

## Overview

**Smart Contact Manager (SCM)** is a full-stack web application designed to help users organize, manage, and maintain their personal and professional contacts in one place.

Built using Java, Spring Boot, Spring Security, Hibernate, and Thymeleaf, the application provides a secure and user-friendly platform for managing contact information.

The project demonstrates practical implementation of backend development, database management, authentication, cloud image storage, and responsive frontend design.

## Features

- **User Authentication:** Secure registration and login using Spring Security.
- **OAuth2 Login:** Social authentication using Google and GitHub.
- **Email Verification:** Email-based account verification.
- **Contact Management:** Add, view, update, and delete contacts.
- **Favorite Contacts:** Mark and manage important contacts.
- **Profile Management:** Manage personal account information and profile pictures.
- **Cloud Image Storage:** Upload and manage images using Cloudinary.
- **Search and Pagination:** Navigate and manage contact records efficiently.
- **Data Export:** Export contact information to Excel.
- **Responsive UI:** Modern interface built with Tailwind CSS.
- **Dark Mode:** Theme support for a comfortable user experience.

*Note: Features should be kept in sync with the functionality implemented in the current version of the project.*

## Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot |
| Security | Spring Security |
| Authentication | OAuth2, Google, GitHub |
| ORM | Hibernate, Spring Data JPA |
| Database | MySQL |
| Frontend | HTML5, CSS3, JavaScript |
| UI Framework | Tailwind CSS |
| Template Engine | Thymeleaf |
| Image Storage | Cloudinary |
| Email Service | Mailtrap SMTP |
| Build Tool | Maven |
| Version Control | Git and GitHub |

## Project Architecture

The application follows a layered architecture to separate responsibilities and make the code easier to maintain.

```text
src/
 └── main/
     ├── java/
     │   └── com/scm/
     │       ├── config/
     │       ├── controllers/
     │       ├── entities/
     │       ├── forms/
     │       ├── helpers/
     │       ├── repositories/
     │       ├── services/
     │       └── ScmApplication.java
     │
     └── resources/
         ├── static/
         ├── templates/
         └── application.properties
```

The package structure above illustrates the intended organization; actual package names may differ in your project.

## Getting Started

Follow these instructions to run the project locally.

### Prerequisites

Make sure you have installed:

- Java 21 or compatible configured JDK
- MySQL
- Git
- Maven (optional, since the project includes Maven Wrapper)
- IDE such as IntelliJ IDEA or VS Code

### 1. Clone the Repository

```bash
git clone https://github.com/himanshu-devs/SCM.git
```

### 2. Navigate to the Project

```bash
cd SCM
```

### 3. Create the MySQL Database

Open MySQL and execute:

```sql
CREATE DATABASE scm;
```

### 4. Configure Environment Variables

The application uses environment variables for sensitive configuration.

Configure the following variables before running the application:

| Variable | Description |
|---|---|
| `DB_URL` | MySQL JDBC URL |
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `GOOGLE_CLIENT_ID` | Google OAuth Client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth Client Secret |
| `GITHUB_CLIENT_ID` | GitHub OAuth Client ID |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth Client Secret |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `MAILTRAP_USERNAME` | Mailtrap SMTP username |
| `MAILTRAP_PASSWORD` | Mailtrap SMTP password |

Configure these values locally. Never commit real credentials, API keys, or passwords to GitHub.

### 5. Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

Alternatively, run `ScmApplication.java` from your IDE.

### 6. Open the Application

Visit:

```text
http://localhost:8080
```

## Security

Security is an important part of this project.

- Spring Security for authentication and authorization.
- BCrypt password encoding.
- OAuth2 authentication with Google and GitHub.
- Environment-based configuration for sensitive credentials.
- Email verification support.

## Future Improvements

Potential enhancements include:

- REST API support for contact management.
- Advanced contact filtering and sorting.
- Contact import functionality.
- Automated testing and test coverage improvements.
- Deployment with a production database.
- Docker-based application setup.

## Screenshots

Add screenshots of your application here to showcase its interface.

Suggested screenshots:

1. Home Page
2. Login and Registration Pages
3. User Dashboard
4. Contact Listing
5. Add Contact Form
6. Contact Details Modal
7. User Profile

Example:

```markdown
![Dashboard](screenshots/dashboard.png)
```

## Learning Outcomes

Through this project, I have gained practical experience with:

- Building full-stack applications using Java and Spring Boot.
- Implementing authentication and authorization.
- Working with relational databases using Spring Data JPA.
- Integrating third-party services.
- Designing responsive interfaces with Tailwind CSS.
- Structuring applications using layered architecture.
- Managing source code with Git and GitHub.

## Author

**Himanshu Rajput**

B.Tech – Computer Science and Engineering

GitHub: [@himanshu-devs](https://github.com/himanshu-devs)

---

<p align="center">
  If you find this project interesting, consider giving it a ⭐ on GitHub!
</p>
