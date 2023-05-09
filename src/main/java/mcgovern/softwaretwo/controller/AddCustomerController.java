package mcgovern.softwaretwo.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mcgovern.softwaretwo.DBAccess.DBCountries;
import mcgovern.softwaretwo.DBAccess.DBCustomers;
import mcgovern.softwaretwo.DBAccess.DBDivisions;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Countries;
import mcgovern.softwaretwo.model.Divisions;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * AddCustomerController - adds customer to database.
 *
 * @author Erik McGovern
 */
public class AddCustomerController implements Initializable {

    @FXML private TextField customerAddressTxt;
    @FXML private ComboBox<Countries> customerCountryComboBox;
    @FXML private TextField customerNameTxt;
    @FXML private TextField customerPhoneTxt;
    @FXML private TextField customerPostalCodeTxt;
    @FXML private ComboBox<Divisions> customerStateComboBox;

    /**
     * After pressing the 'Cancel' button, the user is returned to the customer menu.
     * @param event Event action caused by pressing the 'Cancel' button.
     * @throws IOException This exception is thrown when there is a problem loading the customer menu.
     */
    @FXML
    void onActionCustomerMenu(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Customers.fxml", "Customers Menu");
    }

    /**
     * After selecting a country, the state/province combo box is filled with the related division objects.
     * @param actionEvent Event action caused by selecting an option in the country combo box.
     */
    public void onActionCountry(ActionEvent actionEvent) {
        customerStateComboBox.setDisable(false);
        Countries country = customerCountryComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Divisions> divisionsList = DBDivisions.getDivisionsByCountry(country.getId());
        customerStateComboBox.setItems(divisionsList);
    }

    /**
     * After pressing the 'Save' button, the customer is added to the database.
     * If invalid values are entered, an error dialog appears.
     * @param event Event action caused by pressing the 'Save' button.
     * @throws IOException This exception is thrown when there is a problem loading the customer menu.
     * @throws SQLException This exception is thrown when there is a problem communicating with the database.
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException {
        String name = customerNameTxt.getText();
        String address = customerAddressTxt.getText();
        String postalCode = customerPostalCodeTxt.getText();
        String phoneNumber = customerPhoneTxt.getText();
        Divisions division = customerStateComboBox.getValue();

        if (division == null) {
            Methods.errorDialog("Please enter a value in the missing fields.");
            return;
        }
        int divisionId = division.getDivisionId();

        if (name.isBlank() || address.isBlank() || postalCode.isBlank() || phoneNumber.isBlank()) {
            Methods.errorDialog("Please enter a value in the missing fields.");
            return;
        }
        try {
            int rowsAffected = DBCustomers.addCustomer(name, address, postalCode, phoneNumber, divisionId);

            if (rowsAffected > 0) {
                Methods.messageDialog("Customer created!");
                Methods.switchScenes(event, "view/Customers.fxml", "Customers Menu");
            } else {
                Methods.errorDialog("Failed to add customer.");
            }
        } catch (SQLException e) {
            Methods.errorDialog("Failed to add customer.");
        } catch (IOException e) {
            Methods.errorDialog("Unable to return to Customers Menu.");
        }
    }

    /**
     * Populates the country combo box and disables the state/province combo box.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> allCountries = DBCountries.getAllCountries();
        customerCountryComboBox.setItems(allCountries);
        customerStateComboBox.setDisable(true);
    }

}
