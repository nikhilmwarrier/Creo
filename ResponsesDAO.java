import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ResponsesDAO {
    public void insert(Response response) {
        String query = "INSERT INTO Responses(Request_ID, Status_Code, Header, Body, Summary) VALUES(?, ?, ?, ?, ?);";
        try (Connection con = DBHandle.connect() ; PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, response.getRequestID());
            p.setInt(2, response.getStatusCode());
            p.setString(3, response.getHeaders());
            p.setString(4, response.getBody());
            p.setString(5, response.getSummary()); // most likely null

            p.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Response FindByID(int id) {
        String query = "SELECT * FROM Responses WHERE id=?;";
        try (Connection con = DBHandle.connect() ; PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                return new Response(
                        r.getInt("ID"),
                        r.getInt("Request_ID"),
                        r.getInt("Status_Code"),
                        r.getString("Headers"),
                        r.getString("Body"),
                        r.getString("Summary"),
                        r.getString("Timestamp")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

    public List<Response> GetAll() {
        List<Response> responses = new ArrayList<>();
        String query = "SELECT * FROM Responses;";
        try (Connection con = DBHandle.connect(); Statement s = con.createStatement(); ResultSet r = s.executeQuery(query)) {
            while (r.next()) {
                Response res = new Response(
                        r.getInt("ID"),
                        r.getInt("Request_ID"),
                        r.getInt("Status_Code"),
                        r.getString("Headers"),
                        r.getString("Body"),
                        r.getString("Summary"),
                        r.getString("Timestamp")
                );
                responses.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public void delete(int id) {
        String query = "DELETE FROM Responses WHERE ID=?;";
        try (Connection con = DBHandle.connect() ; PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
