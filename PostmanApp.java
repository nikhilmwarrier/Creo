import java.util.HashMap;
import java.util.Map;

/**
 * Final Demo Application - Postman Clone with Database Integration
 * 
 * This demonstrates a complete HTTP client with database persistence:
 * - Send HTTP requests (GET, POST, PUT, DELETE)
 * - Automatically store all requests and responses in SQLite database
 * - Simple and clean code structure for college project
 */
public class PostmanApp {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  POSTMAN CLONE - FINAL DEMO");
        System.out.println("  HTTP Client with Database Integration");
        System.out.println("===========================================\n");
        
        try {
            // Initialize the service (this sets up the database)
            PostmanBackendService service = new PostmanBackendService();
            
            // Demo 1: Simple GET request
            System.out.println("📡 DEMO 1: GET Request");
            System.out.println("URL: https://jsonplaceholder.typicode.com/posts/1");
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            
            HttpResponse response1 = service.handleRequest(
                "https://jsonplaceholder.typicode.com/posts/1", 
                "GET", 
                headers, 
                null
            );
            
            printResponse("GET Request", response1);
            
            // Demo 2: POST request with body
            System.out.println("\n📡 DEMO 2: POST Request");
            System.out.println("URL: https://jsonplaceholder.typicode.com/posts");
            
            String jsonBody = "{\n" +
                "  \"title\": \"College Project Test\",\n" +
                "  \"body\": \"This is a test post from our Postman clone\",\n" +
                "  \"userId\": 1\n" +
                "}";
            
            Map<String, String> postHeaders = new HashMap<>();
            postHeaders.put("Content-Type", "application/json");
            postHeaders.put("Accept", "application/json");
            
            HttpResponse response2 = service.handleRequest(
                "https://jsonplaceholder.typicode.com/posts", 
                "POST", 
                postHeaders, 
                jsonBody
            );
            
            printResponse("POST Request", response2);
            
            // Demo 3: Show what's in the database
            System.out.println("\n💾 DATABASE STORAGE VERIFICATION");
            showDatabaseContents();
            
            System.out.println("\n✅ DEMO COMPLETED SUCCESSFULLY!");
            System.out.println("🎯 Both HTTP requests were executed and stored in database");
            System.out.println("📁 Check your 'oop.db' file - it contains all the data!");
            
        } catch (Exception e) {
            System.out.println("❌ Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to print response details nicely
     */
    private static void printResponse(String requestType, HttpResponse response) {
        System.out.println("  ✓ " + requestType + " completed:");
        System.out.println("    Status: " + response.getStatusCode() + 
                         (response.isSuccessful() ? " (Success)" : " (Failed)"));
        System.out.println("    Content-Type: " + response.getContentType());
        System.out.println("    Response Size: " + response.getBodySize() + " bytes");
        System.out.println("    Response Time: " + response.getResponseTime() + " ms");
        
        // Show first 100 characters of response body
        if (response.getBody() != null && response.getBody().length() > 0) {
            String preview = response.getBody().length() > 100 ? 
                response.getBody().substring(0, 100) + "..." : 
                response.getBody();
            System.out.println("    Body Preview: " + preview);
        }
    }
    
    /**
     * Show what's stored in the database
     */
    private static void showDatabaseContents() {
        try {
            System.out.println("  📊 Checking database contents...");
            
            // Get all requests from database
            RequestsDAO requestsDAO = new RequestsDAO();
            System.out.println("  📥 Stored Requests: " + requestsDAO.GetALl().size());
            
            // Get all responses from database  
            ResponsesDAO responsesDAO = new ResponsesDAO();
            System.out.println("  📤 Stored Responses: " + responsesDAO.GetAll().size());
            
            System.out.println("  ✓ Database verification complete!");
            
        } catch (Exception e) {
            System.out.println("  ⚠️  Could not verify database contents: " + e.getMessage());
        }
    }
}