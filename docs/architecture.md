# CareFlow architecture

CareFlow is a domain-oriented Spring Boot modular monolith with a React single-page application. Controllers handle HTTP validation and response shaping; workflow logic and persistence remain in service/controller collaborators and SQL repositories. PostgreSQL is the source of truth.

## Main flows

- **Authentication:** registration creates a patient account; BCrypt stores password hashes. Login issues a signed 15-minute JWT and a random refresh token whose SHA-256 digest is stored in PostgreSQL. Refresh rotates the refresh token. Roles are taken from signed claims and enforced with Spring Security method rules.
- **Scheduling:** doctor availability is expanded into candidate slots. Booking and rescheduling run transactionally; a PostgreSQL GiST exclusion constraint rejects overlapping active appointments even under concurrent requests. The appointment event is sent to Kafka; an idempotent consumer writes an in-app notification using `event_inbox`.
- **Documents:** the API accepts PDF only, validates size, extension, MIME type, and the PDF signature, then stores bytes in MinIO. PostgreSQL stores metadata and the opaque object key. Patient ownership and doctor appointment relationships are checked for list/download access; downloads and uploads are audited.
- **Operations:** Redis limits sensitive auth calls per source address. Actuator exposes health and metrics endpoints. OpenAPI/Swagger documents the REST surface. Docker Compose runs PostgreSQL, Redis, Kafka, MinIO, API, and frontend locally.

## API surface

| Area | Routes |
| --- | --- |
| Authentication | `POST /api/v1/auth/register`, `login`, `refresh`, `logout`; `GET /me` |
| Clinicians | `GET /api/v1/doctors`, `/{id}`, `/{id}/availability?date=YYYY-MM-DD` |
| Appointments | `GET/POST /api/v1/appointments`, `PUT /{id}/reschedule`, `POST /{id}/cancel` |
| Documents | `GET/POST /api/v1/documents`, `GET /{id}/download`, `DELETE /{id}` |
| Notifications | `GET /api/v1/notifications`, `POST /{id}/read` |
| Admin | `GET /api/v1/admin/analytics`, `/audit` |
| Assistant | `POST /api/v1/assistant` (general app information only) |

The local seed is intentionally fictional. The role selector in the UI signs into the matching seeded demo account for API requests. The assistant does not diagnose, interpret records, or recommend treatment.

## Known demonstration limits

Redis currently provides auth rate limits; short-lived slot holds and read caches are natural follow-ups. Database constraints provide the booking conflict guarantee. Appointment event publication should move behind a transactional outbox before production use. Email/SMS delivery, real identity verification, cloud secret management, backups, and clinical integrations are outside this portfolio demo.
