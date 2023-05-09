package mcgovern.softwaretwo.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mcgovern.softwaretwo.DBAccess.DBAppointments;
import mcgovern.softwaretwo.DBAccess.DBUsers;
import mcgovern.softwaretwo.Utility.Methods;
import mcgovern.softwaretwo.model.Appointments;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LoginController - defines the behavior of the login menu for interacting with the user.
 *
 * @author Erik McGovern
 */
public class LoginController implements Initializable {

    /**
     * Variable used to hold the current username logged in.
     */
    private static String currentUser;
    @FXML private Label timeZoneTxt;
    @FXML private Label usernameTxt;
    @FXML private Label passwordTxt;
    @FXML private Button loginButton;
    @FXML private Label loginTxt;
    @FXML private TextField passwordText;
    @FXML private Label timeZoneLabel;
    @FXML private TextField usernameText;

    /**
     * After pressing the 'Login' button, the application checks if the user entered a valid username and password from
     * the database. If valid, the user is sent to the main menu and a dialog appears letting the user know if they have
     * an appointment in the next 15 minutes or not. If invalid, an error message appears.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();

        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        if (DBUsers.validateUser(username, password)) {
            Methods.switchScenes(actionEvent, "view/MainMenu.fxml", "Main Menu");
            // Prints success to login_attempt.txt
            printWriter.println("Username: '" + username + "' successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

            // Checks if there is an appointment in next 15 minutes
            ObservableList<Appointments> appointmentList = DBAppointments.getAppointmentsByUser(username);
            for (Appointments appointment : appointmentList) {
                if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15))) {
                    Methods.messageDialog("There is an appointment within the next 15 minutes!\n\n" +
                            "Appointment ID: " + appointment.getAppointmentId() + "\nDate: " + appointment.getStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            "\nTime: " + appointment.getStart().format(DateTimeFormatter.ofPattern("HH:mm")));
                    return;
                }
            }
            Methods.messageDialog("There are no appointments in the next 15 minutes.");
        } else {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Methods.errorDialog("Nom d'utilisateur ou mot de passe invalide.");
            } else {
                // Prints failed to login_attempt.txt
                printWriter.println("Username: '" + username + "' failed to log in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                Methods.errorDialog("Invalid username or password.");
            }
        }
        fileWriter.close();
        printWriter.close();
    }

    /**
     * currentUser getter.
     * @return String currentUser.
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * currentUser setter.
     * @param currentUser String currentUser.
     */
    public static void setCurrentUser(String currentUser) {
        LoginController.currentUser = currentUser;
    }

    /**
     * Populates the time zone label and sets texts based on computers language default.
     * @param url Pointer.
     * @param resourceBundle Enables application to load data from distinct files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        timeZoneLabel.setText(String.valueOf(zone));

        ResourceBundle rb = ResourceBundle.getBundle("/mcgovern/softwaretwo/Nat", Locale.getDefault());

        loginTxt.setText(rb.getString("Login"));
        loginButton.setText(rb.getString("Login"));
        usernameTxt.setText(rb.getString("Username"));
        passwordTxt.setText(rb.getString("Password"));
        timeZoneTxt.setText(rb.getString("Location"));
    }

}
