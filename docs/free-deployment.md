# Free demo deployment

This setup is for a synthetic-data portfolio demo. It is not a clinical system; do not enter real patient information. The free provider plans can sleep, pause inactive projects, impose quotas, or change their limits. Keep all services on their free plans and do not add a payment method if you want to avoid charges.

## Services

- Cloudflare Pages serves the Vite frontend.
- Render Free runs the Spring Boot API and sleeps after inactivity.
- Supabase Free provides PostgreSQL and private S3-compatible file storage.
- The `free` Spring profile writes appointment notifications to PostgreSQL and uses an in-memory authentication rate limit. The local Docker profile continues to use Kafka and Redis.

## 1. Create the Supabase project

Create a free project, save the database password, and copy the direct or session-pooler PostgreSQL connection details. Use a JDBC URL in Render, for example:

```text
jdbc:postgresql://<host>:5432/postgres?sslmode=require
```

For a pooler username, use the username displayed by Supabase (often `postgres.<project-ref>`). Flyway creates the CareFlow schema and synthetic demo accounts at first boot.

In Storage settings, enable S3 protocol access and create a private bucket named `careflow-documents`. Copy its S3 endpoint, access key, secret key, and region. These credentials stay in Render environment variables; never commit them to GitHub.

## 2. Deploy the API on Render

Connect the GitHub repository to Render and create a Blueprint from `render.yaml`. Set the secret variables requested by the Blueprint:

- `DATABASE_URL`, `POSTGRES_USER`, and `POSTGRES_PASSWORD` from Supabase.
- `MINIO_ENDPOINT`, `MINIO_ROOT_USER`, `MINIO_ROOT_PASSWORD`, and `MINIO_REGION` from Supabase Storage's S3 settings.

Keep the service plan set to `Free`. After the first deploy, copy the API's `onrender.com` URL.

## 3. Deploy the frontend on Cloudflare Pages

Connect the GitHub repository to Pages with these build settings:

- Root directory: `frontend`
- Build command: `npm run build`
- Output directory: `dist`
- Environment variable: `VITE_API_BASE_URL=https://<your-api>.onrender.com/api/v1`

After deployment, add the exact Pages origin to the Render API's `CORS_ALLOWED_ORIGIN_PATTERNS` variable if the site does not use a `pages.dev` hostname. Redeploy the API after changing its environment variables.

Seeded demo identities are `sophie@example.test`, `maya@example.test`, and `admin@example.test`; their shared demo password is documented in the project README. These accounts and all sample records are synthetic.
