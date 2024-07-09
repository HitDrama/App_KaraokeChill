package App;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import helpers.MessageDialogHelpers;
import helpers.ShareData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import javafx.application.Platform;

/**
 * FXML Controller class
 *
 * @author Chi
 */
public class MainFrameController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnthoat;
    
    @FXML
    private Label lblChucVu;

    @FXML
    private Label lblImage;

    @FXML
    private Label lblTen;
    
    private Window mainFrame;
    
     @FXML
    private AnchorPane mainfrom;

    
    @FXML
    private Button btn_NhanVien;
     
    @FXML
    private Button btn_TaiKhoan;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        processLoginSuccessful();
    } 
    
    
     @FXML
    void btn_DatPhong(ActionEvent event) {
        try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlDatPhong.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
@FXML
    void btn_ThanhToan(ActionEvent event) {
       try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlThanhToan.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
  
   @FXML
    void btn_SanPham(ActionEvent event) {
        try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlQuanLySanPham.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
    @FXML
    void btn_NhanVien(ActionEvent event) {
           try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlQuanLyNhanVien.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    @FXML
    void btn_TaiKhoan(ActionEvent event) {
         try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlQuanLyTaiKhoan.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
   @FXML
    void btn_KhachHang(ActionEvent event) {
        try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlQuanLyKhachHang.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    @FXML
    void btn_LoaiPhong (ActionEvent event) {
       try {
        // Debug: Print panelLeft
        System.out.println(mainfrom);

        // Load the QuanLyPanel.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/PnlQuanLyPhong.fxml"));
        Node quanLyPanel = loader.load();

        // Clear the current content of panelLeft and set new content
        mainfrom.getChildren().clear();
        mainfrom.getChildren().add(quanLyPanel);  
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    @FXML
    private FontAwesomeIcon iconNhanVien;
    @FXML
    private FontAwesomeIcon iconaccount;

    @FXML
    private MenuButton mnTaiKhoan;
    @FXML
    private MenuItem mnDoiMatKhau;  
    
    
    @FXML
    private ImageView imgthem;
    
    @FXML
    void mnDoiMatKhau(ActionEvent event) {
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/DialogDoiMatKhau.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Doi mat khau");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Loi", "Khong the mo cua so doi mat khau.");
        }
    }
    private void processLoginSuccessful() {
        mnTaiKhoan.setText(ShareData.taiKhoanDangNhap.getTenDangNhap());

        lblTen.setText("Name: " + ShareData.taiKhoanDangNhap.getNhanVien().getHoTen());
        lblChucVu.setText("Role: " + ShareData.taiKhoanDangNhap.getVaiTro());

        byte[] imageBytes = ShareData.taiKhoanDangNhap.getNhanVien().getHinh();
        if (imageBytes != null) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            lblImage.setGraphic(imageView);
        } else {
            lblImage.setGraphic(null); // hoặc bạn có thể đặt một hình ảnh mặc định
        }

        if ("Nhan Vien Le Tan".equals(ShareData.taiKhoanDangNhap.getVaiTro())) {
            btn_NhanVien.setVisible(false);
            iconNhanVien.setVisible(false);
            btn_TaiKhoan.setVisible(false);
            iconaccount.setVisible(false);
        }
    }
    
    @FXML
    void mnGioiThieu(ActionEvent event) {
        try {
            // Tạo một đối tượng FXMLLoader để tải DialogGioiThieu.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/DialogGioiThieu.fxml"));
            Parent parent = fxmlLoader.load();

            // Tạo một cửa sổ mới (Stage) cho dialog
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gioi thieu");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Loi", "Khong the mo cua so gioi thieu.");
        }
    }

    @FXML
    void mnXemTaiKhoan(ActionEvent event) {
        try {
            // Tạo một đối tượng FXMLLoader để tải DialogThongTinTaiKhoan.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/App/DialogThongTinTaiKhoan.fxml"));
            Parent parent = fxmlLoader.load();

            // Tạo một cửa sổ mới (Stage) cho dialog
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thong tin tai khoan");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            MessageDialogHelpers.showErrorDialog(mainFrame, "Loi", "Khong the mo cua so thong tin tai khoan.");
        }

        }
    
    @FXML
    void btnThoat(ActionEvent event) {
        Platform.exit();
    }
}
