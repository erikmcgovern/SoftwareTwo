package mcgovern.softwaretwo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mcgovern.softwaretwo.DBAccess.DBAppointments;
import mcgovern.softwaretwo.HelloApplication;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Appointments;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AppointmentsController - defines the behavior of the appointments menu for interacting with the user.
 *
 * @author Erik McGovern
 */
public class AppointmentsController implements Initializable {

    @FXML private TableColumn<?, ?> appointmentsContactCol;
    @FXML private TableColumn<?, ?> appointmentsCustomerIdCol;
    @FXML private TableColumn<?, ?> appointmentsDescriptionCol;
    @FXML private TableColumn<?, ?> appointmentsEndTimeCol;
    @FXML private TableColumn<?, ?> appointmentsIdCol;
    @FXML private TableColumn<?, ?> appointmentsLocationCol;
    @FXML private TableColumn<?, ?> appointmentsStartTimeCol;
    @FXML private TableView<Appointments> appointmentsTableView;
    @FXML private TableColumn<?, ?> appointmentsTitleCol;
    @FXML private TableColumn<?, ?> appointmentsTypeCol;
    @FXML private TableColumn<?, ?> appointmentsUserIdCol;

    /**
     * After pressing the 'Add' button, the user is sent to the add appointment menu.
     * @param event Event actions caused by pressing 'Add' button.
     * @throws IOException This exception is thrown when there is a problem loading the add appointment menu.
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/AddAppointment.fxml", "Add Appointment");
    }

    /**
     * After pressing the 'Delete' button, the selected appointment is deleted.
     * If no appointment is selected, an error dialog appears.
     * @param event Event actions caused by pressing 'Delete' button.
     * @throws IOException This exception is thrown when there is a problem loading the appointment menu.
     */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws IOException {
        Appointments appointments = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointments != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DBAppointments.deleteAppointment(appointments.getAppointmentId());
                appointmentsTableView.setItems(DBAppointments.getAllAppointments());
                Methods.messageDialog("The following appointment has been deleted:" +
                        "\n\nAppointment ID: " + appointments.getAppointmentId() +
                        "\nAppointment Type: " + appointments.getType());
            }
        } else {
            Methods.errorDialog("No appointment has been selected.");
        }
    }

    /**
     * After pressing the 'Modify' button, the user and appointment is sent to the modify appointment menu.
     * If no appointment is selected, an error dialog appears.
     * @param event Event actions caused by pressing 'Modify' button.
     * @throws IOException This exception is thrown when there is a problem loading the modify appointment menu.
     */
    @FXML
    void onActionModifyAppointment(ActionEvent event) throws IOException {
        Appointments appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointment != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("view/ModifyAppointments.fxml"));
            loader.load();

            ModifyAppointmentsController mAController = loader.getController();
            mAController.sendAppointment(appointment);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Methods.errorDialog("No item has been selected.");
        }
    }

    /**
     * After pressing the 'All' radio button, the table view is populated with all appointments in the database.
     * @param actionEvent Event action is caused by pressing the 'All' radio button.
     */
    public void onActionToggleAll(ActionEvent actionEvent) {
        appointmentsTableView.setItems(DBAppointments.getAllAppointments());
    }

    /**
     * After pressing the 'Month' radio button, the table view is populated with appointments in the database that have
     * a start date in the next 30 days.
     * @lambda Filters appointments that are in the next 30 days.
     * @param actionEvent Event action is caused by pressing the 'Month' radio button.
     */
    public void onActionToggleMonth(ActionEvent actionEvent) {
        ObservableList<Appointments> allAppointmentsList = DBAppointments.getAllAppointments();
        ObservableList<Appointments> monthlyAppointmentsList = FXCollections.observableArrayList();

        LocalDateTime currentMonthStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        LocalDateTime currentMonthEnd = currentMonthStart.plusDays(30);

        // Replaced with lambda
//        for (Appointments appointment : allAppointmentsList) {
//            if (appointment.getStart().isAfter(currentMonthStart) && appointment.getEnd().isBefore(currentMonthEnd)) {
//                monthlyAppointmentsList.add(appointment);
//            }
//        }

        /**
         * @lambda Filters appointments that are in the next 30 days.
         */
        allAppointmentsList.stream()
                .filter(appointments -> {
                    return appointments.getStart().isAfter(currentMonthStart);
                }).filter(appointments -> {
                    return appointments.getEnd().isBefore(currentMonthEnd);
                }).forEach(appointments -> monthlyAppointmentsList.add(appointments));

        appointmentsTableView.setItems(monthlyAppointmentsList);
    }

    /**
     * After pressing the 'Week' radio button, the table view is populated with appointments in the database that have
     * a start date in the next week.
     * @param actionEvent Event action is caused by pressing the 'Week' radio button.
     */
    public void onActionToggleWeek(ActionEvent actionEvent) {
        ObservableList<Appointments> allAppointmentsList = DBAppointments.getAllAppointments();
        ObservableList<Appointments> weeklyAppointmentsList = FXCollections.observableArrayList();

        LocalDateTime currentWeekStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        LocalDateTime currentWeekEnd = currentWeekStart.plusWeeks(1);

        for (Appointments appointment : allAppointmentsList) {
            if (appointment.getStart().isAfter(currentWeekStart) && appointment.getEnd().isBefore(currentWeekEnd)) {
                weeklyAppointmentsList.add(appointment);
            }
        }
        appointmentsTableView.setItems(weeklyAppointmentsList);
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
     * Populates the tableview with all appointments in the database.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsTableView.setItems(DBAppointments.getAllAppointments());

        appointmentsIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentsTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentsEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentsUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

}
