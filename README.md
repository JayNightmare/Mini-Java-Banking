# Core Banking Application

## Setup Instructions

### 1. Database Setup
Make sure you have PostgreSQL running with the following configuration:
- Database: `bank`
- Username: `bank` 
- Password: `bank`
- Port: `5432`

### 2. API Keys Setup

You have two options to configure your OpenAI API key:

#### Option A: Using application-secrets.yml (Recommended)
1. Copy the template file:
   ```bash
   cp src/main/resources/application-secrets.yml.template src/main/resources/application-secrets.yml
   ```
2. Edit `src/main/resources/application-secrets.yml` and replace `your-actual-openai-key-here` with your actual OpenAI API key
3. The file will be automatically ignored by Git

#### Option B: Using Environment Variables
Set the environment variable before running the application:
```bash
# Windows
set OPENAI_API_KEY=your-actual-openai-key-here

# Linux/Mac
export OPENAI_API_KEY=your-actual-openai-key-here
```

### 3. Running the Application

```bash
# Build and run
./gradlew bootRun

# Or just build
./gradlew build
```

The application will start on `http://localhost:8080`

### 4. Security Notes

- Never commit your actual API keys to version control
- The `application-secrets.yml` file is excluded from Git
- Use environment variables in production environments
- The application includes a fallback dummy key for development testing

## Web Interface

The application includes a complete web-based GUI accessible at:

### Main Interface
- **Dashboard**: `http://localhost:8080/` - Overview and quick actions
- **Customers**: `http://localhost:8080/customers` - Customer management
- **Accounts**: `http://localhost:8080/accounts` - Account management  
- **Transactions**: `http://localhost:8080/transactions` - Transaction history and transfers

### Features
- 📊 **Dashboard**: Statistics overview and quick actions
- 👥 **Customer Management**: Create and view customers
- 💳 **Account Management**: Create accounts with auto-generated IBANs
- 💸 **Money Transfers**: Transfer money between accounts with transaction history
- 📱 **Responsive Design**: Works on desktop and mobile devices
- 🎨 **Modern UI**: Bootstrap-based professional banking interface

## API Endpoints

- Health Check: `GET /actuator/health`
- Application Info: `GET /actuator/info`
- Customer API: `POST /api/v1/customers`

## Development

The application uses:
- Spring Boot 3.5.5
- Java 21
- PostgreSQL 17
- Flyway for database migrations
- Spring Security
- Spring AI with OpenAI integration