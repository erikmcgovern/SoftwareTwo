package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.model.Divisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBDivisions - defines the programs behavior when interacting with division objects in the database.
 *
 * @author Erik McGovern
 */
public class DBDivisions {

    /**
     * Queries database for divisions with a specific country id.
     * @param id country id.
     * @return ObservableList of divisions with specific country id.
     */
    public static ObservableList<Divisions> getDivisionsByCountry(int id) {
        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int divisionId = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                Divisions d = new Divisions(divisionId, name, countryId);
                divisionList.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionList;
    }

    /**
     * Queries database for a division with a specific division id.
     * @param id division id.
     * @return Division with specific division id.
     */
    public static Divisions getDivision(int id) {
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int divisionId = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                return new Divisions(divisionId, name, countryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
