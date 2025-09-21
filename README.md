# Postman Clone - HTTP Client with Database Integration# Simple HTTP Client - College Project



A complete HTTP client application built in Java that mimics Postman functionality with automatic database persistence.## What This Project Does



## FeaturesThis is a **simple HTTP client application** similar to Postman, built for a college project. It allows you to:



✅ **HTTP Operations**: Support for GET, POST, PUT, DELETE requests  1. **Send HTTP Requests**: GET, POST, PUT, DELETE to any URL

✅ **Database Integration**: Automatic storage of all requests and responses  2. **Add Custom Headers**: Set Content-Type, Authorization, etc.

✅ **SQLite Database**: Lightweight database with no setup required  3. **Send Request Body**: For POST/PUT requests (JSON, XML, text)

✅ **Response Analysis**: Status codes, headers, body, response time  4. **View Responses**: See status codes, headers, and formatted response body

✅ **Simple Architecture**: Clean, beginner-friendly code structure  5. **Store History**: Requests and responses are saved in a SQLite database



## Project Structure## Project Structure



``````

├── PostmanApp.java           # Main application demonstrating functionalityCreo/

├── HttpClientService.java    # Core HTTP client implementation  ├── SimpleHTTPClientUI.java      # Main UI application (FRONTEND)

├── PostmanBackendService     # Integration layer (Backend + Database)├── backend/

├── DBHandle.java            # Database connection and initialization│   └── HttpClientService.java  # HTTP client service (BACKEND)

├── Request.java             # Request data model├── database/

├── Response.java            # Response data model  │   ├── DBHandle.java           # Database connection

├── RequestsDAO.java         # Database operations for requests│   ├── Request.java            # Request data model

├── ResponsesDAO.java        # Database operations for responses│   ├── Response.java           # Response data model

├── sqlite-jdbc-3.50.3.0.jar # SQLite JDBC driver│   ├── RequestsDAO.java        # Request database operations

└── oop.db                   # SQLite database (auto-created)│   └── ResponsesDAO.java       # Response database operations

```└── README.md                   # This file

```

## How to Run

## How to Run

### Compile

```bash### Easy Way (Recommended for College Demo):

javac -cp "sqlite-jdbc-3.50.3.0.jar;." *.java

```1. **Compile the simple UI**:

   ```bash

### Run Demo   javac SimpleHTTPClientUI.java

```bash   ```

java -cp "sqlite-jdbc-3.50.3.0.jar;." PostmanApp

```2. **Run the application**:

   ```bash

## What the Demo Does   java SimpleHTTPClientUI

   ```

1. **GET Request**: Fetches a post from JSONPlaceholder API

2. **POST Request**: Creates a new post with JSON data### Features of the Simple UI:

3. **Database Storage**: Saves both requests and responses automatically

4. **Verification**: Shows database contents to confirm storage- **URL Field**: Enter any website URL (e.g., https://jsonplaceholder.typicode.com/posts/1)

- **Method Dropdown**: Choose GET, POST, PUT, or DELETE

## Key Classes- **Headers Area**: Add headers like `Content-Type: application/json`

- **Body Area**: Add JSON or text data for POST/PUT requests

### PostmanBackendService- **Send Button**: Execute the request

- Main integration class that combines HTTP operations with database storage- **Response Area**: See the complete response with status, headers, and body

- `handleRequest(url, method, headers, body)` - Sends HTTP request and saves to database

### Example URLs to Test:

### HttpClientService  

- Core HTTP client using Java's built-in HttpClient1. **GET Request**:

- Handles all HTTP operations (GET, POST, PUT, DELETE)   - URL: `https://jsonplaceholder.typicode.com/posts/1`

- Provides response parsing and error handling   - Method: GET

   - Headers: (empty or default)

### Database Layer

- **DBHandle**: Database connection and table creation2. **POST Request**:

- **RequestsDAO/ResponsesDAO**: Data access objects for CRUD operations   - URL: `https://jsonplaceholder.typicode.com/posts`

- **Request/Response**: Data models matching database schema   - Method: POST

   - Headers: `Content-Type: application/json`

## Usage Example   - Body: `{"title": "My Post", "body": "Post content", "userId": 1}`



```java## Technical Explanation (For Your Project Report)

// Create service

PostmanBackendService service = new PostmanBackendService();### Frontend (UI Layer):

- **Technology**: Java Swing

// Prepare headers- **Purpose**: Provides user interface for entering requests and viewing responses

Map<String, String> headers = new HashMap<>();- **Components**: Text fields, buttons, text areas for user interaction

headers.put("Content-Type", "application/json");

### Backend (Business Logic):

// Send request (automatically saves to database)- **Technology**: Java HTTP Client (built-in since Java 11)

HttpResponse response = service.handleRequest(- **Purpose**: Handles actual HTTP communication with external APIs

    "https://api.example.com/data", - **Features**: Supports all HTTP methods, custom headers, request timeouts

    "GET", 

    headers, ### Database Layer:

    null- **Technology**: SQLite (lightweight, file-based database)

);- **Purpose**: Stores request and response history for later review

- **Tables**: 

// Check response  - `Requests`: Stores sent HTTP requests

System.out.println("Status: " + response.getStatusCode());  - `Responses`: Stores received HTTP responses

System.out.println("Body: " + response.getBody());

```### Architecture Pattern:

- **Pattern**: Three-tier architecture (Presentation → Business Logic → Data)

## Database Schema- **Benefits**: Separation of concerns, maintainable code, easy to extend



### Requests Table## Why This Design is Simple:

- ID (Primary Key)

- Method (GET, POST, etc.)1. **Single Main File**: The UI is in one file for easy understanding

- URL2. **Built-in Libraries**: Uses Java's built-in HTTP client (no external dependencies)

- Headers  3. **Clear Separation**: Frontend talks to backend, backend talks to database

- Body4. **Easy to Demo**: Just compile and run one file to show working application

- Timestamp

## College Project Benefits:

### Responses Table

- ID (Primary Key)- ✅ Shows understanding of **GUI development**

- Request_ID (Foreign Key)- ✅ Demonstrates **HTTP protocol knowledge**

- Status_Code- ✅ Includes **database integration**

- Headers- ✅ Uses **object-oriented programming**

- Body- ✅ Implements **MVC-like architecture**

- Content_Type- ✅ Easy to explain and demonstrate

- Timestamp

## Extending the Project:

## Benefits

1. Add request history viewer

- **Educational**: Simple, clean code perfect for learning2. Implement request collections/folders

- **Complete**: Full HTTP client functionality3. Add authentication support

- **Persistent**: All data automatically saved to database4. Export/import request collections

- **Lightweight**: No external dependencies except SQLite driver5. Add response time analytics
- **Portable**: Single JAR file dependency

## Perfect for College Projects

This codebase demonstrates:
- Object-oriented programming concepts
- Database integration patterns  
- HTTP communication
- Error handling
- Clean architecture principles
- Real-world API usage

---

**Created by**: College Project Team  
**Purpose**: Learn HTTP client development with database integration  
**Language**: Java 11+  
**Database**: SQLite  