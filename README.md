# Tic Tac Toe

A full-stack Tic Tac Toe web application.

## Tech Stack
- Frontend: HTML, CSS, JavaScript
- Backend: Java (Spring Boot)
- Database: PostgreSQL (Docker)

## How to Run

### Option 1: Run with Docker
1. Make sure Docker Desktop is open.
2. In the project folder, run:
```bash
docker compose up -d
```
3. Open `http://localhost:3000` in your browser.

To stop:
```bash
docker compose down
```

### Option 2: Run Locally
1. Start PostgreSQL in Docker:
```bash
docker compose up -d postgres
```
2. Run backend:
```bash
cd backend
mvn spring-boot:run
```
3. Open `frontend/index.html` in your browser.
