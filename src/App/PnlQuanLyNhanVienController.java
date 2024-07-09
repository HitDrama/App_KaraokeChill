package App;

import dao.NhanVienDAO;
import entity.NhanVien;
import helpers.DataValidator;
import helpers.ImageHelpers;
import helpers.MessageDialogHelpers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class PnlQuanLyNhanVienController implements Initializable {

    @FXML
    private Label lblImage;

    @FXML
    private Button btnCapNhat;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnMoHinh;

    @FXML
    private Button btnThemNV;

    @FXML
    private ComboBox<String> cmbTim;

    @FXML
    private RadioButton radFemale;

    @FXML
    private RadioButton radMale;

    @FXML
    private TableView<NhanVien> tableview;

    @FXML
    private TextArea txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtHoTen;

    @FXML
    private TextField txtMaNhanVien;

    @FXML
    private TextField txtNamSinh;

    @FXML
    private TextField txtSoDT;

    @FXML
    private TextField txtTim;

    private Window mainFrame;
    private byte[] personalImage;

    @FXML
    void btnMoHinh(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));

        File file = fileChooser.showOpenDialog(mainFrame);

        if (file != null) {
            try {
                Image img = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(img);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                lblImage.setGraphic(imageView);
                personalImage = ImageHelpers.toByteArray(SwingFXUtils.fromFXImage(img, null), "jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void btnLamMoi(ActionEvent event) {
        lamMoiText();
        loadDataToTable();
        tableview.getSortOrder().clear();
    }

    @FXML
    void tableview(MouseEvent event) {
        try {
            NhanVien selectedNV = tableview.getSelectionModel().getSelectedItem();

            if (selectedNV != null) {
                txtMaNhanVien.setText(selectedNV.getMaNhanVien());
                txtHoTen.setText(selectedNV.getHoTen());
                txtEmail.setText(selectedNV.getEmail());
                txtNamSinh.setText(selectedNV.getNamSinh());
                txtSoDT.setText(selectedNV.getSoDT());
                txtDiaChi.setText(selectedNV.getDiaChi());

                boolean isMale = selectedNV.getGioiTinh() == 1;
                radMale.setSelected(isMale);
                radFemale.setSelected(!isMale);

                if (selectedNV.getHinh() != null) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(selectedNV.getHinh());
                    Image img = new Image(bis);
                    ImageView imageView = new ImageView(img);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    lblImage.setGraphic(imageView);
                    personalImage = selectedNV.getHinh();
                } else {
                    personalImage = null;
                    String imagePath = "/image/user.png";
                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl == null) {
                        System.err.println("Image not found at path: " + imagePath);
                    } else {
                        Image img = new Image(imageUrl.toString());
                        ImageView imageView = new ImageView(img);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        lblImage.setGraphic(imageView);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCapNhat(ActionEvent event) {
        int selectedIndex = tableview.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            NhanVien selectedNV = tableview.getSelectionModel().getSelectedItem();

            StringBuilder sb = new StringBuilder();
            dataValidate(sb);

            if (sb.length() > 0) {
                MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", sb.toString());
                return;
            }

            int confirm = MessageDialogHelpers.showConfirmDialog(mainFrame, "Cảnh báo", "Bạn có chắc muốn cập nhật cho nhân viên này?");
            if (confirm == 1) {
                return;
            }

            try {
                NhanVien updatedNV = createNhanVien();
                updatedNV.setMaNhanVien(selectedNV.getMaNhanVien()); // Sử dụng mã nhân viên của nhân viên đã chọn

                // Khởi tạo đối tượng NhanVienDAO
                NhanVienDAO nhanVienDAO = new NhanVienDAO();

                // Gọi phương thức updateNhanVien từ đối tượng nhanVienDAO
                if (nhanVienDAO.updateNhanVien(updatedNV)) {
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Thông báo", "Nhân viên đã cập nhật thành công");
                    loadDataToTable();
                    refreshComboBox();
                } else {
                    MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Cập nhật không thành công");
                    loadDataToTable();
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Đã xảy ra lỗi trong quá trình cập nhật");
            }
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Phải chọn một dòng trong bảng để cập nhật");
        }

        lamMoiText();
    }

    private void refreshComboBox() {
        cmbTim.getItems().clear(); // Xóa danh sách các mục trong ComboBox
        cmbTim.getItems().add("Tìm kiếm theo tên tài khoản");
        cmbTim.getItems().add("Tìm kiếm theo số điện thoại");
    }

    @FXML
    void btnThemNV(ActionEvent event) {
        NhanVien nv = createNhanVien();
        NhanVienDAO nhanVienDAO = new NhanVienDAO();

        StringBuilder sb = new StringBuilder();
        dataValidate(sb);

        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", sb.toString());
            return;
        }

        if (nhanVienDAO.checkExist(txtEmail.getText().trim(), txtSoDT.getText().trim())) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Trùng số điện thoại hoặc email");
            loadDataToTable();
            return;
        }

        if (nhanVienDAO.addNhanVien(nv)) {
            MessageDialogHelpers.showMessageDialog(mainFrame, "Thông báo", "Nhân viên đã thêm thành công");
            loadDataToTable();
            lamMoiText();
            cmbTim.getItems().clear(); // Xóa danh sách các mục trong ComboBox
            refreshComboBox();
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Thêm không thành công");
        }
    }

    private void dataValidate(StringBuilder sb) {
        DataValidator.validateEmpty(txtNamSinh, sb, "Năm sinh không được để trống");
        DataValidator.validateEmpty(txtSoDT, sb, "Số điện thoại không được để trống");
        DataValidator.validateEmpty(txtEmail, sb, "Email không được để trống");
        DataValidator.validateEmpty(txtHoTen, sb, "Tên nhân viên không được để trống");

        DataValidator.validateNamSinh(txtNamSinh, sb, "Năm sinh phải có 4 chữ số và không chứa kí tự đặc biệt");
        DataValidator.validateSoDT(txtSoDT, sb,
                "Số điện thoại sai định dạng, phải có từ 9-10 chữ số, không có kí tự. Ví dụ: 0788775877");
        DataValidator.validateEmail(txtEmail, sb, "Email sai định dạng. Ví dụ: minhquan@gmail.com");
        DataValidator.validateVietnameseCharacters(txtHoTen, sb,
                "Họ tên không được chứa kí tự đặc biệt. Ví dụ: Lê Hoàng Long");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        loadDataToTable();
        cmbTim.getItems().add("Tìm kiếm theo tên tài khoản");
        cmbTim.getItems().add("Tìm kiếm theo số điện thoại");
        lamMoiText();
        try {
            String imagePath = "/image/user.png";
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("Image not found at path: " + imagePath);
            } else {
                Image img = new Image(imageUrl.toString());
                ImageView imageView = new ImageView(img);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                lblImage.setGraphic(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<NhanVien> nhanVienList;

    private void initTable() {
        TableColumn<NhanVien, String> colMaNhanVien = new TableColumn<>("Mã nhân viên");
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));

        TableColumn<NhanVien, String> colHoTen = new TableColumn<>("Họ tên");
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));

        TableColumn<NhanVien, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<NhanVien, String> colSoDT = new TableColumn<>("Số điện thoại");
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));

        TableColumn<NhanVien, String> colNamSinh = new TableColumn<>("Năm sinh");
        colNamSinh.setCellValueFactory(new PropertyValueFactory<>("namSinh"));

        TableColumn<NhanVien, String> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(cellData -> {
            String gender = cellData.getValue().getGioiTinh() == 1 ? "Nam" : "Nữ";
            return new javafx.beans.property.SimpleStringProperty(gender);
        });

        TableColumn<NhanVien, String> colDiaChi = new TableColumn<>("Địa chỉ");
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        tableview.getColumns().addAll(colMaNhanVien, colHoTen, colEmail, colSoDT, colNamSinh, colGioiTinh, colDiaChi);

        nhanVienList = FXCollections.observableArrayList();
        tableview.setItems(nhanVienList);
    }

    private void loadDataToTable() {
        try {
            nhanVienList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới
            NhanVienDAO nvDAO = new NhanVienDAO();
            List<NhanVien> listNV = nvDAO.getDanhSachNhanVien();
            nhanVienList.addAll(listNV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NhanVien createNhanVien() {
        NhanVien nv = new NhanVien();
        nv.setHoTen(txtHoTen.getText());
        nv.setGioiTinh(radMale.isSelected() ? 1 : 0); // Đặt giới tính dựa trên lựa chọn của RadioButton
        nv.setSoDT(txtSoDT.getText());
        nv.setEmail(txtEmail.getText());
        nv.setDiaChi(txtDiaChi.getText());
        nv.setNamSinh(txtNamSinh.getText());
        nv.setHinh(personalImage); // Đảm bảo hình ảnh được gán cho đối tượng NhanVien        
        return nv;
    }

    private void lamMoiText() {
        txtMaNhanVien.setText("");
        txtHoTen.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtSoDT.setText("");
        txtNamSinh.setText("");
        txtTim.setText("");
        txtHoTen.requestFocus();
    }

    @FXML
    void txtTim(KeyEvent event) {
        search(txtTim.getText());
    }

    public void search(String str) {
        if (cmbTim.getSelectionModel().isEmpty()) {
            return;
        }

        String selectedItem = cmbTim.getSelectionModel().getSelectedItem();
        Predicate<NhanVien> predicate = null;

        if (selectedItem.equals("Tìm kiếm theo tên tài khoản")) {
            predicate = nv -> nv.getHoTen().toLowerCase().contains(str.toLowerCase());
        } else if (selectedItem.equals("Tìm kiếm theo số điện thoại")) {
            predicate = nv -> nv.getSoDT().toLowerCase().contains(str.toLowerCase());
        }

        // Áp dụng bộ lọc
        if (predicate != null) {
            ObservableList<NhanVien> filteredList = nhanVienList.filtered(predicate);
            tableview.setItems(filteredList);
        } else {
            tableview.setItems(nhanVienList); // Nếu không có bộ lọc, hiển thị tất cả
        }
    }
}
