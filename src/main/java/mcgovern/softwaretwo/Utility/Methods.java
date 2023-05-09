package mcgovern.softwaretwo.Utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mcgovern.softwaretwo.HelloApplication;

import java.io.IOException;
import java.time.*;

public class Methods {

    /**
     * List of available appointment start times.
     */
    private static ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();

    /**
     * List of available appointment end times.
     */
    private static ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();

    /**
     * Switches scenes.
     * @param event Action event clicked to switch screens.
     * @param file Page which the user will be sent to.
     * @param title Title of the stage being switched to.
     * @throws IOException This exception is thrown when there is a problem loading the file passed in.
     */
    public static void switchScenes (ActionEvent event, String file, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(file));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates an error dialog.
     * @param message Message the dialog will show.
     */
    public static void errorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates an information dialog.
     * @param message Message the dialog will show.
     */
    public static void messageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a list of times for available start and end times of appointments.
     */
    public static void createTimeLists() {
        ZonedDateTime estStartTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), ZoneId.of("America/New_York"));
        LocalDateTime startLocal = estStartTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocal = startLocal.plusHours(14);

        while (startLocal.isBefore(endLocal)) {
            startTimes.add(startLocal.toLocalTime());
            startLocal = startLocal.plusMinutes(30);
            endTimes.add(startLocal.toLocalTime());
        }
    }

    /**
     * Getter for appointment available start times.
     * @return ObservableList of start times.
     */
    public static ObservableList<LocalTime> getStartTimes() {
        if (startTimes.isEmpty()) {
            createTimeLists();
        }
        return startTimes;
    }

    /**
     * Getter for appointment available end times.
     * @return ObservableList of end times.
     */
    public static ObservableList<LocalTime> getEndTimes() {
        if (endTimes.isEmpty()) {
            createTimeLists();
        }
        return endTimes;
    }
}
