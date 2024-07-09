package App;

import dao.KhachHangDAO;
import entity.KhachHang;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class PnlQuanLyKhachHangController implements Initializable {

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnSuaKhachHang;

    @FXML
    private Button btnThemKhachHang;

    @FXML
    private ComboBox<String> cmbTim;

    @FXML
    private ImageView imgthem;

    @FXML
    private AnchorPane mainfrom;

    @FXML
    private TableView<KhachHang> tableview;

    @FXML
    private TextField txtLoaiKhachHang;

    @FXML
    private TextField txtMaKhachHang;

    @FXML
    private TextField txtSoDT;

    @FXML
    private TextField txtSoLanDen;

    @FXML
    private TextField txtTenKhachHang;

    @FXML
    private TextField txtTim;
    private Window mainFrame;
    public void initialize(URL location, ResourceBundle resources){
        initTable();
        loadDataToTable();
        cmbTim.getItems().add("Search by customer name");
        cmbTim.getItems().add("Search by phone number");
    }
    private ObservableList<KhachHang> khachHangList;
    private void initTable(){
        TableColumn<KhachHang, String> colMaKhachHang = new TableColumn<>("Customer code");
        colMaKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));

        TableColumn<KhachHang, String> colTenKhachHang = new TableColumn<>("Customer name");
        colTenKhachHang.setCellValueFactory(new PropertyValueFactory<>("hoTenKH"));

        TableColumn<KhachHang, String> colSoDT = new TableColumn<>("Phone number");
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));

        TableColumn<KhachHang, Integer> colSoLanDen = new TableColumn<>("Number of Visits");
        colSoLanDen.setCellValueFactory(new PropertyValueFactory<>("soLanDen"));

        TableColumn<KhachHang, String> colLoaiKhachHang = new TableColumn<>("Customer Type");
        colLoaiKhachHang.setCellValueFactory(new PropertyValueFactory<>("loaiKhachHang"));

        tableview.getColumns().addAll(colMaKhachHang, colTenKhachHang, colSoDT, colSoLanDen, colLoaiKhachHang);

        khachHangList = FXCollections.observableArrayList();
        tableview.setItems(khachHangList);
    }
    
    public void loadDataToTable() {
        khachHangList.clear(); // Clear the list before adding new data
        try {
            KhachHangDAO khachHangDAO = new KhachHangDAO();
            List<KhachHang> listKH = khachHangDAO.getDanhSachKhachHang();

            for (KhachHang khachHang : listKH) {
                if (khachHang.getSoLanDen() > 2) {
                    khachHang.setLoaiKhachHang("Loyal customers");
                    khachHangDAO.updateLoaiKhachHang(khachHang);
                }
                khachHangList.add(khachHang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    private KhachHang createKhachHang() {
		KhachHang khachHang = new KhachHang();
		khachHang.setHoTenKH(txtTenKhachHang.getText());
		khachHang.setSoDienThoai(txtSoDT.getText());
		return khachHang;
	}
      @FXML
    void tableview(MouseEvent event) {
        try {
            KhachHang selectedKhachHang = tableview.getSelectionModel().getSelectedItem();
            if (selectedKhachHang != null) {
                KhachHangDAO khachHangDAO = new KhachHangDAO();
                KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(selectedKhachHang.getMaKhachHang());

                if (khachHang != null) {
                    txtMaKhachHang.setText(khachHang.getMaKhachHang());
                    txtTenKhachHang.setText(khachHang.getHoTenKH());
                    txtSoDT.setText(khachHang.getSoDienThoai());
                    txtLoaiKhachHang.setText(khachHang.getLoaiKhachHang());
                    txtSoLanDen.setText(Integer.toString(khachHang.getSoLanDen()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnThemKhachHang(ActionEvent event) {
        KhachHang khachHang = createKhachHang();
        KhachHangDAO khachHangDAO = new KhachHangDAO();

        StringBuilder sb = new StringBuilder();
        DataValidator(sb);

        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", sb.toString());
            return;
        }

        if (khachHangDAO.checkExist(txtSoDT.getText())) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Warning", "The customer already exists, the phone number is the same");
            return;
        } else {
            if (khachHangDAO.addKhachHang(khachHang)) {
                MessageDialogHelpers.showMessageDialog(mainFrame, "Notification", "The customer has been added successfully");
                loadDataToTable(); // Refresh the table
                LamMoiText();
                cmbTim.getItems().clear();
            } else {
                MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Add failed");
            }
        }
    }
    
    @FXML
    void btnSuaKhachHang(ActionEvent event) {
        int row = tableview.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            StringBuilder sb = new StringBuilder();
            DataValidator(sb);

            if (sb.length() > 0) {
                MessageDialogHelpers.showErrorDialog(mainFrame, "Error", sb.toString());
                return;
            }

            KhachHangDAO khachHangDAO = new KhachHangDAO(); // Create an instance of KhachHangDAO

            if (khachHangDAO.checkExist(txtSoDT.getText())) {
                MessageDialogHelpers.showErrorDialog(mainFrame, "Warning", "Duplicate phone numbers");
                return;
            } else {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm");
                confirmDialog.setHeaderText(null);
                confirmDialog.setContentText("Are you sure you want to update?");
                if (confirmDialog.showAndWait().get() != ButtonType.OK) {
                    return;
                }

                try {
                    KhachHang khachHang = createKhachHang(); // Create a KhachHang instance to update
                    khachHang.setMaKhachHang(txtMaKhachHang.getText());

                    if (khachHangDAO.updateKhachHang(khachHang)) {
                        MessageDialogHelpers.showMessageDialog(mainFrame, "Notification", "You have successfully updated");
                        loadDataToTable();
                    } else {
                        MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Update failed");
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "A row in the table must be selected");
        }
    }

    private void DataValidator(StringBuilder sb) {
        DataValidator.validateEmpty(txtSoDT, sb, "Phone number can not be left blank");
        DataValidator.validateEmpty(txtTenKhachHang, sb, "Customer name cannot be blank");
        DataValidator.validateVietnameseCharacters(txtTenKhachHang, sb,
                "Tên khách hàng sai. Không được nhập số và kí tự đặc biệt");
        DataValidator.validateSoDT(txtSoDT, sb,
                "The phone number is in the wrong format, must have 9-10 digits, no characters. For example: 0788775877");
        // Check if the phone number already exists in the database
        try {
            KhachHangDAO khachHangDAO = new KhachHangDAO();
            if (khachHangDAO.checkExist(txtSoDT.getText())) {
                sb.append("Phone number already exists\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("Error when checking phone number: ").append(e.getMessage()).append("\n");
        }
    }
    
    public void LamMoiText(){
         txtMaKhachHang.setText("");
		txtTenKhachHang.setText("");
		txtSoDT.setText("");
		txtSoLanDen.setText("");
		txtTim.setText("");
		txtMaKhachHang.requestFocus();
		txtLoaiKhachHang.setText("");
    }
    @FXML
    void btnLammoi(ActionEvent event) {
        LamMoiText();
        loadDataToTable();
        tableview.getSortOrder().clear();
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
        Predicate<KhachHang> predicate = null;

        if (selectedItem.equals("Search by customer name")) {
            predicate = kh -> kh.getHoTenKH().toLowerCase().contains(str.toLowerCase());
        } else if (selectedItem.equals("Search by phone number")) {
            predicate = kh -> kh.getSoDienThoai().toLowerCase().contains(str.toLowerCase());
        }

        // Áp dụng bộ lọc
        if (predicate != null) {
            ObservableList<KhachHang> filteredList = khachHangList.filtered(predicate);
            tableview.setItems(filteredList);
        } else {
            tableview.setItems(khachHangList); // Nếu không có bộ lọc, hiển thị tất cả
        }
       
    }
}
