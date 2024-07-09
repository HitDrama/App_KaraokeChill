/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package App;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class DialogChuaCoKhachHangController implements Initializable {

    @FXML
    private Button btn_OpenQLKH;

    @FXML
    private Button cancelButton;

    @FXML
    void nutCancel(ActionEvent event) {
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    void nutOpenQLKH(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/DialogThemKhachHang.fxml"));
            Parent parent = loader.load();

            // Get the controller instance
            DialogThemKhachHangController controller = loader.getController();

            // You might need to pass data or perform initialization here

            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.show();

            // Hide the current window if needed
            btn_OpenQLKH.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
