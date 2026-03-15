"""TaskFlow API entry point."""

from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from config import settings
from models.database import Base, engine
from routes import api_router
from middleware.error_handler import unhandled_exception_handler


@asynccontextmanager
async def lifespan(app: FastAPI):
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    print(f"🚀 {settings.app_name} API starting...")
    yield
    print(f"👋 {settings.app_name} API shutting down...")


app = FastAPI(
    title=settings.app_name,
    version="0.1.0",
    lifespan=lifespan,
    docs_url="/api/v1/docs",
    openapi_url="/api/v1/openapi.json",
    redirect_slashes=False,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.cors_origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.exception_handler(Exception)(unhandled_exception_handler)
app.include_router(api_router, prefix="/api/v1")


@app.get("/api/v1/health")
async def health():
    return {"status": "healthy", "version": "0.1.0"}
