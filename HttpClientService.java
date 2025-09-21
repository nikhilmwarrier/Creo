
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// Main HTTP Client Service Class
public class HttpClientService {
    
    private final HttpClient httpClient;
    private final Duration timeout;
    
    public HttpClientService() {
        this.timeout = Duration.ofSeconds(30);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
    
    // Main method to handle all HTTP operations
    public HttpResponse executeRequest(HttpRequestData requestData) {
        try {
            switch (requestData.getMethod().toUpperCase()) {
                case "GET":
                    return performGet(requestData);
                case "POST":
                    return performPost(requestData);
                case "PUT":
                    return performPut(requestData);
                case "DELETE":
                    return performDelete(requestData);
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method: " + requestData.getMethod());
            }
        } catch (Exception e) {
            return createErrorResponse(e);
        }
    }
    
    // GET Request Implementation
    private HttpResponse performGet(HttpRequestData requestData) throws Exception {
        validateUrl(requestData.getUrl());
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestData.getUrl()))
                .GET()
                .timeout(timeout);
        
        addHeaders(requestBuilder, requestData.getHeaders());
        
        HttpRequest request = requestBuilder.build();
        java.net.http.HttpResponse<String> response = httpClient.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
        
        return processResponse(response, requestData.getUrl());
    }
    
    // POST Request Implementation
    private HttpResponse performPost(HttpRequestData requestData) throws Exception {
        validateUrl(requestData.getUrl());
        
        HttpRequest.BodyPublisher bodyPublisher = createBodyPublisher(requestData);
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestData.getUrl()))
                .POST(bodyPublisher)
                .timeout(timeout);
        
        addHeaders(requestBuilder, requestData.getHeaders());
        
        HttpRequest request = requestBuilder.build();
        java.net.http.HttpResponse<String> response = httpClient.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
        
        return processResponse(response, requestData.getUrl());
    }
    
    // PUT Request Implementation   
    private HttpResponse performPut(HttpRequestData requestData) throws Exception {
        validateUrl(requestData.getUrl());
        
        HttpRequest.BodyPublisher bodyPublisher = createBodyPublisher(requestData);
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestData.getUrl()))
                .PUT(bodyPublisher)
                .timeout(timeout);
        
        addHeaders(requestBuilder, requestData.getHeaders());
        
        HttpRequest request = requestBuilder.build();
        java.net.http.HttpResponse<String> response = httpClient.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
        
        return processResponse(response, requestData.getUrl());
    }
    
    // DELETE Request Implementation
    private HttpResponse performDelete(HttpRequestData requestData) throws Exception {
        validateUrl(requestData.getUrl());
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestData.getUrl()))
                .DELETE()
                .timeout(timeout);
        
        addHeaders(requestBuilder, requestData.getHeaders());
        
        HttpRequest request = requestBuilder.build();
        java.net.http.HttpResponse<String> response = httpClient.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
        
        return processResponse(response, requestData.getUrl());
    }
    
    
    // Process HTTP Response and sort by content type
    private HttpResponse processResponse(java.net.http.HttpResponse<String> response, String url) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(response.statusCode());
        httpResponse.setUrl(url);
        httpResponse.setHeaders(response.headers().map());
        
        String contentType = getContentType(response.headers());
        httpResponse.setContentType(contentType);
        
        String body = response.body();
        if (body != null) {
            httpResponse.setBody(formatResponseBody(body, contentType));
            httpResponse.setBodySize(body.getBytes(StandardCharsets.UTF_8).length);
        } else {
            httpResponse.setBody("");
            httpResponse.setBodySize(0);
        }
        
        httpResponse.setResponseTime(System.currentTimeMillis());
        
        return httpResponse;
    }
    
    // Format response body based on content type
    private String formatResponseBody(String body, String contentType) {
        if (body == null || body.trim().isEmpty()) {
            return "";
        }
        
        try {
            if (contentType.contains("application/json")) {
                return formatJsonResponse(body);
            } else if (contentType.contains("text/html")) {
                return formatHtmlResponse(body);
            } else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
                return formatXmlResponse(body);
            } else {
                return body; 
            }
        } catch (Exception e) {
            return body;
        }
    }
    
    // Basic JSON formatting (pretty print)
    private String formatJsonResponse(String json) {
        json = json.trim();
        if (!json.startsWith("{") && !json.startsWith("[")) {
            return json;
        }
        
        StringBuilder formatted = new StringBuilder();
        int indent = 0;
        boolean inString = false;
        boolean escape = false;
        
        for (char c : json.toCharArray()) {
            if (escape) {
                formatted.append(c);
                escape = false;
                continue;
            }
            
            if (c == '\\' && inString) {
                escape = true;
                formatted.append(c);
                continue;
            }
            
            if (c == '"' && !escape) {
                inString = !inString;
            }
            
            if (!inString) {
                switch (c) {
                    case '{':
                    case '[':
                        formatted.append(c).append('\n');
                        indent++;
                        addIndent(formatted, indent);
                        break;
                    case '}':
                    case ']':
                        formatted.append('\n');
                        indent--;
                        addIndent(formatted, indent);
                        formatted.append(c);
                        break;
                    case ',':
                        formatted.append(c).append('\n');
                        addIndent(formatted, indent);
                        break;
                    case ':':
                        formatted.append(c).append(' ');
                        break;
                    default:
                        if (!Character.isWhitespace(c)) {
                            formatted.append(c);
                        }
                }
            } else {
                formatted.append(c);
            }
        }
        
        return formatted.toString();
    }
    
    private void addIndent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent * 2; i++) {
            sb.append(' ');
        }
    }
    
    // Basic HTML formatting
    private String formatHtmlResponse(String html) {
        return html;
    }
    
    // Basic XML formatting
    private String formatXmlResponse(String xml) {
        return xml;
    }
    
    // Utility Methods
    private void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        
        try {
            URI uri = URI.create(url);
            if (uri.getScheme() == null) {
                throw new IllegalArgumentException("URL must include protocol (http:// or https://)");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }
    
    private void addHeaders(HttpRequest.Builder requestBuilder, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach((key, value) -> {
                if (key != null && value != null && !key.trim().isEmpty()) {
                    requestBuilder.header(key.trim(), value.trim());
                }
            });
        }
    }
    
    private HttpRequest.BodyPublisher createBodyPublisher(HttpRequestData requestData) {
        String body = requestData.getBody();
        if (body == null || body.trim().isEmpty()) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8);
    }
    
    private String getContentType(HttpHeaders headers) {
        return headers.firstValue("content-type")
                .or(() -> headers.firstValue("Content-Type"))
                .orElse("text/plain");
    }
    
    private HttpResponse createErrorResponse(Exception e) {
        HttpResponse errorResponse = new HttpResponse();
        errorResponse.setStatusCode(-1);
        errorResponse.setError(true);
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setBody("Error: " + e.getMessage());
        errorResponse.setContentType("text/plain");
        errorResponse.setHeaders(new HashMap<>());
        return errorResponse;
    }
    
    // Async request execution
    public CompletableFuture<HttpResponse> executeRequestAsync(HttpRequestData requestData) {
        return CompletableFuture.supplyAsync(() -> executeRequest(requestData));
    }
}

// Request Data Class
class HttpRequestData {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
    
    public HttpRequestData() {
        this.headers = new HashMap<>();
    }
    
    public HttpRequestData(String url, String method) {
        this.url = url;
        this.method = method;
        this.headers = new HashMap<>();
    }
    
    // Getters and Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }
}

// Response Data Class
class HttpResponse {
    private int statusCode;
    private String url;
    private Map<String, List<String>> headers;
    private String body;
    private String contentType;
    private long responseTime;
    private long bodySize;
    private boolean isError;
    private String errorMessage;
    
    public HttpResponse() {
        this.headers = new HashMap<>();
        this.isError = false;
    }
    
    // Getters and Setters
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public Map<String, List<String>> getHeaders() { return headers; }
    public void setHeaders(Map<String, List<String>> headers) { this.headers = headers; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    
    public long getResponseTime() { return responseTime; }
    public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
    
    public long getBodySize() { return bodySize; }
    public void setBodySize(long bodySize) { this.bodySize = bodySize; }
    
    public boolean isError() { return isError; }
    public void setError(boolean error) { isError = error; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    // Utility methods
    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
    
    public String getStatusText() {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 204: return "No Content";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 502: return "Bad Gateway";
            case 503: return "Service Unavailable";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP ").append(statusCode).append(" ").append(getStatusText()).append("\n");
        sb.append("URL: ").append(url).append("\n");
        sb.append("Content-Type: ").append(contentType).append("\n");
        sb.append("Body Size: ").append(bodySize).append(" bytes\n");
        if (isError) {
            sb.append("Error: ").append(errorMessage).append("\n");
        }
        return sb.toString();
    }
}

// Usage Example / Integration Class - NOW WITH DATABASE INTEGRATION!
class PostmanBackendService {
    private final HttpClientService httpClientService;
    
    public PostmanBackendService() {
        this.httpClientService = new HttpClientService();
        // Initialize database when service is created
        initializeDatabase();
    }
    
    // Initialize database tables
    private void initializeDatabase() {
        try {
            DBHandle.Initialize();
            System.out.println("✓ Database initialized successfully");
        } catch (Exception e) {
            System.err.println("✗ Database initialization failed: " + e.getMessage());
        }
    }
    
    // MAIN METHOD: This now automatically saves to database!
    // When you call this method, it will:
    // 1. Save request to database BEFORE sending
    // 2. Send HTTP request 
    // 3. Save response to database AFTER receiving
    public HttpResponse handleRequest(String url, String method, Map<String, String> headers, String body) {
        System.out.println("\n=== Processing Request ===");
        System.out.println("Method: " + method + " | URL: " + url);
        
        try {
            // Step 1: Save request to database BEFORE sending
            saveRequestToDatabase(method, url, headers, body);
            
            // Step 2: Create request data and send HTTP request
            HttpRequestData requestData = new HttpRequestData(url, method);
            requestData.setHeaders(headers);
            requestData.setBody(body);
            
            HttpResponse httpResponse = httpClientService.executeRequest(requestData);
            
            // Step 3: Save response to database AFTER receiving
            saveResponseToDatabase(httpResponse);
            
            System.out.println("✓ Request-Response cycle completed and saved to database");
            return httpResponse;
            
        } catch (Exception e) {
            System.err.println("✗ Error processing request: " + e.getMessage());
            HttpResponse errorResponse = new HttpResponse();
            errorResponse.setError(true);
            errorResponse.setErrorMessage("Failed to process request: " + e.getMessage());
            return errorResponse;
        }
    }
    
    // Save request to database
    private void saveRequestToDatabase(String method, String url, Map<String, String> headers, String body) {
        try {
            Request request = new Request(
                0, // ID will be auto-generated
                method,
                url,
                headers != null ? headers.toString() : "",
                body != null ? body : "",
                "" // Timestamp will be auto-generated
            );
            
            RequestsDAO requestsDAO = new RequestsDAO();
            requestsDAO.insert(request);
            System.out.println("✓ Request saved to database");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to save request to database: " + e.getMessage());
        }
    }
    
    // Save response to database
    private void saveResponseToDatabase(HttpResponse httpResponse) {
        try {
            Response response = new Response(
                0, // ID will be auto-generated
                0, // Request_ID (simplified for now)
                httpResponse.getStatusCode(),
                httpResponse.getHeaders() != null ? httpResponse.getHeaders().toString() : "",
                httpResponse.getBody() != null ? httpResponse.getBody() : "",
                httpResponse.getContentType() != null ? httpResponse.getContentType() : "unknown", // Content type in summary field
                "" // Timestamp will be auto-generated
            );
            
            ResponsesDAO responsesDAO = new ResponsesDAO();
            responsesDAO.insert(response);
            System.out.println("✓ Response saved to database");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to save response to database: " + e.getMessage());
        }
    }
    
    // Convenience methods for specific HTTP methods
    public HttpResponse get(String url, Map<String, String> headers) {
        return handleRequest(url, "GET", headers, null);
    }
    
    public HttpResponse post(String url, Map<String, String> headers, String body) {
        return handleRequest(url, "POST", headers, body);
    }
    
    public HttpResponse put(String url, Map<String, String> headers, String body) {
        return handleRequest(url, "PUT", headers, body);
    }
    
    public HttpResponse delete(String url, Map<String, String> headers) {
        return handleRequest(url, "DELETE", headers, null);
    }
}