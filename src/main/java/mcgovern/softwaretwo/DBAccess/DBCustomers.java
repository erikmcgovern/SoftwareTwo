package mcgovern.softwaretwo.DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcgovern.softwaretwo.Utility.DBConnection;
import mcgovern.softwaretwo.controller.LoginController;
import mcgovern.softwaretwo.model.Customers;
import java.sql.*;

/**
 * DBCustomers - defines the programs behavior when interacting with customer objects in the database.
 *
 * @author Erik McGovern
 */
public class DBCustomers {

    public static int addCustomer(String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES(?, ?, ?, ?, now(), ?, now(), ?, ?)";

        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        // Created_By
        ps.setString(5, LoginController.getCurrentUser());
        // Last_Updated_By
        ps.setString(6, LoginController.getCurrentUser());
        // Division ID
        ps.setInt(7, divisionId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Updates a customer in the database.
     * @param name customer name.
     * @param address customer address.
     * @param postalCode customer postal code.
     * @param phone customer phone number.
     * @param divisionId customer division id.
     * @param id customer id.
     * @return int of how many rows affected.
     * @throws SQLException Exception is thrown if there is a problem connecting to the database.
     */
    public static int updateCustomer(String name, String address, String postalCode, String phone, int divisionId, int id) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = now(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, LoginController.getCurrentUser());
        ps.setInt(6, divisionId);
        ps.setInt(7, id);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Delete a customer in the database with a specific customer id.
     * @param id customer id.
     * @return
     * @throws SQLException This exception is thrown when there is a problem connecting to the database.
     */
    public static int deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customers where Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Queries database for all customers.
     * @return ObservableList of all customers.
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customers customer = new Customers(id, name, address, postalCode, phoneNumber, divisionId);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    /**
     * Queries database for a customer with a specific customer id.
     * @param id customer id.
     * @return customer with a specific customer id.
     */
    public static Customers getCustomer(int id) {
        try {
            String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                return new Customers(id, name, address, postalCode, phoneNumber, divisionId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
