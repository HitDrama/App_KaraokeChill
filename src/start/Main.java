package start; 

import chay.DialogDangNhapController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chay/DialogDangNhap.fxml"));
            Parent root = loader.load();
            
            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            
            // Get the controller
   DialogDangNhapController controller = loader.getController();
            
            // Optionally, do something with the controller before showing the stage
            
            // Show the stage
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
