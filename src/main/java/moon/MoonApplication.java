package moon;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Creates Moon's JavaFX window and connects it to the task-management engine. */
public class MoonApplication extends Application {
    /**
     * Builds and displays Moon's main window.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if the main FXML view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MoonApplication.class.getResource("/view/MainWindow.fxml"));
        Scene scene = new Scene(loader.load());
        MainWindow controller = loader.getController();
        controller.setMoon(new MoonEngine());
        stage.setTitle("Moon");
        stage.setMinWidth(520);
        stage.setMinHeight(420);
        stage.setScene(scene);
        stage.show();
    }
}
