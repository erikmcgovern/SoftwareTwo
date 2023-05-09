package mcgovern.softwaretwo.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mcgovern.softwaretwo.DBAccess.DBAppointments;
import mcgovern.softwaretwo.DBAccess.DBCustomers;
import mcgovern.softwaretwo.HelloApplication;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * CustomersController - defines the behavior of the customers menu for interacting with the user.
 *
 * @author Erik McGovern
 */
public class CustomersController implements Initializable {

    @FXML private TableColumn<Customers, String> customersAddressCol;
    @FXML private TableColumn<Customers, String> customersDivisionIdCol;
    @FXML private TableColumn<Customers, String> customersIdCol;
    @FXML private TableColumn<Customers, String> customersNameCol;
    @FXML private TableColumn<Customers, String> customersPhoneNumberCol;
    @FXML private TableColumn<Customers, String> customersPostalCodeCol;
    @FXML private TableView<Customers> customersTableView;

    /**
     * After pressing the 'Add' button, the user is sent to the add customer menu.
     * @param event Event actions caused by pressing 'Add' button.
     * @throws IOException This exception is thrown when there is a problem loading the add customer menu.
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/AddCustomer.fxml", "Add Customer");
    }

    /**
     * After pressing the 'Delete' button, the selected customer is deleted.
     * If no customer is selected, an error dialog appears.
     * @param event Event actions caused by pressing 'Delete' button.
     * @throws SQLException This exception is thrown when there is a problem connecting to the database.
     * @throws IOException This exception is thrown when there is a problem loading the customer menu.
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws SQLException, IOException {
        Customers customer = customersTableView.getSelectionModel().getSelectedItem();

        if (customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DBAppointments.deleteCustomerAppointments(customer.getId());
                DBCustomers.deleteCustomer(customer.getId());
                Methods.messageDialog("Customer deleted.");
                customersTableView.setItems(DBCustomers.getAllCustomers());
            }
        } else {
            Methods.errorDialog("No customer has been selected.");
        }
    }

    /**
     * After pressing the 'Modify' button, the user and customer is sent to the modify customer menu.
     * If no customer is selected, an error dialog appears.
     * @param event Event actions caused by pressing 'Modify' button.
     * @throws IOException This exception is thrown when there is a problem loading the modify customer menu.
     */
    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        Customers customer = customersTableView.getSelectionModel().getSelectedItem();

        if (customer != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("view/ModifyCustomer.fxml"));
            loader.load();

            ModifyCustomerController mCController = loader.getController();
            mCController.sendCustomer(customer);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Methods.errorDialog("No item has been selected.");
        }
    }

    /**
     * After pressing the 'Back' button, the user is sent to the main menu.
     * @param event Event action is caused by pressing the 'Back' button.
     * @throws IOException This exception is thrown when there is a problem loading the main menu.
     */
    public void onActionMainMenu(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/MainMenu.fxml", "Main Menu");
    }

    /**
     * Populates the tableview with all appointments in the database
     * Lambdas used to populate the customer columns.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTableView.setItems(DBCustomers.getAllCustomers());

        /**
         * @lambda Sets the cell value factory for the customer's ID column.
         */
        customersIdCol.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().getId())));

        /**
         * @lambda Sets the cell value factory for the customer's name column.
         */
        customersNameCol.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getName()));

        /**
         * @lambda Sets the cell value factory for the customer's address column.
         */
        customersAddressCol.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAddress()));

        /**
         * @lambda Sets the cell value factory for the customer's postal code column.
         */
        customersPostalCodeCol.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getPostalCode()));

        /**
         * @lambda Sets the cell value factory for the customer's phone number column.
         */
        customersPhoneNumberCol.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getPhoneNumber()));

        /**
         * @lambda Sets the cell value factory for the customer's division id column.
         */
        customersDivisionIdCol.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().getDivisionId())));
    }

}
