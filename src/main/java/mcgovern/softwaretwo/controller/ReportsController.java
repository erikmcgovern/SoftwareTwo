package mcgovern.softwaretwo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mcgovern.softwaretwo.DBAccess.DBAppointments;
import mcgovern.softwaretwo.DBAccess.DBContacts;
import mcgovern.softwaretwo.DBAccess.DBCountries;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Appointments;
import mcgovern.softwaretwo.model.Contacts;
import mcgovern.softwaretwo.model.Countries;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * ReportsController - defines the behavior of the reports menu for interacting with the user.
 *
 * @author Erik McGovern
 */
public class ReportsController implements Initializable {
    @FXML private RadioButton typeToggle;
    @FXML private RadioButton monthToggle;
    @FXML private RadioButton contactToggle;
    @FXML private RadioButton countryToggle;
    @FXML private TableColumn<?, ?> appointmentsIdCol;
    @FXML private TableColumn<?, ?> customerIdCol;
    @FXML private TableColumn<?, ?> descriptionCol;
    @FXML private TableColumn<?, ?> endTimeCol;
    @FXML private TableView<Appointments> reportsTableView;
    @FXML private ComboBox sortByComboBox;
    @FXML private Label sortByText;
    @FXML private TableColumn<?, ?> startTimeCol;
    @FXML private TableColumn<?, ?> titleCol;
    @FXML private Label totalCustomersText;
    @FXML private TableColumn<?, ?> typeCol;

    /**
     * Populates the sort by combo box with all contacts in database.
     * @param event Event action caused by pressing the 'Contact' radio button.
     */
    @FXML
    void onActionContact(ActionEvent event) {
        reportsTableView.setItems(null);
        totalCustomersText.setText("0");
        sortByText.setText("Sort by Contact");
        sortByComboBox.setItems(DBContacts.getAllContacts());
    }

    /**
     * Populates the sort by combo box with all countries in database.
     * @param event Event action caused by pressing the 'Country' radio button.
     */
    @FXML
    void onActionCountry(ActionEvent event) {
        reportsTableView.setItems(null);
        totalCustomersText.setText("0");
        sortByText.setText("Sort by Country");
        sortByComboBox.setItems(DBCountries.getAllCountries());
    }

    /**
     * Populates the sort by combo box with all 12 months.
     * @param event Event action caused by pressing the 'Month' radio button.
     */
    @FXML
    void onActionMonth(ActionEvent event) {
        reportsTableView.setItems(null);
        totalCustomersText.setText("0");
        sortByText.setText("Sort by Month");
        sortByComboBox.setItems(FXCollections.observableArrayList(Month.values()));
    }

    /**
     * Populates the sort by combo box with all appointment types in database.
     * @param event Event action caused by pressing the 'Type' radio button.
     */
    @FXML
    void onActionType(ActionEvent event) {
        reportsTableView.setItems(null);
        totalCustomersText.setText("0");
        sortByText.setText("Sort by Type");

        ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<String> allTypes = FXCollections.observableArrayList();

        for (Appointments appointment : allAppointments) {
            String type = appointment.getType();
            if (!allTypes.contains(type)) {
                allTypes.add(type);
            }
        }
        sortByComboBox.setItems(allTypes);
    }

    /**
     * Populates the tableview with the appropriate contents based on the user's selection and the total appointment count.
     * @param actionEvent Event action caused by selecting an option from the combo box.
     */
    @FXML
    void onActionSortByComboBox(ActionEvent actionEvent) {
        if (contactToggle.isSelected() && sortByComboBox.getValue() != null) {
            Contacts contact = (Contacts) sortByComboBox.getValue();
            reportsTableView.setItems(DBAppointments.getAppointmentsByContact(contact.getId()));
        } else if (countryToggle.isSelected() && sortByComboBox.getValue() != null) {
            Countries country = (Countries) sortByComboBox.getValue();
            reportsTableView.setItems(DBAppointments.getAppointmentsByCountry(country.getId()));
        } else if (typeToggle.isSelected() && sortByComboBox.getValue() != null) {
            String type = (String) sortByComboBox.getValue();
            reportsTableView.setItems(DBAppointments.getAppointmentsByType(type));
        } else if (monthToggle.isSelected() && sortByComboBox.getValue() != null) {
            ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();
            ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();
            for (Appointments appointment : allAppointments) {
                if (appointment.getMonth().equals(sortByComboBox.getValue())) {
                    monthlyAppointments.add(appointment);
                }
                reportsTableView.setItems(monthlyAppointments);
            }
        }
        // Set total customer number text
        if (reportsTableView.getItems() != null) {
            int customerCount = reportsTableView.getItems().size();
            totalCustomersText.setText(String.valueOf(customerCount));
        }
    }

    /**
     * After pressing the 'Back' button, the user is returned to the main screen.
     * @param event Event action caused by pressing the 'Back' button.
     * @throws IOException This exception is thrown when there is a problem loading the main menu.
     */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/MainMenu.fxml", "Main Menu");
    }

    /**
     * Populates the tableview with all appointments in the database and the total appointment count.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportsTableView.setItems(DBAppointments.getAllAppointments());

        appointmentsIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        if (reportsTableView.getItems() != null) {
            int customerCount = reportsTableView.getItems().size();
            totalCustomersText.setText(String.valueOf(customerCount));
        }
    }
}
