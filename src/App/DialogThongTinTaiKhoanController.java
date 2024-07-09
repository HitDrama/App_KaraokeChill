/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package App;

import helpers.ShareData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class DialogThongTinTaiKhoanController implements Initializable {

    @FXML
    private Button btnDong;

    @FXML
    private TextField lblGioiTinh;

    @FXML
    private TextField lblMaNV;

    @FXML
    private TextField lblTenDangNhap;

    @FXML
    private TextField lblTenNV;

    @FXML
    private TextField lblVaiTro;

    @FXML
    void nutDong(ActionEvent event) {
        btnDong.getScene().getWindow().hide();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       processLoginSuccessfull();
    }

    // Method to populate UI components with login information
    private void processLoginSuccessfull() {
        lblTenDangNhap.setText(ShareData.taiKhoanDangNhap.getTenDangNhap());
        lblVaiTro.setText(ShareData.taiKhoanDangNhap.getVaiTro());
        lblMaNV.setText(ShareData.taiKhoanDangNhap.getNhanVien().getMaNhanVien());
        lblTenNV.setText(ShareData.taiKhoanDangNhap.getNhanVien().getHoTen());
        lblGioiTinh.setText(ShareData.taiKhoanDangNhap.getNhanVien().getGioiTinh() == 1 ? "Nam" : "Nữ");
    }
}
