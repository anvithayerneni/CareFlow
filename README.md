# CareFlow

[![Live Demo](https://img.shields.io/badge/Live%20Demo-careflow--frontend.onrender.com-00B4D8?style=for-the-badge&logo=render&logoColor=white)](https://careflow-frontend-bzw3.onrender.com/)
[![CI](https://img.shields.io/github/actions/workflow/status/anvithayerneni/CareFlow/ci.yml?style=for-the-badge&label=CI)](https://github.com/anvithayerneni/CareFlow/actions)

> 🚀 **Live Demo:** **[https://careflow-frontend-bzw3.onrender.com/](https://careflow-frontend-bzw3.onrender.com/)**
>
> Experience the interactive patient, doctor, and admin workspaces directly in your browser.

CareFlow is a **synthetic-data portfolio demonstration**, not a clinical system. It provides a patient, clinician, and administrator workflow for exploring scheduling and care operations. Do not enter real patient information. The informational assistant does not diagnose or recommend treatment.

## Stack

- React, TypeScript, Vite, React Router, TanStack Query, Tailwind, React Hook Form, Zod, Recharts, Axios
- Java 21, Spring Boot 3, Spring Security, JPA, Flyway, Actuator, Kafka, Redis
- PostgreSQL, Redis, Kafka, MinIO for local S3-compatible object storage

## Run locally

1. Copy `.env.example` to `.env` and replace secrets for any non-local deployment.
2. Start the full stack: `docker compose up --build` (UI at `http://localhost:3000`; API at `http://localhost:8080`).
3. For live frontend development, start infrastructure with `docker compose up -d postgres redis kafka minio`, then run the API with `cd backend && mvn spring-boot:run` (requires Java 21) and the UI with `npm install && npm run dev`.

The frontend includes a demo workspace with a role switcher. Seeded demo accounts use `sophie@example.test`, `maya@example.test`, and `admin@example.test` with the shared local-only password `CareFlowDemo2024!`. API requests target `/api/v1`; Vite proxies them to port 8080. API docs are at `/swagger-ui.html`, and health is exposed through `/actuator/health`.

## Free online demo

**Live demo:** [CareFlow on Render](https://careflow-frontend-bzw3.onrender.com/)

The project includes a free-tier deployment guide in [`docs/free-deployment.md`](docs/free-deployment.md). Its free profile keeps the UI, API, PostgreSQL, private file storage, and booking notifications working without Redis or Kafka. Free services may sleep or pause when inactive; this is a portfolio demo, not a clinical system.

## Architecture and safety notes

The backend is a domain-organized modular monolith. Controllers validate and translate HTTP requests; services own workflows; repositories own persistence. Flyway migrations establish UUID keys, foreign keys, indexes, and a database-level exclusion constraint to prevent overlapping active appointments for a doctor. Files are represented as object-storage metadata; binary payloads must be stored in MinIO/S3, never in PostgreSQL.

Redis is intended for short-lived slot holds and availability/search cache entries. Kafka carries appointment and notification events; consumers should be idempotent because delivery is at least once. A production deployment should add an outbox, managed secrets, TLS, backups, alerting, and object lifecycle policies. Local credentials are for development only.

This project is a demonstration and makes no HIPAA-compliance claim. The assistant returns general informational content from demo data only and must not be used for diagnosis or treatment decisions.
