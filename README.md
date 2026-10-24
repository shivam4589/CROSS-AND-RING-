# Full-Stack Tic-Tac-Toe

A full-stack Tic-Tac-Toe web application built with Vanilla JavaScript, Java (Spring Boot 3), and PostgreSQL (Docker).

## Tech Stack
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API), served via Nginx
- **Backend**: Java 21, Spring Boot 3.3.4, Spring Data JPA
- **Database**: PostgreSQL 16
- **Orchestration**: Docker Compose

## Quick Start (One Command)

Make sure Docker Desktop is running, then run:

```bash
docker compose up --build -d
```

That's it!
- **Frontend Game UI**: Open [http://localhost:3000](http://localhost:3000)
- **Backend REST API**: Running at [http://localhost:8080/api/games](http://localhost:8080/api/games)
- **PostgreSQL Database**: Running at `localhost:5432`

To stop all services:
```bash
docker compose down
```

---

## Local Development (Without Dockerizing App)

If you prefer running the backend in your IDE/terminal while developing:

1. Start only PostgreSQL:
   ```bash
   docker compose up -d postgres
   ```
2. Run backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3. Open `frontend/index.html` in your browser.
