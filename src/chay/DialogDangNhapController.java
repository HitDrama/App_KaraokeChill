
package chay;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import helpers.ShareData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class DialogDangNhapController implements Initializable {

    @FXML
    private Button btnDangNhap;
    @FXML
    private Button btncardID;
    @FXML
    private Button btnThoat;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    private Window mainFrame;
    private TaiKhoanDAO taiKhoanDAO;
    
    @FXML
    private Label lblQuenMatKhau;
    @FXML
    void btnDangNhap(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
			DataValidator.validateEmpty(txtPassword, sb, "Password can not be blank!");
			DataValidator.validateEmpty(txtUsername, sb, "Username cannot be empty!");
			if (sb.length() > 0) {
				MessageDialogHelpers.showErrorDialog(mainFrame, "Error", sb.toString());
				return;
			}

			taiKhoanDAO = new TaiKhoanDAO();
			TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(txtUsername.getText(), txtPassword.getText());
			if (taiKhoan != null) {
				ShareData.taiKhoanDangNhap = taiKhoan;					
        try {
            Stage stage = (Stage) btnDangNhap.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/InitPreloader.fxml"));
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage(); // Khởi tạo một Stage mới
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.setTitle("MainFrame");
            mainStage.setScene(new Scene(root));
            mainStage.show();
           
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Unable to open mainframe");
        }
			} else {
				MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Wrong login name or password");
				txtUsername.requestFocus();
				txtUsername.selectAll();
			}
    }

    @FXML
    void btnThoat(ActionEvent event) {
           Stage stage = (Stage) btnThoat.getScene().getWindow();
           stage.close();
    }
    @FXML
    void lblQuenMatKhau(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/DialogQuenMatKhau.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Forgot password");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Cannot open the forgot password window.");
        }
    }
      @FXML
    void btncardID(ActionEvent event) {
        try {
            Stage stage = (Stage) btnDangNhap.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/DialogLoginCard.fxml"));
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage(); // Khởi tạo một Stage mới
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.setTitle("MainFrame");
            mainStage.setScene(new Scene(root));
            mainStage.show();
           
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Unable to open mainframe");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
