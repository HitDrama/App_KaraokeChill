
package App;

import dao.ChiTietHoaDonDao;
import dao.HoaDonDao;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import helpers.ShareData;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.beans.property.SimpleDoubleProperty;

public class DialogDanhSachHoaDonController implements Initializable {

    @FXML
    private Button btnLammoi;

    @FXML
    private Button btnQuayLai;

    @FXML
    private Button btnXuatHoaDon;

    @FXML
    private ComboBox<String> cmbTimHD;

    @FXML
    private TableColumn<HoaDon, String> colMaHoaDon;
    @FXML
    private TableColumn<HoaDon, String> colNhanVienLap;
    @FXML
    private TableColumn<HoaDon, String> colTenKhachHang;
    @FXML
    private TableColumn<HoaDon, String> colNgayLapHoaDon;
    @FXML
    private TableColumn<HoaDon, String> colTongTien;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colSanPham;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colDonGia;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colThanhTien;

    @FXML
    private TableView<ChiTietHoaDon> tblChiTiet;

    @FXML
    private TableView<HoaDon> tblHoaDon;

    @FXML
    private TextField txtTimKiem;

    private ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList();
    private ObservableList<ChiTietHoaDon> chiTietHoaDonList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbTimHD.getItems().addAll(
            "Tìm theo mã hóa đơn",
            "Tìm theo tên khách hàng",
            "Tìm theo ngày lập",
            "Tìm theo tổng tiền"
        );

        initTableHoaDon();
        initTableChiTietSanPham();
        loadDataToTableHoaDon();
    }

    private void initTableHoaDon() {
    colMaHoaDon.setCellValueFactory(cellData -> new SimpleStringProperty("HD"+cellData.getValue().getMaHoaDon()));
    colNhanVienLap.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getHoTen()));
    colTenKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenKhachHang()));

    colNgayLapHoaDon.setCellValueFactory(cellData -> {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return new SimpleStringProperty(formatter.format(cellData.getValue().getNgayTao()));
    });

    colTongTien.setCellValueFactory(cellData -> {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return new SimpleStringProperty(currencyFormatter.format(cellData.getValue().getTongTien()));
    });

    tblHoaDon.setItems(hoaDonList);
}

    private void initTableChiTietSanPham() {
        colSanPham.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSanPham().getTenSanPham()));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));        
        colDonGia.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSanPham().getDonGia()).asObject());
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        tblChiTiet.setItems(chiTietHoaDonList);
    }

    private void loadDataToTableHoaDon() {
        try {
            HoaDonDao hoaDonDao = new HoaDonDao();
            List<HoaDon> listHoaDon = hoaDonDao.getDSHoaDon(ShareData.taiKhoanDangNhap.getNhanVien());

            hoaDonList.addAll(listHoaDon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            if (path.exists()) {
                java.awt.Desktop.getDesktop().open(path);
            } else {
                System.out.println("File does not exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(String str) {
        ObservableList<HoaDon> filteredList = FXCollections.observableArrayList();

        for (HoaDon hoaDon : hoaDonList) {
            if (cmbTimHD.getValue().equals("Tìm theo mã hóa đơn") && hoaDon.getMaHoaDon().contains(str)) {
                filteredList.add(hoaDon);
            }
            if (cmbTimHD.getValue().equals("Tìm theo tên khách hàng") && hoaDon.getTenKhachHang().contains(str)) {
                filteredList.add(hoaDon);
            }
            if (cmbTimHD.getValue().equals("Tìm theo ngày lập") && hoaDon.getNgayTaoFormatted().contains(str)) {
                filteredList.add(hoaDon);
            }
            if (cmbTimHD.getValue().equals("Tìm theo tổng tiền") && hoaDon.getTongTienFormatted().contains(str)) {
                filteredList.add(hoaDon);
            }
        }

        tblHoaDon.setItems(filteredList);
    }


    @FXML
    void btnQuayLai(ActionEvent event) {
        // Đóng Stage hiện tại
        Stage stage = (Stage) btnQuayLai.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnXuatHoaDon(ActionEvent event) {
        try {
            HoaDon selectedHoaDon = tblHoaDon.getSelectionModel().getSelectedItem();
            if (selectedHoaDon != null) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
                fileChooser.getExtensionFilters().add(extFilter);
                File saveFile = fileChooser.showSaveDialog(btnXuatHoaDon.getScene().getWindow());

                if (saveFile != null) {
                    saveFile = new File(saveFile.toString() + ".xlsx");
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Hoadon");

                    // Tạo hàng đầu tiên (header) cho bảng chi tiết hóa đơn
                    Row rowCol = sheet.createRow(0);
                    for (int i = 0; i < tblChiTiet.getColumns().size(); i++) {
                        Cell cell = rowCol.createCell(i);
                        cell.setCellValue(tblChiTiet.getColumns().get(i).getText());
                    }

                    // Điền dữ liệu vào bảng chi tiết hóa đơn
                    for (int j = 0; j < tblChiTiet.getItems().size(); j++) {
                        Row row = sheet.createRow(j + 1);
                        for (int k = 0; k < tblChiTiet.getColumns().size(); k++) {
                            if (tblChiTiet.getColumns().get(k).getCellData(j) != null) {
                                Cell cell = row.createCell(k);
                                cell.setCellValue(tblChiTiet.getColumns().get(k).getCellData(j).toString());
                            }
                        }
                    }

                    // Tạo hàng tiếp theo để lưu thông tin hóa đơn
                    int col = tblChiTiet.getItems().size() + 2;
                    Row rowCol1 = sheet.createRow(col);
                    for (int i = 0; i < tblHoaDon.getColumns().size(); i++) {
                        Cell cell = rowCol1.createCell(i);
                        cell.setCellValue(tblHoaDon.getColumns().get(i).getText());
                    }

                    Row rowCol2 = sheet.createRow(col + 1);
                    rowCol2.createCell(0).setCellValue(selectedHoaDon.getMaHoaDon());
                    rowCol2.createCell(1).setCellValue(selectedHoaDon.getNhanVien().getHoTen());
                    rowCol2.createCell(2).setCellValue(selectedHoaDon.getTenKhachHang());
                    rowCol2.createCell(3).setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedHoaDon.getNgayTao()));
                    rowCol2.createCell(4).setCellValue(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(selectedHoaDon.getTongTien()));

                    FileOutputStream out = new FileOutputStream(saveFile);
                    workbook.write(out);
                    workbook.close();
                    out.close();
                    openFile(saveFile.toString());
                } else {
                    System.out.println("Đã xảy ra lỗi!");
                }
            } else {
                System.out.println("Cần chọn 1 hóa đơn");
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    @FXML
    void btnLammoi(ActionEvent event) {
        txtTimKiem.clear();
        hoaDonList.clear();
        chiTietHoaDonList.clear();
        loadDataToTableHoaDon();
    }

    @FXML
    void tblHoaDon(MouseEvent event) {
        if (event.getClickCount() == 1) { // Kiểm tra xem có phải click đơn không
            HoaDon selectedHoaDon = tblHoaDon.getSelectionModel().getSelectedItem();
            if (selectedHoaDon != null) {
                ChiTietHoaDonDao cthdDao = new ChiTietHoaDonDao();
                chiTietHoaDonList.clear();

                List<ChiTietHoaDon> dsCTHD = cthdDao.getdsCTHD(selectedHoaDon.getMaHoaDon());
                for (ChiTietHoaDon cthd : dsCTHD) {
                    chiTietHoaDonList.add(cthd);
                }

                tblChiTiet.setItems(chiTietHoaDonList);
            }
        }
    }
}
