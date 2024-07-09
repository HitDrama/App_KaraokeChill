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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.JOptionPane;

public class DialogDoiMatKhauController implements  Initializable{

    @FXML
    private Button btnDong;

    @FXML
    private Button btnXacNhan;

    @FXML
    private TextField txtMatKhauMoi;

    @FXML
    private TextField txtNhapLaiMK;
    private Window mainFrame;

    @FXML
    void btnDong(ActionEvent event) {
        Stage stage = (Stage) btnDong.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnXacNhan(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
			dataValidate(sb);
			if (sb.length() > 0) {
				MessageDialogHelpers.showErrorDialog(mainFrame, "Error",
						sb.toString());
				return;
			}

			if (MessageDialogHelpers.showConfirmDialog(mainFrame, "Error",
					"Are you sure you want to change your password?") == JOptionPane.NO_OPTION) {
				return;
			}

			if (txtMatKhauMoi.getText().equals(txtNhapLaiMK.getText())) {
				try {
					TaiKhoan tk = ShareData.taiKhoanDangNhap;
					tk.setMatKhau(txtMatKhauMoi.getText());
					TaiKhoanDAO tkDAO = new TaiKhoanDAO();
					if (tkDAO.updateMatKhau(tk)) {
						MessageDialogHelpers.showMessageDialog(mainFrame, "Notification",
								"Password updated successfully");
						
					} else {
						MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Update failed");
                                                
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else {
				MessageDialogHelpers.showErrorDialog(mainFrame, "Error",
						"Password and re-enter password are not the same");
			}

    }
    public void initialize(URL location, ResourceBundle resources) {
            
    
    }
    private void dataValidate(StringBuilder sb) {
		DataValidator.validateEmpty(txtNhapLaiMK, sb, "Haven't re-entered the password yet");
		DataValidator.validateEmpty(txtMatKhauMoi, sb, "Password can not be blank");
		
		DataValidator.validateMatKhau(txtMatKhauMoi, sb,
				"Password must have at least 1 lowercase letter, 1 uppercase letter, 1 special character, no spaces and at least 8 characters");
	}
}
