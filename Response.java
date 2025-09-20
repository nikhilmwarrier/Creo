public class Response {
    private int id, request_id, stat_code;
    private String headers, body, summary, timestamp;

    public Response(int id, int request_id, int stat_code, String headers, String body, String summary, String timestamp) {
        this.id = id;
        this.request_id = request_id;
        this.stat_code = stat_code;
        this.headers = headers;
        this.body = body;
        this.summary = summary;
        this.timestamp = timestamp;
    }

    // Getters
    public int getID() { return id;}
    public int getRequestID() { return request_id; }
    public int getStatusCode() { return stat_code; }
    public String getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getSummary() { return summary; }
    public String getTimestamp() { return timestamp; }
}
