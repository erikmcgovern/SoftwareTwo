package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.model.Countries;
import java.sql.*;

/**
 * DBCountries - defines the programs behavior when interacting with country objects in the database.
 *
 * @author Erik McGovern
 */
public class DBCountries {

    /**
     * Queries database for all countries.
     * @return ObservableList of all countries.
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries c = new Countries(countryId, countryName);
                countryList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }

    /**
     * Queries database for a country with specific country id.
     * @param countryId country id.
     * @return country with specific country id.
     */
    public static Countries getCountry(int countryId) {

        try{
            String sql = "SELECT * FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                return new Countries(id, countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
