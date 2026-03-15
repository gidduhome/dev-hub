# Deploy

1. Run tests: `pytest tests/ -v && cd src/frontend && npm run test`
2. Build frontend: `cd src/frontend && npm run build`
3. Build images: `docker compose -f docker-compose.prod.yml build`
4. Migrate DB: `alembic upgrade head`
5. Deploy: `docker compose -f docker-compose.prod.yml up -d`
6. Verify: `curl http://localhost:8000/api/v1/health`
