package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.controller.LoginController;
import mcgovern.softwaretwo.model.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBUsers - defines the programs behavior when interacting with user objects in the database.
 *
 * @author Erik McGovern
 */
public class DBUsers {

    /**
     * Queries database for a user with a specific username and password.
     * @param username username.
     * @param password password.
     * @return true if username and password match any users, false if they do not match.
     */
    public static boolean validateUser(String username, String password) {
        try {
            String sql = "Select * FROM users WHERE user_name = ? AND password = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LoginController.setCurrentUser(username);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Queries database for all users.
     * @return ObservableList of all users.
     */
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> allUsers = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users user = new Users(id, userName, password);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * Queries database for user with specific user id.
     * @param id user id.
     * @return User with specific id.
     */
    public static Users getUser(int id) {
        try {
            String sql = "SELECT * FROM users WHERE User_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                return new Users(id, userName, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
