"""Application configuration loaded from environment."""

from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "TaskFlow"
    debug: bool = False

    # Database
    database_url: str = "postgresql+asyncpg://taskflow:taskflow@localhost:5433/taskflow"

    # CORS
    cors_origins: list[str] = ["http://localhost:5173"]

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


settings = Settings()
