package mcgovern.softwaretwo.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import mcgovern.softwaretwo.DBAccess.*;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * ModifyAppointmentsController - modifies appointment objects.
 *
 * @author Erik McGovern
 */
public class ModifyAppointmentsController implements Initializable {

    /**
     * Variable used to hold the appointment passed in from the appointment menu.
     */
    private Appointments modifyAppointment;
    @FXML private TextField appointmentIdTxt;
    @FXML private TextField appointmentDescriptionTxt;
    @FXML private TextField appointmentLocationTxt;
    @FXML private TextField appointmentTitleTxt;
    @FXML private TextField appointmentTypeTxt;
    @FXML private ComboBox<Contacts> contactComboBox;
    @FXML private ComboBox<Customers> customerIdComboBox;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<Users> userIdComboBox;

    /**
     * Receives information from the appointment menu.
     * @param appointment appointment received.
     */
    public void sendAppointment(Appointments appointment) {
        modifyAppointment = appointment;

        // Appointment ID
        appointmentIdTxt.setText(String.valueOf(appointment.getAppointmentId()));
        //Appointment title
        appointmentTitleTxt.setText(appointment.getTitle());
        //Appointment description
        appointmentDescriptionTxt.setText(appointment.getDescription());
        // Appointment location
        appointmentLocationTxt.setText(appointment.getLocation());
        // Appointment type
        appointmentTypeTxt.setText(appointment.getType());
        // Appointment contact ID
        contactComboBox.setValue(DBContacts.getContact(appointment.getContactId()));
        // Appointment customer ID
        customerIdComboBox.setValue(DBCustomers.getCustomer(appointment.getCustomerId()));
        // Appointment user ID
        userIdComboBox.setValue(DBUsers.getUser(appointment.getUserId()));
        // Appointment start date
        startDatePicker.setValue(appointment.getStart().toLocalDate());
        // Appointment start time
        startTimeComboBox.setValue(appointment.getStart().toLocalTime());
        // Appointment end date
        endDatePicker.setValue(appointment.getEnd().toLocalDate());
        // Appointment end time
        endTimeComboBox.setValue(appointment.getEnd().toLocalTime());
    }

    /**
     * After pressing the 'Cancel' button, the user is returned to the appointment menu.
     * @param event Event action caused by pressing 'Cancel' button.
     * @throws IOException This exception is thrown when there is a problem loading the appointment menu.
     */
    @FXML
    void onActionAppointmentsMenu(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Appointments.fxml", "Appointments Menu");
    }

    /**
     * After pressing 'Save' button, the appointment is updated in the database.
     * If invalid values are entered, an error dialog appears.
     * @param event Event action caused by pressing 'Save' button.
     * @throws IOException This exception is thrown when there is a problem loading appointment menu.
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        int appointmentId = modifyAppointment.getAppointmentId();
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
        ObservableList<Appointments> customerAppointments = DBAppointments.getCustomerAppointments(customerId, appointmentId);

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
                Methods.errorDialog("Appointment start & end date are within an existing appointment.");
                return;
            }
        }
        int rowsAffected = DBAppointments.updateAppointment(title, description, location, type ,startDateTime, endDateTme,
                customerId, userId, contactId, appointmentId);

        if (rowsAffected > 0) {
            System.out.println("Insert Successful!");
            Methods.switchScenes(event, "view/Appointments.fxml", "Appointments Menu");
        } else {
            Methods.errorDialog("Failed to add appointment.");
        }
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
