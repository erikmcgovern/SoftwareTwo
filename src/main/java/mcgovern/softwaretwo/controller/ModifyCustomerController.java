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
import mcgovern.softwaretwo.model.Customers;
import mcgovern.softwaretwo.model.Divisions;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * ModifyCustomerController - modifies customer objects.
 *
 * @author Erik McGovern
 */
public class ModifyCustomerController implements Initializable {

    /**
     * Variable used to hold the customer passed in from the customer menu.
     */
    private Customers modifyCustomer;
    @FXML private ComboBox<Countries> customerCountryComboBox;
    @FXML private ComboBox<Divisions> customerStateComboBox;
    @FXML private TextField customerAddressTxt;
    @FXML private TextField customerIdTxt;
    @FXML private TextField customerNameTxt;
    @FXML private TextField customerPhoneTxt;
    @FXML private TextField customerPostalCodeTxt;

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
        customerStateComboBox.setValue(null);
        Countries country = customerCountryComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Divisions> divisionsList = DBDivisions.getDivisionsByCountry(country.getId());
        customerStateComboBox.setItems(divisionsList);
    }

    /**
     * After pressing the 'Save' button, the customer is updated in the database.
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
        String phone = customerPhoneTxt.getText();
        int id = modifyCustomer.getId();
        Divisions division = customerStateComboBox.getValue();

        if (division == null) {
            Methods.errorDialog("Please enter a state/province.");
            return;
        }
        int divisionId = division.getDivisionId();

        if (name.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()) {
            Methods.errorDialog("Please enter a value in each field");
            return;
        }
        try {
            int rowsAffected = DBCustomers.updateCustomer(name, address, postalCode, phone, divisionId, id);
            if (rowsAffected > 0) {
                System.out.println("Update Successful");
                Methods.switchScenes(event, "view/Customers.fxml", "Customers Menu");
            } else {
                Methods.errorDialog("Failed to update customer.");
            }
        } catch (SQLException e) {
            Methods.errorDialog("Failed to update customer.");
        } catch (IOException e) {
            Methods.errorDialog("Unable to return to Customers Menu.");
        }
    }

    /**
     * Receives information from the customer menu.
     * @param customer customer received.
     */
    public void sendCustomer(Customers customer) {
        modifyCustomer = customer;

        int divisionId = customer.getDivisionId();
        Divisions division = DBDivisions.getDivision(divisionId);

        int countryId = division.getCountryId();
        Countries country = DBCountries.getCountry(countryId);

        ObservableList<Divisions> divisionList = DBDivisions.getDivisionsByCountry(countryId);

        customerIdTxt.setText(String.valueOf(modifyCustomer.getId()));
        customerNameTxt.setText(modifyCustomer.getName());
        customerAddressTxt.setText(modifyCustomer.getAddress());
        customerPostalCodeTxt.setText(modifyCustomer.getPostalCode());
        customerPhoneTxt.setText(modifyCustomer.getPhoneNumber());
        customerCountryComboBox.setValue(country);
        customerStateComboBox.setItems(divisionList);
        customerStateComboBox.setValue(division);
    }

    /**
     * Populates the country combo box.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> allCountries = DBCountries.getAllCountries();
        customerCountryComboBox.setItems(allCountries);
    }

}
