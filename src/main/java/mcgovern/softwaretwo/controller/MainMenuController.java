package mcgovern.softwaretwo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import mcgovern.softwaretwo.Utility.Methods;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainMenuController - defines the behavior of the main menu for interacting with the user.
 *
 * @author Erik McGovern
 */
public class MainMenuController {


    /**
     * After pressing the 'Log Out' button, the user is sent to the login menu.
     * @param event Event action caused by pressing the 'Log Out' button.
     * @throws IOException This exception is thrown when there is a problem loading the login menu.
     */
    @FXML
    void onActionLogOut(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Login.fxml", "Login Menu");
        LoginController.setCurrentUser(null);
    }

    /**
     * After pressing the 'View Appointments' button, the user is sent to the appointment menu.
     * @param event Event action caused by pressing the 'View Appointments' button.
     * @throws IOException This exception is thrown when there is a problem loading the appointment menu.
     */
    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Appointments.fxml", "Appointments Menu");
    }

    /**
     * After pressing the 'View Customers' button, the user is sent to the customer menu.
     * @param event Event action caused by pressing the 'View Customers' button.
     * @throws IOException This exception is thrown when there is a problem loading the customer menu.
     */
    @FXML
    void onActionViewCustomers(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Customers.fxml", "Customers Menu");
    }

    /**
     * After pressing the 'View Reports' button, the user is sent to the report menu.
     * @param event Event action caused by pressing the 'View Reports' button.
     * @throws IOException This exception is thrown when there is a problem loading the report menu.
     */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        Methods.switchScenes(event, "view/Reports.fxml", "Reports Menu");
    }

}
