package moon;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Controls Moon's main JavaFX window and forwards user commands to the engine. */
public class MainWindow {
    @FXML
    private VBox dialogContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private MoonEngine moon;

    /** Connects this controller to Moon's command engine. */
    public void setMoon(MoonEngine moon) {
        this.moon = moon;
        addDialog("Hello! I'm Moon, your personal chatbot.\nWhat can I do for you?", false);
        if (moon.hasLoadingError()) {
            addDialog("Oof! I couldn't load your task list. Starting with an empty list.", false);
        }
    }

    /** Sends the command currently entered in the text field. */
    @FXML
    private void handleUserInput() {
        assert moon != null : "The main window must be connected to Moon before use.";
        String command = userInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        addDialog(command, true);
        addDialog(moon.getResponse(command).strip(), false);
        userInput.clear();
        userInput.requestFocus();
    }

    private void addDialog(String message, boolean isUserMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            DialogBox dialogBox = new DialogBox(message, isUserMessage);
            loader.setRoot(dialogBox);
            loader.setController(dialogBox);
            dialogContainer.getChildren().add(loader.load());
            scrollPane.setVvalue(1.0);
            dialogBox.setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        } catch (IOException exception) {
            addFallbackDialog(message);
        }
    }

    private void addFallbackDialog(String message) {
        DialogBox dialogBox = new DialogBox(message, false);
        dialogContainer.getChildren().add(dialogBox);
    }
}
