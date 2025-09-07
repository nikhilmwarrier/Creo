import java.sql.*;

public class DBHandler {
    private static final String url = "jdbc:sqlite:oop.db";

    public static Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
            System.out.println("Connected successfully");
        } catch (SQLException e) {
            System.out.println("Error Occurred");
            e.printStackTrace();
        }
        return con;
    }

    public static void createTable() {
        try (Connection con = connect(); Statement stmt = con.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute("CREATE TABLE IF NOT EXISTS Requests (ID INTEGER primary key, method text, url text, headers text, body text, timestamp datetime default current_timestamp);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Responses (ID INTEGER primary key, Request_ID INTEGER, Status_Code INTEGER, Header text, Body text, timestamp datetime default current_timestamp, FOREIGN KEY(Request_ID) references Requests(ID));");
            System.out.println("Tables created");
        } catch (SQLException e) {
            System.out.println("Table creation failed");
            e.printStackTrace();
        }
    }

    public static void insertRequest(String method, String url, String headers, String body) {
        String sql = "INSERT INTO REQUESTS(method, url, headers, body) VALUES (?, ?, ?, ?);";
        try (Connection con = connect(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, method);
            pstmt.setString(2, url);
            pstmt.setString(3, headers);
            pstmt.setString(4, body);

            pstmt.executeUpdate();

            System.out.println("Request added");
        } catch (SQLException e) {
            System.out.println("Request failed");
            e.printStackTrace();
        }
    }

    public static void insertResponse(int requestID, int statusCode, String headers, String body) {
        String sql = "INSERT INTO RESPONSES(request_id, status_code, headers, body) VALUES (?, ?, ?, ?);";
        try (Connection con = connect() ; PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, requestID);
            pstmt.setInt(2, statusCode);
            pstmt.setString(3, headers);
            pstmt.setString(4, body);

            pstmt.executeUpdate();

            System.out.println("Response inserted");
        } catch (SQLException e) {
            System.out.println("Response failed to insert");
            e.printStackTrace();
        }
    }

    public static void printRequests() {
        String sql = "SELECT * from Requests;";
        try (Connection con = connect(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Method: "+rs.getString("method"));
                System.out.println("URL: "+rs.getString("url"));
                System.out.println("Headers: "+rs.getString("headers"));
                System.out.println("Body: "+rs.getString("body"));
                System.out.println("----------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

}
