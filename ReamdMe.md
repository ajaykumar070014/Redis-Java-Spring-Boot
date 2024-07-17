# Redis Docker Setup

This document describes how to set up Redis server Docker containers with different usernames (`staff` and `student`).

## Redis Server for Staff

### Docker Command

```bash
docker run -d --name redis-server-local-staff -p 6999:7000 -e REDIS_USERNAME=staff -e REDIS_PASSWORD=zxcvbnm redis:alpine redis-server --port 7000 --requirepass zxcvbnm
```
## Redis Server for Student

### Docker Command

```bash
docker run -d --name redis-server-local-student -p 7999:8000 -e REDIS_USERNAME=student -e REDIS_PASSWORD=zxcvbnm redis:alpine redis-server --port 8000 --requirepass zxcvbnm
```