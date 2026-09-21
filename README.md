# SimuladorBancarioAccenture

## Running with Docker

| Environment | File |
|---|---|
| Local | `compose.yaml` |
| GitHub Codespaces | `compose.codespaces.yaml` |

### First run

1. Copy `.env.example`, rename it to `.env` and set your credentials.
2. Build and start:

   ```bash
   docker compose up --build                              # local
   docker compose -f compose.codespaces.yaml up --build   # Codespaces
   ```

3. API on `http://localhost:8080` (Swagger UI at `/swagger-ui.html`). In Codespaces, open port `8080` from the **PORTS** panel.

### Next runs

```bash
docker compose up                              # local
docker compose -f compose.codespaces.yaml up   # Codespaces
```

`-d` runs it detached. `docker compose down` stops it; add `-v` to also wipe the database and Maven cache.

### Which file

- **Local:** `compose.yaml`.
- **GitHub Codespaces:** `compose.codespaces.yaml` (do not use it locally).

## Running with VS Code Dev Containers

Requires the [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) extension and Docker running.

1. Copy `.env.example`, rename it to `.env` and set your credentials.
2. Open the project in VS Code and run **Dev Containers: Reopen in Container** from the command palette (`F1`).
3. VS Code builds the container and installs the Java extensions automatically. API available at `http://localhost:8080`.

## API Documentation

Once the application is running (default port `8080`):

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **OpenAPI YAML:** http://localhost:8080/v3/api-docs.yaml

Endpoints are secured with a JWT bearer token. In Swagger UI, click **Authorize** and provide the token as `Bearer <token>`.
