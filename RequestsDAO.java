import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestsDAO {
    public void insert(Request request) {
        String query = "INSERT INTO Requests(Method, URL, Headers, Body) VALUES (?, ?, ?, ?);";
        try (Connection con = DBHandle.connect(); PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, request.getMethod());
            p.setString(2, request.getUrl());
            p.setString(3, request.getHeaders());
            p.setString(4, request.getBody());

            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Request FindByID(int id) {
        String query = "SELECT * FROM Requests WHERE ID=?;";
        try (Connection con = DBHandle.connect() ; PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                return new Request(
                        r.getInt("ID"),
                        r.getString("Method"),
                        r.getString("URL"),
                        r.getString("Headers"),
                        r.getString("Body"),
                        r.getString("Timestamp")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

    public List<Request> GetALl() {
        List<Request> requests = new ArrayList<>();

        String query = "SELECT * FROM Requests ORDER BY Timestamp DESC;";
        try (Connection con = DBHandle.connect(); Statement s = con.createStatement() ; ResultSet r = s.executeQuery(query)) {
            while (r.next()) {
                Request req = new Request(
                        r.getInt("ID"),
                        r.getString("Method"),
                        r.getString("URL"),
                        r.getString("Headers"),
                        r.getString("Body"),
                        r.getString("Timestamp")
                );
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void delete(int id) {
        String query = "DELETE FROM Requests WHERE ID=?;";
        try (Connection con = DBHandle.connect(); PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
