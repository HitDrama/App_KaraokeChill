/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package App;

import dao.KhachHangDAO;
import entity.KhachHang;
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

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class DialogThemKhachHangController implements Initializable {

    @FXML
    private Button btnDong;

    @FXML
    private Button btnThem;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

    private Window mainFrame;
    @FXML
    void nutDong(ActionEvent event) {
        btnDong.getScene().getWindow().hide();
    }

    @FXML
    void nutThem(ActionEvent event) {
        Stage currentStage = (Stage) btnThem.getScene().getWindow();
        currentStage.hide();

        // Create a KhachHang object
        KhachHang khachHang = createKhachHang();

        // Validate input data
        StringBuilder sb = new StringBuilder();
        dataValidateKhachHang(sb); // Assuming this method validates other fields

        // Validate phone number (txtSDT)
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            sb.append("Số điện thoại không được để trống.\n");
        } else if (!isValidPhoneNumber(sdt)) {
            sb.append("Số điện thoại không hợp lệ.\n");
        }

        // Check if there are validation errors
        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(currentStage, "Lỗi", sb.toString());
            return;
        }

        // Check if the phone number already exists
        KhachHangDAO khachHangDAO = new KhachHangDAO(); // Instantiate your DAO class
        if (khachHangDAO.checkExist(sdt)) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Cảnh báo", "Khách hàng đã tồn tại, số điện thoại trùng");
            return;
        }

        // Set phone number to khachHang object
        khachHang.setSoDienThoai(sdt);

        // Attempt to add the customer
        if (khachHangDAO.addKhachHang(khachHang)) {
            MessageDialogHelpers.showMessageDialog(mainFrame, "Thông báo", "Khách hàng đã thêm thành công");
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Thêm không thành công");
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\s", "");
        String regex = "(0\\d{9})|(\\+84\\d{9})";
        return phoneNumber.matches(regex);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    private void dataValidateKhachHang(StringBuilder sb) {
	DataValidator.validateEmpty(txtSDT, sb, "Số điện thoại không được để trống");
	DataValidator.validateEmpty(txtTenKH, sb, "Tên khách hàng không được để trống");
	DataValidator.validateVietnameseCharacters(txtTenKH, sb,"Tên khách hàng sai.Không được nhập số và kí tự đặt biệt");
	DataValidator.validateSoDT(txtSDT, sb,"Số điện thoại sai định dạng, phải có từ 9-10 chữ số, không có kí tự. Ví dụ:0788775877");
	DataValidator.validateSoDT(txtSDT, sb, "Số điện thoại đã tồn tại");
    }
    private KhachHang createKhachHang() {
	KhachHang khachHang = new KhachHang();
	khachHang.setHoTenKH(txtTenKH.getText());
	khachHang.setSoDienThoai(txtSDT.getText());
	return khachHang;
    }
}
