# Webhook SQL Solver - Spring Boot Application

A Spring Boot application that automatically generates a webhook, solves a SQL problem based on the response, and submits the solution.

## Features

- Automatically runs on application startup (no controller/endpoint needed)
- Sends POST request to generate webhook with user details
- Solves SQL problem based on registration number (odd/even determines question)
- Submits solution using JWT token authentication
- Uses RestTemplate for HTTP communication

## Project Structure

```
src/main/java/com/bajaj/
├── WebhookSolverApplication.java      # Main Spring Boot application
├── component/
│   └── WebhookStartupComponent.java   # Triggers flow on startup
├── config/
│   ├── AppProperties.java             # Configuration properties
│   └── RestTemplateConfig.java        # RestTemplate bean configuration
├── dto/
│   ├── WebhookGenerateRequest.java    # Request DTO for webhook generation
│   ├── WebhookGenerateResponse.java   # Response DTO from webhook generation
│   └── WebhookSubmitRequest.java      # Request DTO for solution submission
└── service/
    ├── WebhookService.java            # Main service handling webhook flow
    └── SqlSolverService.java          # Service for solving SQL problems
```

## Configuration

Update `src/main/resources/application.properties` with your details:

```properties
app.user.name=Y Rushik Kumar
app.user.regNo=22BRS1271
app.user.email=rushik7078@gmail.com
```

## How It Works

1. **On Startup**: The `WebhookStartupComponent` automatically triggers the flow using `@PostConstruct`
2. **Generate Webhook**: Sends POST request to generate webhook endpoint with user details
3. **Receive Response**: Gets webhook URL and accessToken from the response
4. **Solve SQL Problem**: Determines which question to solve based on last two digits of registration number
   - Odd → Question 1
   - Even → Question 2
5. **Submit Solution**: Sends the SQL query to the webhook URL with JWT token in Authorization header

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build the application
```bash
mvn clean package
```

### Run the application
```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/webhook-solver-1.0.0.jar
```

## Implementation Notes

- The SQL problem solving logic is in `SqlSolverService.solveQuestion1()` and `solveQuestion2()`
- Currently, these methods contain placeholder queries - update them with the actual SQL solution once you receive the question
- The application uses RestTemplate for HTTP communication
- JWT token is sent in the Authorization header as: `Bearer <accessToken>`
- All operations are logged for debugging purposes

## GitHub Setup for Submission

See [GITHUB_SETUP.md](GITHUB_SETUP.md) for detailed instructions on:
- Building the JAR file
- Creating a public GitHub repository
- Getting the raw download link for the JAR
- Setting up releases

### Quick Setup Commands

```bash
# Build JAR
mvn clean package

# Initialize Git
git init
git add .
git commit -m "Initial commit"

# Connect to GitHub (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/webhook-solver.git
git branch -M main
git push -u origin main
```

### JAR Download Links

After pushing to GitHub, use one of these formats:

**From main branch:**
```
https://raw.githubusercontent.com/YOUR_USERNAME/webhook-solver/main/target/webhook-solver-1.0.0.jar
```

**From releases (recommended):**
```
https://github.com/YOUR_USERNAME/webhook-solver/releases/download/v1.0.0/webhook-solver-1.0.0.jar
```

## Next Steps

1. Run the application to receive the SQL question
2. Update the `solveQuestion1()` or `solveQuestion2()` method in `SqlSolverService.java` with your SQL solution
3. The solution will be automatically submitted on the next run
4. Follow the GitHub setup guide to create a public repository with the JAR file

