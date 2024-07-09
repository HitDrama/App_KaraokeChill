
package App;

import dao.TaiKhoanDAO;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


public class DialogQuenMatKhauController implements Initializable {
     @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnTim;

    @FXML
    private Button btnTroLai;

    @FXML
    private TextField txtCauHoi;

    @FXML
    private TextField txtCauTraLoi;

    @FXML
    private TextField txtMatKhauMoi;

    @FXML
    private TextField txtTenDN;
    private Window mainFarme;

    @FXML
    void btnLamMoi(ActionEvent event) {
        lamRongText();
    }

    @FXML
    void btnLuu(ActionEvent event) {
        String tenDN = txtTenDN.getText();
			String matKhauMoi = txtMatKhauMoi.getText();
			String cauTraLoi = txtCauTraLoi.getText();

			TaiKhoanDAO tkDao = new TaiKhoanDAO();

			// validate
			StringBuilder sb = new StringBuilder();
			validateAll(sb);
			if (sb.length() > 0) {
				MessageDialogHelpers.showMessageDialog(mainFarme, "Remind", sb.toString());
				return;
			}

			if (tkDao.updateMatKhauTheoTenVaTraLoi(tenDN, cauTraLoi, matKhauMoi)) {
				MessageDialogHelpers.showMessageDialog(mainFarme, "Notification",
						"New password updated successfully");
				lamRongText();
			} else {
				MessageDialogHelpers.showErrorDialog(mainFarme, " Error ", "Update failed");
			}

    }

    @FXML
    void btnTim(ActionEvent event) {
        String tenDN = txtTenDN.getText();
			TaiKhoanDAO tkDao = new TaiKhoanDAO();
			String cauHoi = tkDao.getCauHoiTheoTen(tenDN);

			// validate
			StringBuilder sb = new StringBuilder();
			validateTim(sb);
			if (sb.length() > 0) {
				MessageDialogHelpers.showMessageDialog(mainFarme, "Remind", sb.toString());
				return;
			}

			if (cauHoi != null) {
				txtCauHoi.setText(cauHoi);
				txtCauTraLoi.setEditable(true);
				txtMatKhauMoi.setEditable(true);
			} else {
				MessageDialogHelpers.showErrorDialog(mainFarme, "Error", "This username was not found");
			}
    }

    @FXML
    void btnTroLai(ActionEvent event) {
        Stage stage = (Stage) btnTroLai.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void validateTim(StringBuilder sb) {
		DataValidator.validateEmpty(txtTenDN, sb, "Username cannot be empty");
	}
    public void validateAll(StringBuilder sb) {
		DataValidator.validateMatKhau(txtMatKhauMoi, sb,
				"Password must have correct format: must have 1 uppercase letter, 1 lowercase letter, 1 special character, minimum 8 characters");
           
        
		DataValidator.validateEmpty(txtMatKhauMoi, sb, "The new password cannot be empty");
		DataValidator.validateEmpty(txtCauHoi, sb, "Questions cannot be empty");
		DataValidator.validateEmpty(txtCauTraLoi, sb, "The answer cannot be empty");            
		DataValidator.validateEmpty(txtTenDN, sb, "Username cannot be empty");
	}
    public void lamRongText() {
		txtTenDN.setText("");
		txtCauHoi.setText("");
		txtCauTraLoi.setText("");
		txtMatKhauMoi.setText("");
		txtTenDN.requestFocus();
                txtCauTraLoi.setEditable(false);
		txtMatKhauMoi.setEditable(false);
	}

    void setVisble(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
