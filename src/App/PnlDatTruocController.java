package App;

import dao.DonDatPhongDAO;
import dao.HoaDonDao;
import dao.KhachHangDAO;
import dao.PhongDAO;
import entity.DonDatPhong;
import entity.HoaDon;
import entity.KhachHang;
import entity.Phong;
import helpers.MessageDialogHelpers;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class PnlDatTruocController implements Initializable {

    @FXML
    private TableView<Phong> TableViewDSPhong;

    @FXML
    private TableView<DonDatPhong> TableViewDonDatPhong;
    
    @FXML
    private TableColumn<Phong, Double> colDonGia;

    @FXML
    private TableColumn<Phong, String> colLoaiPhong;

    @FXML
    private TableColumn<Phong, String> colMaPhong;

    @FXML
    private TableColumn<Phong, String> colTenPhong;
    
    @FXML
    private TableColumn<DonDatPhong, String> colNgayDat;

    @FXML
    private TableColumn<DonDatPhong, String> colSDT;

    @FXML
    private TableColumn<DonDatPhong, String> colTenKH;

    @FXML
    private TableColumn<DonDatPhong, String> colTenPhong2;

    @FXML
    private TableColumn<DonDatPhong, String> colTrangThai;

    @FXML
    private TableColumn<DonDatPhong, String> colLoaiPhong2;

    @FXML
    private TableColumn<DonDatPhong, String> colMaDatPhong;

    @FXML
    private ComboBox<String> cmbTimDon;

    @FXML
    private ComboBox<String> cmb_LocPhongTheoTrangThai;

    @FXML
    private DatePicker dateChooser;

    @FXML
    private Button nutDatTruoc;

    @FXML
    private Button nutHuyDonDat;

    @FXML
    private Button nutLamMoi;

    @FXML
    private Button nutNhanPhong;

    @FXML
    private Button nutTim;

    @FXML
    private Button nutTimPhong;

    @FXML
    private TextField txtTimDon;

    @FXML
    private TextField txt_LoaiKH;

    @FXML
    private TextField txt_MaKH;

    @FXML
    private TextField txt_TenKH;

    @FXML
    private TextField txt_TimSDT;

    private ObservableList<DonDatPhong> dfDanhSachPhong = FXCollections.observableArrayList();
    
    private Date ngayDatTruoc;

    private Window mainFrame;
    
    @FXML
    public void btn_DatTruoc(ActionEvent event) {
        try {
            DonDatPhongDAO ddpDAO = new DonDatPhongDAO();
            DonDatPhong ddp = createThongTinDatTruoc();

            if (ddp != null) {
                // Set trạng thái của đơn đặt phòng thành "Đặt Trước"
                ddp.setTrangThaiDon("Đặt Trước");

                // Add booking information to database
                if (ddpDAO.addThongTinDatPhong(ddp)) {
                    // Show success message
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Thông báo", "Đặt Trước Thành Công");

                    // Clear and reload data in table views
                    danhSachDonDatPhong.clear();
                    loadDataToTableDonDatPhong();
                    dfDanhSachPhong.clear();
                    LocalDate ngayDatTruoc = dateChooser.getValue();
                    if (ngayDatTruoc != null) {
                        Date ngayDatTruocAsDate = java.sql.Date.valueOf(ngayDatTruoc);
                        loadDataToTablePhong(ngayDatTruocAsDate);
                    }
                    resetTxt();
                } else {
                    // Handle case where booking could not be added (show error message, if needed)
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Lỗi", "Không thể đặt trước vào lúc này");
                }
            }
        } catch (Exception e) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Đã xảy ra lỗi khi đặt trước phòng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    private DonDatPhong createThongTinDatTruoc() {
        try {
            DonDatPhong donDatPhong = new DonDatPhong();
            KhachHangDAO khachHangDAO = new KhachHangDAO();
            PhongDAO phongDAO = new PhongDAO();
            KhachHang kh = khachHangDAO.getKhachHangTheoSĐT(txt_TimSDT.getText());
            int selectedIndex = TableViewDSPhong.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                Phong phong = TableViewDSPhong.getSelectionModel().getSelectedItem();
                donDatPhong.setPhong(phong);
                donDatPhong.setKhachHang(kh);

                LocalDate ngayDatTruoc = dateChooser.getValue();
                if (ngayDatTruoc != null) {
                    donDatPhong.setThoiGianVao(Date.from(ngayDatTruoc.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    donDatPhong.setTrangThaiDon("Đặt Trước");
                } else {
                    MessageDialogHelpers.showErrorDialog(nutDatTruoc.getScene().getWindow(), "Cảnh báo", "Vui lòng chọn ngày đặt trước");
                    return null;
                }
            } else {
                MessageDialogHelpers.showErrorDialog(nutDatTruoc.getScene().getWindow(), "Cảnh báo", "Chọn 1 phòng để đặt");
                return null;
            }
            return donDatPhong;
        } catch (Exception e) {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Đã xảy ra lỗi khi tạo thông tin đặt trước: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        }

    @FXML
    public void btn_HuyDonDat(ActionEvent event) {
        DonDatPhongDAO donDatPhongDAO = new DonDatPhongDAO();
        int row = TableViewDonDatPhong.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            String maDonDat = colMaDatPhong.getCellData(row);

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Canh bao");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("Ban co chac muon huy don nay?");
            
            MessageDialogHelpers.showAlert(Alert.AlertType.ERROR, "Canh bao", "Ban co chac muon huy don nay?");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (donDatPhongDAO.updateTrangThaiDonDat_Huy(maDonDat)) {
                    MessageDialogHelpers.showMessageDialog(mainFrame,"Thong bao","Huy Thanh Cong");
                    danhSachDonDatPhong.clear(); // Xóa dữ liệu trong bảng
                    loadDataToTableDonDatPhong(); // Tải dữ liệu mới vào bảng
                } else {
                    MessageDialogHelpers.showMessageDialog(mainFrame,"Loi","Khong the huy don dat phong nay!");
                }
            }
        } else {
            MessageDialogHelpers.showMessageDialog(mainFrame,"Thong Bao","Phai Chon Mot Don Dat");
        }
    }
    public void resetTxt(){
        txtTimDon.clear();
        txt_LoaiKH.clear();
        txt_MaKH.clear();
        txt_TenKH.clear();
        txt_TimSDT.clear();
        dateChooser.setValue(null);
    }
    public void resetTable(){
        LocalDate ngayDatTruoc = dateChooser.getValue();
        if (ngayDatTruoc != null) {
            Date ngayDatTruocAsDate = java.sql.Date.valueOf(ngayDatTruoc);
            loadDataToTablePhong(ngayDatTruocAsDate);
        }
        danhSachDonDatPhong.clear();
        loadDataToTableDonDatPhong();
    }
    @FXML
    public void btn_LamMoi(ActionEvent event) {
        resetTxt();
        resetTable();
    }

    @FXML
    public void btn_NhanPhong(ActionEvent event) {
        DonDatPhongDAO donDatPhongDAO = new DonDatPhongDAO();
        HoaDonDao hoaDonDao = new HoaDonDao();
        KhachHangDAO khDao = new KhachHangDAO();
        HoaDon hoaDon = new HoaDon();

        // Check if any row is selected in the TableView
        int selectedIndex = TableViewDonDatPhong.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            DonDatPhong selectedDonDatPhong = TableViewDonDatPhong.getItems().get(selectedIndex);

            // Ensure selectedDonDatPhong is not null
            if (selectedDonDatPhong != null) {
                String maDonDat = selectedDonDatPhong.getMaDatPhong();

                // Check if the selected booking date matches the current date
                Date ngayDat = selectedDonDatPhong.getThoiGianVao(); // Ensure selectedDonDatPhong is not null

                // Check if the selected booking date matches the current date
                if (isToday(ngayDat)) {
                    // Update the status of the booking to indicate it's ready for payment
                    if (donDatPhongDAO.updateTrangThaiDonDat_ChoThanhToan(maDonDat)) {
                        // Set customer name for the invoice
                        hoaDon.setTenKhachHang(selectedDonDatPhong.getKhachHang().getHoTenKH());
                        hoaDonDao.addHoaDon(hoaDon); // Save invoice to database

                        // Show success message
                        showAlert(Alert.AlertType.INFORMATION, "Thong bao", null, "Nhan Phong Thanh Cong");

                        // Update the number of visits for the customer
                        KhachHang kh = khDao.getKhachHangTheoSĐT(selectedDonDatPhong.getKhachHang().getSoDienThoai());
                        khDao.updateSoLanDen(kh);

                        // Remove the processed booking from the TableView
                        danhSachDonDatPhong.remove(selectedDonDatPhong);
                    }
                } else {
                    // Show error message if the booking date is not today
                    showAlert(Alert.AlertType.ERROR, "Loi", null, "Phong van chua đen thoi gian nhan");
                }
            } else {
                // Handle case where selectedDonDatPhong is null
                showAlert(Alert.AlertType.ERROR, "Thong Bao", null, "Khong co don dat phong nao đuoc chon");
            }
        } else {
            // Show error message if no booking is selected
            showAlert(Alert.AlertType.ERROR, "Thong Bao", null, "Phai Chon Mot Don Dat");
        }
    }

    // Function to check if a date is today
    private boolean isToday(Date date) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar2.setTime(new Date());
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
               calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    // Function to display an alert message
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    public void btn_Tim(ActionEvent event) {
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        KhachHang kh = khachHangDAO.getKhachHangTheoSĐT(txt_TimSDT.getText());
        if (kh != null) {
            txt_MaKH.setText(kh.getMaKhachHang());
            txt_TenKH.setText(kh.getHoTenKH());
            txt_LoaiKH.setText(kh.getLoaiKhachHang());
            txt_TimSDT.setText(kh.getSoDienThoai());
        } else {
            // Hiển thị thông báo rằng không tìm thấy khách hàng
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thong Bao");
            alert.setHeaderText(null);
            alert.setContentText("Khong Tim Thay Khach Hang");
            alert.showAndWait();
        }
    }

    @FXML
    public void btn_TimPhong(ActionEvent event) {
        LocalDate ngayDatTruoc = dateChooser.getValue();
        if (ngayDatTruoc != null) {
            int dayChoosed = ngayDatTruoc.getDayOfMonth();
            int today = LocalDate.now().getDayOfMonth();

            if (dayChoosed <= today) {
                // Hiển thị thông báo lỗi khi ngày đặt phòng nhỏ hơn hoặc bằng ngày hiện tại
                MessageDialogHelpers.showErrorDialog(((Node) event.getSource()).getScene().getWindow(), "Loi", "Khong tim duoc phong, ngay dat phong phai lon hon ngay hom nay");
                return;
            }

            // Chuyển LocalDate thành Date
            Date ngayDatTruocAsDate = java.sql.Date.valueOf(ngayDatTruoc);

            // Xóa dữ liệu cũ
            dfDanhSachPhong.clear();
            // Tải dữ liệu mới vào bảng phòng
            loadDataToTablePhong(ngayDatTruocAsDate);
        } else {
            // Hiển thị thông báo lỗi nếu ngày chưa được chọn
            MessageDialogHelpers.showErrorDialog(((Node) event.getSource()).getScene().getWindow(), "Loi", "Vui long chon ngay dat phong");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTablePhong();
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        // Chuyển LocalDate thành java.util.Date
        Date date = java.sql.Date.valueOf(currentDate);
        // Gọi phương thức loadDataToTablePhong với ngày hiện tại
        loadDataToTablePhong(date);
        initTableDonDatPhong();
        loadDataToTableDonDatPhong();

        txtTimDon.textProperty().addListener((observable, oldValue, newValue) -> search(newValue));

        cmbTimDon.getItems().addAll(
        "Tim theo ten khach hang",
        "Tim theo ten phong",
        "Tim theo so đien thoai",
        "Tim theo ngay đat"
        );
        cmb_LocPhongTheoTrangThai.getItems().addAll(
        "Dat Truoc",
        "Huy Don"
        );
        cmb_LocPhongTheoTrangThai.setOnAction(event -> filterBookingsByStatus());
    }

    private void filterBookingsByStatus() {
        DonDatPhongDAO donDatPhongDao = new DonDatPhongDAO();
        String trangThaiDon = cmb_LocPhongTheoTrangThai.getSelectionModel().getSelectedItem();

        List<DonDatPhong> dsDonDatPhong = donDatPhongDao.getDanhSachDonDatPhong(trangThaiDon);
        danhSachDonDatPhong.clear();
        danhSachDonDatPhong.addAll(dsDonDatPhong);

        TableViewDonDatPhong.refresh();
    }
    
    private void search(String str) {
        FilteredList<DonDatPhong> filteredData = new FilteredList<>(danhSachDonDatPhong, p -> true);
        filteredData.setPredicate(donDatPhong -> {
            if (str == null || str.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = str.toLowerCase();
            switch (cmbTimDon.getValue()) {
                case "Tim theo ten khach hang":
                    return donDatPhong.getKhachHang().getHoTenKH().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo ten phong":
                    return donDatPhong.getPhong().getTenPhong().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo so đien thoai":
                    return donDatPhong.getKhachHang().getSoDienThoai().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo ngay đat":
                    return donDatPhong.getThoiGianVao().toString().toLowerCase().contains(lowerCaseFilter);
                default:
                    return false;
            }
        });
        TableViewDonDatPhong.setItems(filteredData);
    }
    private ObservableList<Phong> phongData= FXCollections.observableArrayList();
    
    private void initTablePhong() {
        colMaPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhong()));
        colTenPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTenPhong()));
        colLoaiPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLoaiPhong().getTenLoaiPhong()));
        colDonGia.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getLoaiPhong().getDonGia()));

        
        TableViewDSPhong.setItems(phongData);
    }

    private void loadDataToTablePhong(Date ngayDatTruoc) {
        try {
            PhongDAO phongDAO = new PhongDAO();
            List<Phong> listPhong = phongDAO.getDanhSachPhong();
            List<Phong> listPhongDatTruoc = phongDAO.getDanhSachPhongTheoNgayDatTruoc(ngayDatTruoc);
            listPhong.removeAll(listPhongDatTruoc);

            // Xóa dữ liệu cũ
            phongData.clear();

            // Thêm dữ liệu mới
            for (Phong phong : listPhong) {
                phongData.add(phong);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ObservableList<DonDatPhong> danhSachDonDatPhong = FXCollections.observableArrayList();

    
    private void initTableDonDatPhong() {
        colMaDatPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaDatPhong()));
        colTenKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getHoTenKH()));
        colSDT.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getSoDienThoai()));
        colTenPhong2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhong().getTenPhong()));
        colLoaiPhong2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhong().getLoaiPhong().getTenLoaiPhong()));
        colNgayDat.setCellValueFactory(cellData -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return new SimpleStringProperty(formatter.format(cellData.getValue().getThoiGianVao()));
        });
        colTrangThai.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTrangThaiDon()));

        danhSachDonDatPhong = FXCollections.observableArrayList();
        TableViewDonDatPhong.setItems(danhSachDonDatPhong);
    }

    // Load dữ liệu từ CSDL vào TableView
    private void loadDataToTableDonDatPhong() {
        try {
            DonDatPhongDAO ddpDAO = new DonDatPhongDAO();
            List<DonDatPhong> listTTDatPhong = ddpDAO.getDanhSachDonDatTruoc();

            // In danh sách ra console để kiểm tra
            for (DonDatPhong ddp : listTTDatPhong) {
                System.out.println(ddp);
            }

            danhSachDonDatPhong.clear();
            danhSachDonDatPhong.addAll(listTTDatPhong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
