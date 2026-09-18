package moon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Displays one user or Moon message in the conversation. */
public class DialogBox extends HBox {
    @FXML
    private Label messageLabel;

    private final String message;
    private final boolean userMessage;

    /** Creates a dialog message with its speaker direction. */
    public DialogBox(String message, boolean userMessage) {
        this.message = message;
        this.userMessage = userMessage;
    }

    /** Initializes the message label after the dialog FXML has been loaded. */
    @FXML
    private void initialize() {
        assert messageLabel != null : "DialogBox.fxml must inject the message label.";
        messageLabel.setText(message);
        String backgroundColor = userMessage ? "#dbeafe" : "#ffffff";
        messageLabel.setStyle("-fx-background-color: " + backgroundColor
                + "; -fx-background-radius: 10; -fx-padding: 10;");
    }
}
