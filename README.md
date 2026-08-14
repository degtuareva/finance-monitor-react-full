# Finance Monitor: Spring Boot + React

Приложение для учета доходов/расходов, аналитики и экспорта отчетов в PDF/Excel.

## Стек

- Backend: Java 11+, Spring Boot 2.7, Spring Security, JPA, JDBC, PostgreSQL
- Frontend: React + Vite, Axios, React Router, Chart.js
- Reports: OpenPDF и Apache POI
- Tests: JUnit 5, Mockito, MockMvc

## Структура

```text
finance-monitor/
├── src/                 # Spring Boot backend
├── frontend/            # React SPA
├── application.yml
├── application-dev.yml
├── application-prod.yml
└── docker-compose.yml
```

## Предварительные требования

- JDK 21+ (рекомендуется JDK 17 или 21)
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+ или Docker Desktop

## Запуск PostgreSQL

### Docker

```bash
docker compose up -d postgres
```

База будет создана автоматически: `finance_db`, пользователь: `postgres`, пароль: `postgres`.

### Локальный PostgreSQL

```powershell
psql -U postgres -c "CREATE DATABASE finance_db;"
```

Настройте пароль в переменной окружения или в `application-dev.yml`:

```powershell
$env:DB_PASSWORD="ваш_пароль"
```

## Запуск backend

В корневой папке проекта:

```bash
mvn clean package
mvn spring-boot:run
```

Backend доступен на `http://localhost:8080`.

Запуск production-профиля:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="finance_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="пароль"
$env:CORS_ALLOWED_ORIGINS="https://example.com"
java -jar target/finance-monitor-1.0.0.jar
```

## Запуск frontend

Во втором терминале:

```bash
cd frontend
npm install
npm run dev
```

Откройте `http://localhost:5173/register`.

## API

- `POST /api/auth/register`
- `GET, POST /api/categories`
- `DELETE /api/categories/{id}`
- `GET, POST /api/transactions`
- `PUT, DELETE /api/transactions/{id}`
- `GET /api/analytics/metrics?from=YYYY-MM-DD&to=YYYY-MM-DD`
- `GET /api/reports/preview?period=MONTH&format=PDF`
- `GET /api/reports/export?period=MONTH&format=PDF`

## Тесты

```bash
mvn test
```

Включены unit-тест `TransactionServiceTest` (Mockito) и web-layer тест `AuthApiControllerTest` (MockMvc).

## Важно о SPA-аутентификации

Этот учебный архив содержит регистрацию и защищенные бизнес-API через Spring Security, но полноценный React-login/JWT не завершен. Для production добавьте `POST /api/auth/login` с JWT либо session-login endpoint с CSRF-защитой. До этого используйте серверную/Basic-auth схему только для локальной отладки.

## Лицензии

OpenPDF и Apache POI имеют собственные лицензии; перед коммерческим использованием проверьте их актуальные условия.
