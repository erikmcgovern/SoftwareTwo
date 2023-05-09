package mcgovern.softwaretwo.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import mcgovern.softwaretwo.DBAccess.DBAppointments;
import mcgovern.softwaretwo.DBAccess.DBContacts;
import mcgovern.softwaretwo.DBAccess.DBCustomers;
import mcgovern.softwaretwo.DBAccess.DBUsers;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Appointments;
import mcgovern.softwaretwo.model.Contacts;
import mcgovern.softwaretwo.model.Customers;
import mcgovern.softwaretwo.model.Users;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

/**
 * AddAppointmentController - adds appointment to database.
 *
 * @author Erik McGovern
 */
public class AddAppointmentController implements Initializable {

    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Customers> customerIdComboBox;
    @FXML private ComboBox<Users> userIdComboBox;
    @FXML private ComboBox<Contacts> contactComboBox;
    @FXML private TextField appointmentDescriptionTxt;
    @FXML private TextField appointmentLocationTxt;
    @FXML private TextField appointmentTitleTxt;
    @FXML private TextField appointmentTypeTxt;

    /**
     * After pressing 'Save' button, the appointment is saved to the database.
     * If invalid values are entered, an error dialog appears.
     * @param event Event action caused by pressing 'Save' button.
     * @throws IOException This exception is thrown when there is a problem loading appointment menu.
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        String title = appointmentTitleTxt.getText();
        String description = appointmentDescriptionTxt.getText();
        String location = appointmentLocationTxt.getText();
        String type = appointmentTypeTxt.getText();
        Customers customer = customerIdComboBox.getValue();
        Users user = userIdComboBox.getValue();
        Contacts contact = contactComboBox.getValue();

        if (customer == null || user == null || contact == null) {
            Methods.errorDialog("Please select an option in the combo boxes.");
            return;
        }
        int customerId = customer.getId();
        int userId = user.getUserId();
        int contactId = contact.getId();

        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();

        if (startDate == null || startTime == null) {
            Methods.errorDialog("Please enter a valid value in start date/time.");
            return;
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getValue();
        if (endDate == null || endTime == null) {
            Methods.errorDialog("Please enter a valid value in the end date/time");
            return;
        }
        LocalDateTime endDateTme = LocalDateTime.of(endDate, endTime);

        if (title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()) {
            Methods.errorDialog("Please enter a value in the missing fields.");
            return;
        }
        if (startDateTime.plusMinutes(1).isAfter(endDateTme)){
            Methods.errorDialog("End date/time has to be after start date/time.");
            return;
        }
        ObservableList<Appointments> customerAppointments = DBAppointments.getCustomerAppointments(customerId);

        for (Appointments appointment : customerAppointments) {
            if (appointment.getStart().isBefore(startDateTime) && appointment.getEnd().isAfter(startDateTime)) {
                Methods.errorDialog("Appointment start date is within an existing appointment.");
                return;
            }
            else if (appointment.getStart().isBefore(endDateTme) && appointment.getEnd().isAfter(endDateTme)) {
                Methods.errorDialog("Appointment end date is within an existing appointment.");
                return;
            }
            else if (appointment.getStart().isBefore(endDateTme) && appointment.getEnd().isAfter(startDateTime)) {
                Methods.errorDialog("An existing appointment is within the start & end date.");
                return;
            }
        }
        int rowsAffected = DBAppointments.addAppointment(title, description, location, type ,startDateTime, endDateTme,
                customerId, userId, contactId);

        if (rowsAffected > 0) {
            Methods.messageDialog("Appointment created!");
            Methods.switchScenes(event, "view/Appointments.fxml", "Appointments Menu");
        } else {
            Methods.errorDialog("Failed to add appointment.");
        }
    }

    /**
     * After pressing the 'Cancel' button, the user is returned to the appointment menu.
     * @param event Event action caused by pressing 'Cancel' button.
     * @throws IOException This exception is thrown when there is a problem loading the appointment menu.
     */
    public void onActionAppointmentsMenu(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Appointments.fxml", "Appointments Menu");
    }

    /**
     * Populates the five combo boxes.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // All contacts added to Contact Combo Box
        contactComboBox.setItems(DBContacts.getAllContacts());
        // All customers added to Customer Combo Box
        customerIdComboBox.setItems(DBCustomers.getAllCustomers());
        // All users added to User Combo Box
        userIdComboBox.setItems(DBUsers.getAllUsers());

        // Time options added to Time Combo Boxes
        startTimeComboBox.setItems(Methods.getStartTimes());
        endTimeComboBox.setItems(Methods.getEndTimes());
    }

}
