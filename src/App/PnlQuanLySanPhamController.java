package App;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PnlQuanLySanPhamController implements Initializable {
    
    @FXML
    private Button btnCapNhatLoaiSP;

    @FXML
    private Button btnCapNhatSP;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnThemLoaiSP;

    @FXML
    private Button btnXoaLoaiSP;

    @FXML
    private Button btnXoaSP;

    @FXML
    private ComboBox<String> cmbLoaiSanPham;

    @FXML
    private ComboBox<String> cmbTim;

    @FXML
    private TextField txtDonGia;

    @FXML
    private TextField txtMaLoaiSP;

    @FXML
    private TextField txtMaSP;

    @FXML
    private TextField txtTenLoaiSP;

    @FXML
    private TextField txtTenSP;

    @FXML
    private TextField txtTim;

    @FXML
    private TableView<LoaiSanPham> viewLoaiSanPham;

    @FXML
    private TableView<SanPham> viewSanPham;

    @FXML
    void btnCapNhatLoaiSP(ActionEvent event) {
        int row = viewLoaiSanPham.getSelectionModel().getSelectedIndex();

        LoaiSanPham lsp = createLoaiSanPham();
        lsp.setMaLoaiSP(txtMaLoaiSP.getText());
        LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();

        if (row >= 0) {
            StringBuilder sb = new StringBuilder();
            dataValidateLoaiSP(sb);
            if (sb.length() > 0) {
                MessageDialogHelpers.showErrorDialog(null, "Loi", sb.toString());
                return;
            }
            if (loaiSanPhamDAO.checkExist(txtTenLoaiSP.getText())) {
                MessageDialogHelpers.showErrorDialog(null, "Canh bao", "Ten loai san pham trung");
                return;
            } else {
                int choice = MessageDialogHelpers.showConfirmDialog(null, "Xac nhan", "Ban co chac muon cap nhat?");
                if (choice == 1) { // NO_OPTION
                    return;
                }
                try {
                    if (loaiSanPhamDAO.updateLoaiSanPham(lsp)) {
                        MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Ban da cap nhat thanh cong");

                        loadDataToTblLoaiSanPham();
                        cmbLoaiSanPham.getItems().clear();
                        loadDataToCmbLoaiSanPham();
                    } else {
                        MessageDialogHelpers.showErrorDialog(null, "Loi", "Cap nhat khong thanh cong");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            MessageDialogHelpers.showErrorDialog(null, "Loi", "Phai chon mot dong trong bang");
        }
        xoaRongTextfieldLoaiSP();
    }

    @FXML
    void btnCapNhatSP(ActionEvent event) {
       int row = viewSanPham.getSelectionModel().getSelectedIndex();

        SanPham sp = createSanPham();
        sp.setMaSanPham(txtMaSP.getText());
        SanPhamDAO sanPhamDAO = new SanPhamDAO();

        if (row >= 0) {
            StringBuilder sb = new StringBuilder();
            dataValidateSP(sb);
            if (sb.length() > 0) {
                MessageDialogHelpers.showErrorDialog(null, "Loi", sb.toString());
                return;
            } else {
                int choice = MessageDialogHelpers.showConfirmDialog(null, "Xac nhan", "Ban co chac muon cap nhat?");
                if (choice == 1) { // NO_OPTION
                    return;
                }
                try {
                    if (sanPhamDAO.updateSanPham(sp)) {
                        MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Ban da cap nhat thanh cong");

                        loadDataToTblSanPham();
                    } else {
                        MessageDialogHelpers.showErrorDialog(null, "Loi", "Cap nhat khong thanh cong");
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            MessageDialogHelpers.showErrorDialog(null, "Loi", "Phai chon mot dong trong bang");
        }
        xoaRongTextfieldsSanPham();
    }

    @FXML
    void btnLamMoi(ActionEvent event) {
        xoaRongTextfieldsSanPham();
        xoaRongTextfieldLoaiSP();

        // Xoa va lam moi du lieu bang san pham
        sanPhamList.clear();
        loadDataToTblSanPham();

        // Xoa va lam moi du lieu bang loai san pham
        loaiSanPhamList.clear();
        loadDataToTblLoaiSanPham();

        // Xoa noi dung txtTim
        txtTim.setText("");
    }

    @FXML
    void btnThem(ActionEvent event) {
        SanPham sanPham = createSanPham();
        SanPhamDAO sanPhamDAO = new SanPhamDAO();

        StringBuilder sb = new StringBuilder();
        dataValidateSP(sb);

        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(null, "Loi", sb.toString());
            return;
        }

        if (sanPhamDAO.checkExist(txtTenSP.getText())) {
            MessageDialogHelpers.showErrorDialog(null, "Canh bao", "San pham da ton tai, ten san pham trung");
            return;
        } else {
            try {
                if (sanPhamDAO.addSanPham(sanPham)) {
                    MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Ban da them thanh cong");

                    loadDataToTblSanPham();
                    xoaRongTextfieldsSanPham();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @FXML
    void btnThemLoaiSP(ActionEvent event) {
        LoaiSanPham lsp = createLoaiSanPham();
        LoaiSanPhamDAO loaiSPDAO = new LoaiSanPhamDAO();

        StringBuilder sb = new StringBuilder();
        dataValidateLoaiSP(sb);
        if (sb.length() > 0) {
            MessageDialogHelpers.showErrorDialog(null, "Loi", sb.toString());
            return;
        }
        if (loaiSPDAO.checkExist(txtTenLoaiSP.getText())) {
            MessageDialogHelpers.showErrorDialog(null, "Canh bao", "Loai san pham da ton tai, ten loai san pham trung");
            return;
        } else {
            if (loaiSPDAO.addLoaiSanPham(lsp)) {
                MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Ban da them thanh cong");

                loadDataToTblLoaiSanPham();
                cmbLoaiSanPham.getItems().clear();
                loadDataToCmbLoaiSanPham();
                xoaRongTextfieldLoaiSP();
            }
        }
    }

    @FXML
    void btnXoaLoaiSP(ActionEvent event) {
        LoaiSanPham selectedLoaiSanPham = viewLoaiSanPham.getSelectionModel().getSelectedItem();
        LoaiSanPhamDAO lspDAO = new LoaiSanPhamDAO();
        SanPhamDAO spDAO = new SanPhamDAO();

        if (selectedLoaiSanPham != null) {
            String maLoaiSP = selectedLoaiSanPham.getMaLoaiSP();
            int stt = Integer.parseInt(maLoaiSP.replaceAll("LSP", ""));

            int choice = MessageDialogHelpers.showConfirmDialog(null, "Canh bao", "Ban co chac muon xoa loai san pham nay va cac san pham con?");
            if (choice == 1) { // NO_OPTION
                return;
            }

            try {
                // Xoa cac san pham con truoc
                if (spDAO.deleteSanPhamByLoai(maLoaiSP)) {
                    // Sau khi xoa cac san pham con thi xoa loai san pham
                    if (lspDAO.deleteLoaiSanPham(String.valueOf(stt))) {
                        MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Xoa thanh cong");

                        loadDataToTblLoaiSanPham();
                        cmbLoaiSanPham.getItems().clear();
                        loadDataToCmbLoaiSanPham();
                        xoaRongTextfieldLoaiSP();
                    } else {
                        MessageDialogHelpers.showErrorDialog(null, "Loi", "Xoa loai san pham khong thanh cong");
                    }
                } else {
                    MessageDialogHelpers.showErrorDialog(null, "Loi", "Xoa san pham con khong thanh cong");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessageDialogHelpers.showErrorDialog(null, "Loi", "Da co loi xay ra.");
            }
        } else {
            MessageDialogHelpers.showErrorDialog(null, "Loi", "Phai chon 1 dong trong bang");
        }
    }
    
    

    @FXML
    void btnXoaSP(ActionEvent event) {
        int row = viewSanPham.getSelectionModel().getSelectedIndex();
    SanPhamDAO spDAO = new SanPhamDAO();

    if (row >= 0) {
        String maSP = viewSanPham.getItems().get(row).getMaSanPham();
        String stt = maSP.replaceAll("SP", "");
        int choice = MessageDialogHelpers.showConfirmDialog(null, "Canh bao", "Ban co chac muon xoa san pham nay?");
        if (choice == 1) { // NO_OPTION
            return;
        }
        try {
            if (spDAO.deleteSanPham(stt)) {
                MessageDialogHelpers.showMessageDialog(null, "Thong bao", "Xoa thanh cong");

                loadDataToTblSanPham();
                cmbTim.getItems().clear();
            } else {
                MessageDialogHelpers.showErrorDialog(null, "Loi", "Xoa khong thanh cong");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    } else {
        MessageDialogHelpers.showErrorDialog(null, "Loi", "Phai chon mot dong trong bang");
    }
    }

    @FXML
    void tblLoaiSanPham(MouseEvent event) {
        try{
            LoaiSanPham selectedLoaiSanPham = viewLoaiSanPham.getSelectionModel().getSelectedItem();
            if (selectedLoaiSanPham != null) {
                txtMaLoaiSP.setText(selectedLoaiSanPham.getMaLoaiSP());
                txtTenLoaiSP.setText(selectedLoaiSanPham.getTenLoaiSP());
            }
        } catch (Exception e){
            e.printStackTrace();;
        }
    }

    @FXML
    void tblSanPham(MouseEvent event) {
        try {
            SanPham selectedSanPham = viewSanPham.getSelectionModel().getSelectedItem();
            if (selectedSanPham != null) {
                txtMaSP.setText(selectedSanPham.getMaSanPham());
                txtTenSP.setText(selectedSanPham.getTenSanPham());
                txtDonGia.setText(Double.toString(selectedSanPham.getDonGia()));
                cmbLoaiSanPham.getSelectionModel().select(selectedSanPham.getLoaiSanPham().getTenLoaiSP());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    @FXML
    private TableColumn<LoaiSanPham, String> colMaLoaiSP;

    @FXML
    private TableColumn<LoaiSanPham, String> colTenLoaiSP;
    
    @FXML
    private TableColumn<SanPham, String> colMaSP;

    @FXML
    private TableColumn<SanPham, String> colTenSP;

    @FXML
    private TableColumn<SanPham, String> colLoaiSP;

    @FXML
    private TableColumn<SanPham, Double> colDonGia;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Clear the ComboBox items
        cmbTim.getItems().clear();
        // Add items to the ComboBox
        cmbTim.getItems().addAll("Tim theo ten san pham", "Tim theo ma san pham");
    
        // Initialize tables
        initTableLoaiSP();
        initTableSanPham();

        // Load data to TableViews
        loadDataToTblLoaiSanPham();
        loadDataToTblSanPham();
        
        loadDataToCmbLoaiSanPham();
        
        txtTim.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });

        viewSanPham.setItems(filteredSanPhamList);
    }    
    
    private ObservableList<LoaiSanPham> loaiSanPhamList = FXCollections.observableArrayList();
    private ObservableList<SanPham> sanPhamList = FXCollections.observableArrayList();
    private FilteredList<SanPham> filteredSanPhamList = new FilteredList<>(sanPhamList, p -> true);

    private void loadDataToTblLoaiSanPham() {
        LoaiSanPhamDAO ds = new LoaiSanPhamDAO();
        List<LoaiSanPham> list = ds.getDanhSachLoaiSanPham();
        loaiSanPhamList.clear();
        loaiSanPhamList.addAll(list);
        viewLoaiSanPham.setItems(loaiSanPhamList);
    }

    private void loadDataToTblSanPham() {
       SanPhamDAO ds = new SanPhamDAO();
        List<SanPham> list = ds.getDanhSachSanPham();
        sanPhamList.clear();
        sanPhamList.addAll(list);
        viewSanPham.setItems(sanPhamList);
    }

    private void initTableLoaiSP() {
        colMaLoaiSP.setCellValueFactory(new PropertyValueFactory<>("maLoaiSP"));
        colTenLoaiSP.setCellValueFactory(new PropertyValueFactory<>("tenLoaiSP"));
    }   

    private void initTableSanPham() {
         colMaSP.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
    colTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
    colLoaiSP.setCellValueFactory(cellData -> {
        LoaiSanPham loaiSanPham = cellData.getValue().getLoaiSanPham();
        if (loaiSanPham != null) {
            return new SimpleStringProperty(loaiSanPham.getTenLoaiSP());
        } else {
            return new SimpleStringProperty(""); // hoac gia tri mac dinh neu can
        }
    });
    colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
    }
    
    private void xoaRongTextfieldLoaiSP() {
        txtMaLoaiSP.setText("");
        txtTenLoaiSP.setText("");
        txtMaLoaiSP.requestFocus();
    }
    
    private void xoaRongTextfieldsSanPham() {
    txtMaSP.setText("");
    txtTenSP.setText("");
    txtDonGia.setText("");
    if (!cmbLoaiSanPham.getItems().isEmpty()) {
        cmbLoaiSanPham.getSelectionModel().selectFirst();
    }
    viewSanPham.getSortOrder().clear();
    txtMaSP.requestFocus();
}
    private LoaiSanPham createLoaiSanPham() {
        LoaiSanPham loaiSanPham = new LoaiSanPham();
        loaiSanPham.setTenLoaiSP(txtTenLoaiSP.getText());
        return loaiSanPham;
    }
    
    private void dataValidateLoaiSP(StringBuilder sb) {
        DataValidator.validateEmpty(txtTenLoaiSP, sb, "Ten loai san pham khong duoc de trong");
        DataValidator.validateVietnameseCharacters(txtTenLoaiSP, sb, "Ten loai san pham sai. Khong duoc nhap so va ky tu dac biet");
    }
    
    private void search(String str) {
        filteredSanPhamList.setPredicate(new Predicate<SanPham>() {
            @Override
            public boolean test(SanPham sanPham) {
                if (str == null || str.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = str.toLowerCase();
                if (cmbTim.getSelectionModel().getSelectedItem().equals("Tim theo ten san pham")) {
                    return sanPham.getTenSanPham().toLowerCase().contains(lowerCaseFilter);
                } else if (cmbTim.getSelectionModel().getSelectedItem().equals("Tim theo ma san pham")) {
                    return sanPham.getMaSanPham().toLowerCase().contains(lowerCaseFilter);
                }
                return false;
            }
        });
    }
    
    private void dataValidateSP(StringBuilder sb) {
        DataValidator.validateEmpty(txtDonGia, sb, "Don gia khong duoc de trong");
        DataValidator.validateEmpty(txtTenSP, sb, "Ten san pham khong duoc de trong");
        DataValidator.validateDonGia(txtDonGia, sb, "Don gia chi duoc nhap so");
        DataValidator.validateVietnameseCharactersAndNumber(txtTenSP, sb, "Ten san pham sai. Khong co ky tu dac biet");
    }
    
    private SanPham createSanPham() {
        SanPham sp = new SanPham();
        LoaiSanPham loaiSanPham = new LoaiSanPham();
        LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();

        String tenLoaiSP = cmbLoaiSanPham.getSelectionModel().getSelectedItem();
    if (tenLoaiSP != null && !tenLoaiSP.isEmpty()) {
        String maLoaiSP = loaiSanPhamDAO.getMaTheoTenLoai(tenLoaiSP);
        loaiSanPham.setMaLoaiSP(maLoaiSP);
        loaiSanPham.setTenLoaiSP(tenLoaiSP);
    } else {
        loaiSanPham = null; // Hoac xu ly theo cach khac neu can
    }

        sp.setMaSanPham(txtMaSP.getText());
        sp.setTenSanPham(txtTenSP.getText());
        sp.setDonGia(Double.parseDouble(txtDonGia.getText()));
        sp.setLoaiSanPham(loaiSanPham);

        return sp;
    }
    
    private void loadDataToCmbLoaiSanPham() {
    LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();
    List<LoaiSanPham> lsp = loaiSanPhamDAO.getDanhSachLoaiSanPham();
    cmbLoaiSanPham.getItems().clear(); // Xoa cac muc hien tai
    for (LoaiSanPham loaiSanPham : lsp) {
        cmbLoaiSanPham.getItems().add(loaiSanPham.getTenLoaiSP());
    }
}
}
