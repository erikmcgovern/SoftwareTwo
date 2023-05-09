package mcgovern.softwaretwo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mcgovern.softwaretwo.Utility.DBConnection;
import java.io.IOException;

/**
 * Main class that creates the scheduling application.
 *
 * @author Erik McGovern
 */
public class HelloApplication extends Application {

    /**
     * Initializes the Login.fxml.
     * @param stage Stage variable.
     * @throws IOException This exception is thrown when there is a problem loading login menu.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application.
     * @param args Stores arguments passed by command line.
     */
    public static void main(String[] args) {
        DBConnection.openConnection();
        launch();
        DBConnection.closeConnection();
    }

}