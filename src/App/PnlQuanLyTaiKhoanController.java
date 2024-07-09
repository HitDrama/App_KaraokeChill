package App;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import helpers.ShareData;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javax.swing.JOptionPane;

public class PnlQuanLyTaiKhoanController implements Initializable{

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnThemTK;

    @FXML
    private Button btnXoaTK;

    @FXML
    private ComboBox<String> cmbListCauHoi;

    @FXML
    private ComboBox<String> cmbMaNhanVien;

    @FXML
    private ComboBox<String> cmbTim;

    @FXML
    private ComboBox<String> cmbVaiTro;

    @FXML
    private TableView<TaiKhoan> tableView;

    @FXML
    private TextArea txaCauTraLoi;

    @FXML
    private TextField txtMatKhau;

    @FXML
    private TextField txtTenDN;

    @FXML
    private TextField txtTim;

    private Window mainFrame;
    
    @FXML
    private TextField txtCard;

    @FXML
    void btnLamMoi(ActionEvent event) {
        lamMoiText();
        loadDataToTable();

        // Additional code to reset table sorter if needed
        tableView.getSortOrder().clear();
    }

    @FXML
    void btnThemTK(ActionEvent event) {
        TaiKhoan tk = createTaiKhoan();
        TaiKhoanDAO tkDAO = new TaiKhoanDAO();

        StringBuilder sb = new StringBuilder();
        dataValidate(sb);

        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(null, "Error", sb.toString());
            return;
        }

        String maNV = cmbMaNhanVien.getSelectionModel().getSelectedItem();
        int maNVInt = Integer.parseInt(maNV.replaceAll("NV", ""));
        
        String maCard = txtCard.getText();

        if (tkDAO.checkExist(maNVInt)) {
            MessageDialogHelpers.showErrorDialog(null, "Warning", "Username already exists or employee code is duplicated");
            return;
        } else if (tkDAO.checkCardExist(maCard)) {
            MessageDialogHelpers.showErrorDialog(null, "Warning", "Card number is already in use");
            return;
        } else {
            if (tkDAO.addTaiKhoan(tk)) {
                MessageDialogHelpers.showMessageDialog(null, "Information", "Account added successfully");
                loadDataToTable();
                cmbTim.getItems().add(tk.getTenDangNhap());
                lamMoiText();
            }
        }
    }

    @FXML
    void btnXoaTK(ActionEvent event) {
        TaiKhoanDAO tkDAO = new TaiKhoanDAO();
        TaiKhoan selectedTK = tableView.getSelectionModel().getSelectedItem();

        if (selectedTK != null) {
            String tenDN = selectedTK.getTenDangNhap();

            if (!tenDN.equals(ShareData.taiKhoanDangNhap.getTenDangNhap())) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Warning");
                confirmAlert.setHeaderText("Are you sure you want to delete this account?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            if (tkDAO.deleteTaiKhoan(tenDN)) {
                                MessageDialogHelpers.showMessageDialog(mainFrame ,"Information", "Deleted successfully");
                                taiKhoanList.remove(selectedTK);
                                cmbTim.getItems().remove(tenDN);
                            } else {
                                MessageDialogHelpers.showMessageDialog(mainFrame,"Error", "Deletion failed");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
           } else {
                MessageDialogHelpers.showMessageDialog(mainFrame,"Warning", "Cannot delete currently logged-in account");
            }
        } else {
            MessageDialogHelpers.showMessageDialog(mainFrame,"Warning", "Please select a row in the table");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbVaiTro.getItems().add("Quan Ly");
        cmbVaiTro.getItems().add("Nhan Vien Le Tan");

        cmbListCauHoi.getItems().add("What is your favorite color?");
        cmbListCauHoi.getItems().add("Do you have any pets?");
        cmbListCauHoi.getItems().add("What is your girlfriend's name?");
	cmbListCauHoi.getItems().add("Where do your parents live?");
	cmbListCauHoi.getItems().add("What do you do in your free time?");

        cmbTim.getItems().add("Search by username");

        initTable();
        loadDataToTable();
        loadDataToComboMaNV();
    }

    private ObservableList<TaiKhoan> taiKhoanList;
    private FilteredList<TaiKhoan> filteredData;

    private void initTable() {
        TableColumn<TaiKhoan, String> tenDangNhapColumn = new TableColumn<>("Username");
        tenDangNhapColumn.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));

        TableColumn<TaiKhoan, String> vaiTroColumn = new TableColumn<>("Role");
        vaiTroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVaiTro()));

        TableColumn<TaiKhoan, String> maNhanVienColumn = new TableColumn<>("Employee Code");
        maNhanVienColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getMaNhanVien()));
        
        TableColumn<TaiKhoan,String> maCard = new TableColumn<>("CardNumber");
        maCard.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));

        tableView.getColumns().addAll(tenDangNhapColumn, vaiTroColumn, maNhanVienColumn, maCard);

        taiKhoanList = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(taiKhoanList, p -> true);
        tableView.setItems(filteredData);
        tableView.setOnMouseClicked(this::tblDSTaiKhoanMouseClicked);
        txtTim.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue);
        });
    }

    private void loadDataToTable() {
        try {
            TaiKhoanDAO tkDAO = new TaiKhoanDAO();
            List<TaiKhoan> listTK = tkDAO.getDanhSachTaiKhoan();
            taiKhoanList.setAll(listTK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tblDSTaiKhoanMouseClicked(MouseEvent event) {
        try {
            TaiKhoan selectedTK = tableView.getSelectionModel().getSelectedItem();

            if (selectedTK != null) {
                txtTenDN.setText(selectedTK.getTenDangNhap());
                cmbVaiTro.setValue(selectedTK.getVaiTro());
                cmbMaNhanVien.setValue(selectedTK.getNhanVien().getMaNhanVien());
                txtCard.setText(selectedTK.getCardNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lamMoiText() {
        txtTenDN.setText("");
        txtMatKhau.setText("");
        cmbVaiTro.getSelectionModel().select(0);
        cmbListCauHoi.getSelectionModel().select(0);
        cmbMaNhanVien.getSelectionModel().select(0);
        txaCauTraLoi.setText("");
        txtTim.setText("");
        txtTenDN.requestFocus();
        txtCard.setText("");
    }

    private TaiKhoan createTaiKhoan() {
        TaiKhoan tk = new TaiKhoan();
        NhanVienDAO nvDAO = new NhanVienDAO();

        tk.setTenDangNhap(txtTenDN.getText());
        tk.setMatKhau(txtMatKhau.getText());
        tk.setVaiTro(cmbVaiTro.getSelectionModel().getSelectedItem());

        String maNhanVien = cmbMaNhanVien.getSelectionModel().getSelectedItem();
        NhanVien nhanVien = nvDAO.getNhanVienTheoMa(maNhanVien);
        tk.setNhanVien(nhanVien);

        tk.setCauHoi(cmbListCauHoi.getSelectionModel().getSelectedItem());
        tk.setTraLoi(txaCauTraLoi.getText());
        tk.setCardNumber(txtCard.getText());
        return tk;
    }

    private void dataValidate(StringBuilder sb) {
        DataValidator.validateEmpty(txtMatKhau, sb, "Password cannot be empty");
        DataValidator.validateEmpty(txtTenDN, sb, "Username cannot be empty");
        DataValidator.validateTenDN(txtTenDN, sb, "Username cannot contain spaces or special characters");
        DataValidator.validateMatKhau(txtMatKhau, sb, "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, no spaces, and be at least 8 characters long");
        DataValidator.validateEmpty(txtCard, sb, "Cardnumber cannot be empty");
    }

    private void search(String str) {
        if (cmbTim.getSelectionModel().getSelectedItem().equals("Search by username")) {
            filteredData.setPredicate(taiKhoan -> {
                if (str == null || str.isEmpty()) {
                    return true;
                }
                return taiKhoan.getTenDangNhap().toLowerCase().contains(str.toLowerCase());
            });
        }
    }

    private void loadDataToComboMaNV() {
        NhanVienDAO nvDAO = new NhanVienDAO();
        List<NhanVien> listNV = nvDAO.getDanhSachNhanVien();
        for (NhanVien nhanVien : listNV) {
            cmbMaNhanVien.getItems().add(nhanVien.getMaNhanVien());
        }
    }
}
