import java.sql.*;

public class DBHandle {
    private static final String url = "jdbc:sqlite:oop.db";

    public static Connection connect() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
            // System.out.println("Connected successfully!");
        } catch (SQLException e) {
            System.out.println("Error occurred");
            e.printStackTrace();
        }
        return con;
    }

    public static void Initialize() {
        try (Connection con = connect(); Statement s = con.createStatement()) {
            s.execute("PRAGMA foreign_keys=ON;");
            s.execute("CREATE TABLE IF NOT EXISTS Requests (ID INTEGER PRIMARY KEY, Method TEXT, URL TEXT, Headers TEXT, Body TEXT, Timestamp DATETIME DEFAULT current_timestamp);");
            s.execute("CREATE TABLE IF NOT EXISTS Responses(ID INTEGER PRIMARY, Request_ID INTEGER, Status_Code INTEGER, Header TEXT, Body TEXT, Timestamp DATETIME DEFAULT current_timestamp, FOREIGN KEY(Request_ID), Summary TEXT);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
