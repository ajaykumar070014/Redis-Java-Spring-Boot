## Setting Up Redis for Local Development
### 1. Download Redis:

- Go to GitHub to install Redis for Windows https://github.com/tporadowski/redis/releases.
- Download `Redis-x64-5.0.14.1.zip` from the latest stable release.`
### 2. Run Redis Server:

- Extract the downloaded ZIP file.
- Navigate to the extracted directory and locate redis-server.exe.
- Double-click redis-server.exe to start the Redis server.
### 3. Configure Your Spring Boot Application:

- Ensure your Spring Boot application is configured to use Redis.
- Update application properties or configuration classes to specify the Redis host and port.
### 4. Verify Redis Connection:

- Start your Spring Boot application.
- Check that the application connects to Redis at localhost:6379 (default port).

### 5. Testing Redis Integration:

- Use endpoints in your Spring Boot application that interact with Redis.
- Example endpoint: `http://localhost:5000/api/students` 
### 6. Additional Notes:

- Make sure Redis is running whenever you start your Spring Boot application for cache operations.
