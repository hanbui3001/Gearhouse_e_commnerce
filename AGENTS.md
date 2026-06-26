# Repository Guidelines

## Project Structure & Module Organization

This repository contains a Spring Boot backend at the root and a Next.js frontend in `frontend/`.

- Backend source lives in `src/main/java/com/example/demo_ecommerce`, organized by responsibility: `controller`, `service`, `repository`, `model`, `dto`, `mapper`, `configuration`, `security`, `validation`, and related utility packages.
- Backend resources live in `src/main/resources`, including `application.yml`, `static/`, and `templates/`.
- Backend tests live in `src/test/java/com/example/demo_ecommerce`.
- Frontend source lives in `frontend/src`, with app routes in `frontend/src/app`, shared UI in `frontend/src/components`, helpers in `frontend/src/lib`, and public assets in `frontend/public`.

## Build, Test, and Development Commands

- `.\gradlew.bat bootRun` runs the Spring Boot API locally.
- `.\gradlew.bat build` compiles the backend and runs tests.
- `.\gradlew.bat test` runs the JUnit test suite.
- `cd frontend; npm run dev` starts the Next.js development server.
- `cd frontend; npm run build` creates a production frontend build.
- `cd frontend; npm run lint` runs ESLint for the frontend.

Use the Gradle wrapper rather than a system Gradle install. Use `npm install` in `frontend/` when dependencies are missing.

## Coding Style & Naming Conventions

Use Java 21 conventions with 4-space indentation. Keep controllers thin, place business logic in services, and keep persistence access in repositories. Name DTOs by direction and purpose, such as `CreateProductRequest` or `UserDetailResponse`. Use MapStruct mappers for entity/DTO conversion when possible, and Lombok consistently with existing models and DTOs.

For frontend code, use TypeScript, React components in PascalCase, hooks/helpers in camelCase, and keep route-specific code under `frontend/src/app`.

## Testing Guidelines

Backend tests use JUnit Platform through Gradle. Place tests beside the matching package under `src/test/java`, and name test classes with the `*Test` or `*Tests` suffix. Prefer service and controller tests for behavior changes, and add repository tests when persistence queries change.

Before opening a PR, run `.\gradlew.bat test`. For frontend changes, also run `cd frontend; npm run lint`.

## Commit & Pull Request Guidelines

Recent commits use short, imperative messages, sometimes with a lightweight prefix, for example `add feat: delete-role service` or `create feature product management`. Keep commits focused and describe the changed behavior.

Pull requests should include a short summary, test results, linked issues when applicable, and screenshots or screen recordings for visible frontend changes. Mention any database, Redis, environment, or API contract changes explicitly.

## Security & Configuration Tips

Do not commit secrets from `.env` or `frontend/.env.local`. Keep environment-specific database, Redis, JWT, and API URL values outside source control. When changing configuration, update sample documentation or PR notes so other contributors can run the project locally.
