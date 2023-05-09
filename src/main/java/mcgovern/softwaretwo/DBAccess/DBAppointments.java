package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.controller.LoginController;
import mcgovern.softwaretwo.model.Appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DBAppointments - defines the programs behavior when interacting with appointment objects in the database.
 *
 * @author Erik McGovern
 */
public class DBAppointments {

    /**
     * Queries database for all appointments.
     * @return ObservableList of all appointments.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Adds an appointment in the database.
     * @param title appointment title.
     * @param description appointment description.
     * @param location appointment location.
     * @param type appointment type.
     * @param start appointment start.
     * @param end appointment end.
     * @param customerId customer id.
     * @param userId user id.
     * @param contactId contact id.
     * @return int of how many rows affected.
     */
    public static int addAppointment(String title, String description, String location, String type, LocalDateTime start,
                                     LocalDateTime end, int customerId, int userId, int contactId) {

        try {
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                    "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            // Created by
            ps.setString(7, LoginController.getCurrentUser());
            // Last Updated By
            ps.setString(8, LoginController.getCurrentUser());
            ps.setInt(9, customerId);
            ps.setInt(10, userId);
            ps.setInt(11, contactId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an appointment in the database.
     * @param title appointment title.
     * @param description appointment description.
     * @param location appointment location.
     * @param type appointment type.
     * @param start appointment start.
     * @param end appointment end.
     * @param customerId customer id.
     * @param userId user id.
     * @param contactId contact id.
     * @param appointmentId appointment id.
     * @return int of how many rows affected.
     */
    public static int updateAppointment(String title, String description, String location, String type, LocalDateTime start,
                                        LocalDateTime end, int customerId, int userId, int contactId, int appointmentId) {
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = now(), " +
                    "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setString(7, LoginController.getCurrentUser());
            ps.setInt(8, customerId);
            ps.setInt(9, userId);
            ps.setInt(10, contactId);
            ps.setInt(11, appointmentId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete an appointment in the database with a specific appointment id.
     * @param id appointment id.
     * @return int of how many rows affected.
     */
    public static int deleteAppointment(int id) {
        try {
            String sql = "DELETE FROM appointments where Appointment_ID = ?";
            PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            Methods.errorDialog("Failed to delete appointment.");
        }
        return 0;
    }

    /**
     * Delete appointments in the database with a specific customer id.
     * @param id customer id.
     * @return int of how many rows affected.
     */
    public static int deleteCustomerAppointments(int id) {
        try {
            String sql = "DELETE FROM appointments where Customer_ID = ?";
            PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            Methods.errorDialog("Failed to delete appointment.");
        }
        return 0;
    }

    /**
     * Queries database for appointments with a specific customer id.
     * @param id customer id.
     * @return ObservableList of appointments with a specific customer id.
     */
    public static ObservableList<Appointments> getCustomerAppointments(int id) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Queries database for appointments with a specific customer id and appointment id.
     * @param cId customer id.
     * @param aId appointment id.
     * @return ObservableList of appointments with a specific customer id and appointment id.
     */
    public static ObservableList<Appointments> getCustomerAppointments(int cId, int aId) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ? AND Appointment_ID != ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, cId);
            ps.setInt(2, aId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Queries database for appointments with a specific contact id.
     * @param cId contact id.
     * @return ObservableList of appointments with a specific contact id.
     */
    public static ObservableList<Appointments> getAppointmentsByContact(int cId) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, cId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Queries database for appointments with a specific country id.
     * @param countryId country id.
     * @return ObservableList of appointments with a specific country id.
     */
    public static ObservableList<Appointments> getAppointmentsByCountry(int countryId) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * " +
                    "FROM ((appointments " +
                    "INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID)" +
                    "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID)" +
                    "WHERE Country_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return aList;
    }

    /**
     * Queries database for appointments with a specific type.
     * @param t appointment type
     * @return ObservableList of appointments with a specific type.
     */
    public static ObservableList<Appointments> getAppointmentsByType(String t) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE Type = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, t);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

    /**
     * Queries database for appointments with a specific username.
     * @param name username.
     * @return ObservableList of appointments with a specific username.
     */
    public static ObservableList<Appointments> getAppointmentsByUser(String name) {
        ObservableList<Appointments> aList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * " +
                    "FROM (appointments " +
                    "INNER JOIN users ON appointments.User_ID = users.User_ID)" +
                    "WHERE User_Name = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                aList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aList;
    }

}
