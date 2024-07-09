package App;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitPreloaderController implements Initializable {

    public Label lblLoading;
    public static Label lblLoadingg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblLoadingg = lblLoading;
        checkFunctions();
    }

    public void checkFunctions() {
        final String[] message = {""};

        // First Function
        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            message[0] = "First Function";
            lblLoadingg.setText(message[0]);
        }));
        timeline1.setCycleCount(1);

        // Second Function
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            message[0] = "Second Function";
            lblLoadingg.setText(message[0]);
        }));
        timeline2.setCycleCount(1);

        // Open Main Stage
        Timeline timeline3 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            message[0] = "Open Main Stage";
            lblLoadingg.setText(message[0]);
            Platform.runLater(() -> {
                try {
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    // Close the splash screen
                    lblLoadingg.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }));
        timeline3.setCycleCount(1);

        // Start the timelines in sequence
        timeline1.setOnFinished(event -> timeline2.play());
        timeline2.setOnFinished(event -> timeline3.play());

        timeline1.play();
    }
}
