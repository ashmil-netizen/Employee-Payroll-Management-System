# Employee Payroll Management System

A full-stack payroll management project with a React web interface and a Spring Boot REST API. It demonstrates employee administration, attendance tracking, leave requests, and payroll calculations.

> **Project/demo use only:** The application includes sample accounts and simplified authentication. Do not use the demo credentials or this application with real employee or payroll data.

## Features

- Login screens for an administrator and employees.
- Dark and light themes with the selected appearance saved in the browser.
- Admin dashboard to view employees and attendance, add employees, and review leave requests.
- Employee dashboard to view payroll information, record attendance, and submit leave requests.
- Availability tracking with available/unavailable status options.
- Employee types for full-time, part-time, and contract staff.
- REST API backed by Spring Data JPA.
- H2 in-memory database for a quick local start, with optional PostgreSQL configuration.

## Technology

- **Frontend:** React 18, React Router, Vite, Tailwind CSS
- **Backend:** Java 17+, Spring Boot 3.3, Spring Web, Spring Data JPA, Bean Validation
- **Database:** H2 by default; PostgreSQL driver is also included

## Requirements

Install these before starting:

- Git
- Java Development Kit (JDK) 17 or newer
- Apache Maven 3.6.3 or newer
- Node.js 18 or newer (includes npm)

Check that the tools are available in a terminal:

```powershell
git --version
java -version
mvn -version
node --version
npm --version
```

## Clone the project

Open PowerShell or a terminal, choose a directory where you want the project, and run:

```powershell
git clone https://github.com/ashmil-netizen/Employee-Payroll-Management-System.git
cd Employee-Payroll-Management-System
```

## Run locally

The backend and frontend run as separate processes. Open **two terminals** in the project directory.

### 1. Start the backend

In the first terminal:

```powershell
cd backend
mvn spring-boot:run
```

The Spring Boot API starts at `http://localhost:8080`. Leave this terminal running while you use the app.

By default, the backend uses an H2 in-memory database and creates the sample departments and employees on startup. The database is temporary: its data is lost when the backend stops.

### 2. Start the frontend

In a second terminal, from the project directory:

```powershell
cd frontend
npm ci
npm run dev
```

Vite prints a local URL when it is ready, normally `http://localhost:5173`. Open that URL in your browser and choose a demo account on the login screen.

For later runs, dependencies do not need to be installed again unless the package lock or dependencies change; start the frontend with `npm run dev`.

## Demo accounts

| Role | Name | Password |
| --- | --- | --- |
| Administrator | `admin` | `admin123` |
| Employee | `Ashmil` | `ashmil123` |
| Employee | `Rinosh` | `rinosh123` |

These are sample credentials included in the source code for local demonstration. They are not suitable for a deployed or production system.

## Database configuration

No separate database installation is needed for the default local setup. The backend uses H2 in memory.

For a local PostgreSQL database, create a database first, then set these environment variables before starting the backend:

- `DB_URL` — JDBC connection URL, for example `jdbc:postgresql://localhost:5432/payroll`
- `DB_USERNAME` — database username
- `DB_PASSWORD` — database password

In PowerShell, set them in the backend terminal before running Maven:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/payroll"
$env:DB_USERNAME = "your_username"
$env:DB_PASSWORD = "your_password"
mvn spring-boot:run
```

Never commit real credentials or `.env` files.

## Project layout

```text
.
├── backend/
│   ├── pom.xml                    # Maven dependencies and backend build
│   └── src/main/java/com/oops/payroll/
│       ├── controller/            # REST API endpoints
│       ├── dto/                   # Request data objects
│       ├── model/                 # Employee, payroll, leave, and attendance entities
│       ├── repository/            # Spring Data JPA repositories
│       └── service/               # Application and payroll logic
├── frontend/
│   ├── package.json               # Frontend scripts and dependencies
│   └── src/
│       ├── pages/                 # Login, admin, employee, attendance, leave, and salary screens
│       └── services/api.js        # Calls to the backend API
└── Employee Payroll Management System (1).pptx  # Project presentation
```

## API overview

The backend serves its REST API under `http://localhost:8080/api`:

- `POST /login` — authenticate a demo account.
- `/employees` and `/departments` — list, add, or remove employees and list departments.
- `/attendance` — list attendance, view an employee's summary, or record attendance.
- `/leave` — submit and list leave requests, and approve or reject them.
- `/payroll/{employeeId}` — calculate and return an employee's current payroll details.

## Troubleshooting

- **`mvn` is not recognized:** Install Maven and add its `bin` directory to your `PATH`, then open a new terminal.
- **`npm` or `node` is not recognized:** Install Node.js 18 or newer, then open a new terminal.
- **The page says “Failed to fetch”:** Make sure the backend is still running at `http://localhost:8080`.
- **Port 8080 or 5173 is already in use:** Stop the other process using that port, then restart the corresponding app.
- **Frontend dependencies are missing:** Run `npm ci` from the `frontend` directory.

## Tests and production builds

Run backend tests from `backend`:

```powershell
mvn test
```

Build the frontend from `frontend`:

```powershell
npm ci
npm run build
```
