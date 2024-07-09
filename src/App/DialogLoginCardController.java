package App;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogLoginCardController implements Initializable {
    
    private Window window;

    @FXML
    private Button btnLoginCard;

    @FXML
    private TextField txtnumber;
    
    @FXML
    private Button btnquaylai;

    @FXML
    void onLoginAction(ActionEvent event) {
        loginWithCardNumber();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginWithCardNumber();
        }
    }

    private void loginWithCardNumber() {
        StringBuilder sb = new StringBuilder();
        DataValidator.validateEmpty(txtnumber, sb, "Card number cannot be empty!");
        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(null, "Error", sb.toString());
            return;
        }

        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        TaiKhoan taiKhoan = taiKhoanDAO.checkLoginByCardNumber(txtnumber.getText().trim());
        if (taiKhoan != null) {
            ShareData.taiKhoanDangNhap = taiKhoan;
            try {
                Stage stage = (Stage) btnLoginCard.getScene().getWindow();
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
                MessageDialogHelpers.showErrorDialog(null, "Error", "Unable to open mainframe");
            }
        } else {
            MessageDialogHelpers.showErrorDialog(null, "Error", "Wrong card number");
            txtnumber.requestFocus();
            txtnumber.selectAll();
        }
    }
    
    @FXML
    void btnquaylai(ActionEvent event) {
    try {
            Stage stage = (Stage) btnquaylai.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chay/DialogDangNhap.fxml"));
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage(); // Khởi tạo một Stage mới
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.setTitle("MainFrame");
            mainStage.setScene(new Scene(root));
            mainStage.show();
           
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(window, "Error", "Unable to open DialogDangNhap");
        }
    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Không cần thiết lập listener ở đây nữa vì đã thiết lập trong FXML
    }
}
