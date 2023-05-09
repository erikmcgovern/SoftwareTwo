package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.model.Contacts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBContacts - defines the programs behavior when interacting with contact objects in the database.
 *
 * @author Erik McGovern
 */
public class DBContacts {

    /**
     * Queries database for all contacts.
     * @return ObservableList of all contacts.
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contacts contact = new Contacts(id, name, email);
                allContacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     * Queries database for a contact with a specific contact id.
     * @param id contact id.
     * @return Contact with specific contact id.
     */
    public static Contacts getContact(int id) {
        try {
            String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                return new Contacts(id, name, email);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
