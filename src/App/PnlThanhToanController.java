package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.LoaiSanPhamDAO;
import dao.ChiTietHoaDonDao;
import dao.SanPhamDAO;
import dao.HoaDonDao;
import entity.HoaDon;
import entity.LoaiSanPham;
import entity.SanPham;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import connectDB.MSSQLConnection;
import dao.DonDatPhongDAO;
import dao.PhongDAO;
import entity.ChiTietHoaDon;
import entity.LoaiPhong;
import entity.Phong;
import helpers.DataValidator;
import helpers.MessageDialogHelpers;
import helpers.ShareData;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PnlThanhToanController implements Initializable {
    
    @FXML
    private Button btnInHoaDon;
    
    @FXML
    private Button btnTraPhong;

    @FXML
    private ComboBox<String> cmbLoaiThucPham;

    @FXML
    private ComboBox<String> cmbTenThucPham;

    @FXML
    private ComboBox<String> cmbTimKiem;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> colDonGiaDichVu;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> colDongia;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colKhachHang;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colLoaiPhong;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colMaDonDat;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colMaPhong;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colSanPham;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colSoLuong;

    @FXML
    private TableColumn<ChiTietHoaDon, String> colTenPhong;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> colThanhTien;

    @FXML
    private TableColumn<ChiTietHoaDon, Date> colThoiGianVao;
    
    @FXML
    private TableColumn<ChiTietHoaDon, Boolean> colTrangThai;

    @FXML
    private Label lblDonGiaPhong;

    @FXML
    private Label lblKhachHang;

    @FXML
    private Label lblPhong;

    @FXML
    private Label lblTienDichVu;

    @FXML
    private Label lblTienThua;

    @FXML
    private Label lblTongTienHoaDon;

    @FXML
    private TextField txtSoLuong;

    @FXML
    private TextField txtTienKhachDua;
    
    @FXML
    private TextField txtNhanVienLap;

    @FXML
    private TextField txtTimPhong;

    @FXML
    private TableView<ChiTietHoaDon> viewDichVu;

    @FXML
    private TableView<ChiTietHoaDon> viewPhongDangSD;

    private ObservableList<ChiTietHoaDon> phongSuDungData;
    private ObservableList<ChiTietHoaDon> dichVuData;
    
    private FilteredList<ChiTietHoaDon> filteredData;
    
    private LocalTime gioRa = null;
    private int selectedRow = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewPhongDangSD.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cmbTimKiem.getItems().clear();
        cmbTimKiem.getItems().addAll(
                "Tim theo ma phong",
                "Tim theo ten phong",
                "Tim theo loai phong",
                "Tim theo don gia",
                "Tim theo thoi gian vao",
                "Tim theo khach hang"
        );
        
        txtNhanVienLap.setText(ShareData.taiKhoanDangNhap.getNhanVien().getHoTen());

        initTablePhongSuDung();
        initTableDichVu();
        loadTablePhongDangSuDungTheoTrangThai("Chua Thanh Toan");

        loadDataToCmbLoaiSanPham();
        loadDataToCmbTenSanPham();

        txtTienKhachDua.addEventFilter(KeyEvent.KEY_RELEASED, this::txtTienKhachDuaKeyReleased);
        
        txtTimPhong.setOnKeyReleased(e -> search(txtTimPhong.getText()));
        filteredData = new FilteredList<>(phongSuDungData, p -> true);
        SortedList<ChiTietHoaDon> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(viewPhongDangSD.comparatorProperty());

        viewPhongDangSD.setItems(sortedData);
    }

    private void initTablePhongSuDung() {
        colMaDonDat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoaDon().getMaHoaDon()));
        colMaPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhong().getMaPhong()));
        colTenPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhong().getTenPhong()));
        colLoaiPhong.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhong().getLoaiPhong().getTenLoaiPhong()));
        colDongia.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPhong().getLoaiPhong().getDonGia()).asObject());
        colThoiGianVao.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHoaDon().getNgayTao()));
        colKhachHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoaDon().getTenKhachHang()));
        colTrangThai.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isTrangThai()));

        phongSuDungData = FXCollections.observableArrayList();
        viewPhongDangSD.setItems(phongSuDungData);
    }

    private void initTableDichVu() {
        colSanPham.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSanPham().getTenSanPham()));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDonGiaDichVu.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSanPham().getDonGia()).asObject());
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        dichVuData = FXCollections.observableArrayList();
        viewDichVu.setItems(dichVuData);
    }

    
    
    private void loadTablePhongDangSuDungTheoTrangThai(String trangThaiDon) {
        String sql = "SELECT Room_reservation.BookingCode, Room_reservation.RoomID, Room.RoomName, Room_type.RoomTypeName, Room_type.RoomPrice, Room_reservation.CheckinTime, Customer.CustomerName " +
                     "FROM Room_reservation " +
                     "INNER JOIN Customer ON Room_reservation.CustomerID = Customer.CustomerID " +
                     "INNER JOIN Room ON Room_reservation.RoomID = Room.RoomID " +
                     "INNER JOIN Room_type ON Room.RoomTypeID = Room_type.RoomTypeID " +
                     "WHERE OrderStatus = ?";

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {
            prepareStatement.setString(1, trangThaiDon);
            ResultSet rs = prepareStatement.executeQuery();
            phongSuDungData.clear();
            while (rs.next()) {
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                HoaDon hoaDon = new HoaDon();
                Phong phong = new Phong();
                LoaiPhong loaiPhong = new LoaiPhong();

                hoaDon.setMaHoaDon("DP"+rs.getString("BookingCode"));
                hoaDon.setTenKhachHang(rs.getString("CustomerName"));
                hoaDon.setNgayTao(rs.getTimestamp("CheckinTime"));

                phong.setMaPhong("MP"+rs.getString("RoomID"));
                phong.setTenPhong(rs.getString("RoomName"));
                loaiPhong.setTenLoaiPhong(rs.getString("RoomTypeName"));
                loaiPhong.setDonGia(rs.getDouble("RoomPrice"));
                phong.setLoaiPhong(loaiPhong);

                chiTiet.setHoaDon(hoaDon);
                chiTiet.setPhong(phong);

                phongSuDungData.add(chiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataToCmbTenSanPham() {
        SanPhamDAO sanPhamDAO = new SanPhamDAO();
        List<SanPham> sp = sanPhamDAO.getDanhSachSanPham();
        for (SanPham sanPham : sp) {
            cmbTenThucPham.getItems().add(sanPham.getTenSanPham());
        }
    }

    private void loadDataToCmbLoaiSanPham() {
        LoaiSanPhamDAO loaiSanPhamDao = new LoaiSanPhamDAO();
        List<LoaiSanPham> dsLoaiSanPham = loaiSanPhamDao.getDanhSachLoaiSanPham();
        for (LoaiSanPham loaiSanPham : dsLoaiSanPham) {
            cmbLoaiThucPham.getItems().add(loaiSanPham.getTenLoaiSP());
        }
    }

    private void cmbLoaiThucPhamActionPerformed(ActionEvent evt) {
        SanPhamDAO sanPhamDao = new SanPhamDAO();
        List<String> dsTenSanPham = sanPhamDao.getSanPhamTheoLoai(cmbLoaiThucPham.getSelectionModel().getSelectedItem());
        cmbTenThucPham.getItems().clear();
        for (String tenSanPham : dsTenSanPham) {
            cmbTenThucPham.getItems().add(tenSanPham);
        }
    }
    
    @FXML
    private void loadDataCTHD() {
        HoaDonDao hoaDonDao = new HoaDonDao();
        ChiTietHoaDonDao cthdDao = new ChiTietHoaDonDao();
        double tongTien = 0;

        // Lấy tên khách hàng từ label và lấy mã hóa đơn từ DAO
        String tenKhachHang = lblKhachHang.getText();
        String maHoaDon = hoaDonDao.getMaHoaDon(tenKhachHang);

        // Kiểm tra nếu mã hóa đơn không null
        if (maHoaDon != null && !maHoaDon.isEmpty()) {
            List<ChiTietHoaDon> dsCTHD = cthdDao.getdsCTHD(maHoaDon);

            // Xóa dữ liệu cũ nếu có
            dichVuData.clear();

            // Duyệt qua danh sách chi tiết hóa đơn và thêm vào ObservableList
            for (ChiTietHoaDon cthd : dsCTHD) {
                dichVuData.add(cthd);
                tongTien += cthd.getThanhTien();
            }

            // Cập nhật tổng tiền dịch vụ
            lblTienDichVu.setText(tongTien + " VND");
        } else {
            // Xử lý trường hợp không tìm thấy mã hóa đơn
            System.out.println("Khong tim thay ma hoa don cho khach hang: " + tenKhachHang);
            lblTienDichVu.setText("0 VND");
        }
    }
    
    private void hienThiTongTien(int row) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        double tongTien = tinhTongTien(row); // Hàm tinhTongTien(row) trả về tổng tiền của hóa đơn
        lblTongTienHoaDon.setText("Tong tien: " + format.format(tongTien));
    }
    
    private double tinhTongTien(int row) {
        try {
            // Lấy thời gian vào từ bảng tblPhongDangSuDung
            String strThoiGianVao = colThoiGianVao.getCellData(row).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date thoiGianVao = sdf.parse(strThoiGianVao);

            // Lấy giờ và phút vào từ thời gian vào
            int gioVao = thoiGianVao.getHours();
            int phutVao = thoiGianVao.getMinutes();

            // Sử dụng thời gian trả phòng lưu trong ChiTietHoaDon
            ChiTietHoaDon chiTiet = phongSuDungData.get(row);
            int gioRaFinal;
            int phutRaFinal;
            if (chiTiet.getGioRa() != null) {
                gioRaFinal = chiTiet.getGioRa().getHour();
                phutRaFinal = chiTiet.getGioRa().getMinute();
            } else {
                // Nếu chưa trả phòng, sử dụng thời gian hiện tại
                gioRaFinal = LocalTime.now().getHour();
                phutRaFinal = LocalTime.now().getMinute();
            }

            // Lấy đơn giá từ bảng tblPhongDangSuDung
            double donGia = colDongia.getCellData(row);

            // Lấy tiền dịch vụ từ lblTienDichVu
            double tienDichVu = 0.0;
            String strTienDichVu = lblTienDichVu.getText().replaceAll("[^0-9.]", ""); // Xóa hết ký tự ngoài số và dấu chấm
            if (!strTienDichVu.isEmpty()) {
                tienDichVu = Double.parseDouble(strTienDichVu);
            }

            // Tính tổng thời gian sử dụng
            int tongGio = gioRaFinal - gioVao;
            int tongPhut = phutRaFinal - phutVao;
            int thoiGianSuDung = tongGio * 60 + tongPhut;

            // Tính tiền sử dụng phòng và cộng thêm tiền dịch vụ
            double tienSuDungPhong = thoiGianSuDung * (donGia / 60.0);
            return tienSuDungPhong + tienDichVu;
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ khi không thể parse thời gian vào từ chuỗi
            return 0.0; // hoặc giá trị mặc định phù hợp
        }
    }
    
    private void dataValidateThemTP(StringBuilder sb) {
        DataValidator.validateEmpty(txtSoLuong, sb, "Phai nhap so luong truoc");
        DataValidator.validateDonGia(txtSoLuong, sb, "Khong duoc nhap ki tu, so luong phai lon hon hoac bang 0");
    }

    
    
    private SanPham createSanPham() {
        String tenSanPham = cmbTenThucPham.getSelectionModel().getSelectedItem();
        SanPhamDAO sanPhamDao = new SanPhamDAO();
        return sanPhamDao.getSanPhamTheoTen(tenSanPham);
    }
    
    private void lamMoiText() {
        txtSoLuong.setText("");
        lblDonGiaPhong.setText("Don gia phong:");
        lblKhachHang.setText("Khach hang:");
        lblPhong.setText("Phong:");
        lblTienDichVu.setText("");
    }
    
    private void txtTienKhachDuaKeyReleased(KeyEvent e) {
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();

        if (row == -1) {
            // Không có hàng nào được chọn, do đó không thể tính toán tiền thừa
            return;
        }

        try {
            double tongTien = tinhTongTien(row);
            double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText().trim());
            double tienThua = tienKhachDua - tongTien;

            Locale locale = new Locale("vi", "VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);

            lblTienThua.setText(format.format(tienThua));
        } catch (NumberFormatException ex) {
            // Xử lý khi người dùng nhập không phải là số vào txtTienKhachDua
            lblTienThua.setText("So tien khong hop le");
        }
    }

    @FXML
    void btnTraPhong(ActionEvent event) {
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            // Lưu lại thời gian trả phòng cho hàng đã chọn
            ChiTietHoaDon chiTiet = phongSuDungData.get(row);
            chiTiet.setGioRa(LocalTime.now());
            chiTiet.setTrangThai(true);
            viewPhongDangSD.refresh(); // Cập nhật lại bảng hiển thị

            // Hiển thị thông báo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thong bao");
            alert.setHeaderText(null);
            alert.setContentText("Da luu thoi gian tra phong cho phong da chon");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Canh bao");
            alert.setHeaderText(null);
            alert.setContentText("Chua chon phong de tra phong");
            alert.showAndWait();
        }
    }

    @FXML
    void btnInHoaDon(ActionEvent event) {
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();

        if (row >= 0) {
            ChiTietHoaDon chiTietHD = phongSuDungData.get(row);

            if (chiTietHD.getGioRa() == null) {
                MessageDialogHelpers.showAlert(Alert.AlertType.WARNING, "Canh bao", "Chua luu thoi gian tra phong");
                return;
            }

            // Lấy dữ liệu để tính
            Date thoiGianVao = chiTietHD.getHoaDon().getNgayTao();
            int gioVao = thoiGianVao.getHours();
            int phutVao = thoiGianVao.getMinutes();

            double donGia = chiTietHD.getPhong().getLoaiPhong().getDonGia();
            double tienDichVu = lblTienDichVu.getText().isEmpty() ? 0 : Double.parseDouble(lblTienDichVu.getText().replaceAll(" VND", ""));

            // Sử dụng thời gian trả phòng đã lưu
            int gioRaFinal = chiTietHD.getGioRa().getHour();
            int phutRaFinal = chiTietHD.getGioRa().getMinute();

            // Tính theo công thức
            int tongGio = gioRaFinal - gioVao;
            int tongPhut = phutRaFinal - phutVao;
            int thoiGianSuDung = tongGio * 60 + tongPhut;
            double tienSuDungPhong = thoiGianSuDung * (donGia / 60);
            double tongTien = tienSuDungPhong + tienDichVu;

            // Format tiền tệ và ngày tháng
            Locale locale = new Locale("vi", "VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            String pattern = "dd-MM-yyyy hh:mm a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            // Lấy mã hóa đơn
            HoaDonDao hoaDonDao = new HoaDonDao();
            String maHoaDon = hoaDonDao.getMaHoaDon(lblKhachHang.getText());

            // Kiểm tra null cho maHoaDon
            if (maHoaDon == null || maHoaDon.isEmpty()) {
                MessageDialogHelpers.showAlert(Alert.AlertType.ERROR, "Loi", "Khong tim thay ma hoa don cho khach hang");
                return;
            }

            // Lấy dữ liệu chi tiết hóa đơn
            ChiTietHoaDonDao cthdDao = new ChiTietHoaDonDao();
            List<ChiTietHoaDon> dsCTHD = cthdDao.getdsCTHD(maHoaDon);

            // Đưa dữ liệu vào PDF
            String thoiGianRa = dateFormat.format(new Date());
            String thoiGianVaoFormated = dateFormat.format(thoiGianVao);

            // Tạo PDF
            PDFGenerator.createPDF("invoice.pdf", lblKhachHang.getText(), maHoaDon, thoiGianRa, thoiGianVaoFormated,
                    thoiGianSuDung + " phut", format.format(tienDichVu), format.format(tongTien),
                    format.format(donGia), format.format(tienSuDungPhong), dsCTHD);

            // Hiển thị thông báo
            MessageDialogHelpers.showAlert(Alert.AlertType.INFORMATION, "Thong bao", "Hoa don da duoc in");

        } else {
            MessageDialogHelpers.showAlert(Alert.AlertType.WARNING, "Canh bao", "Chua chon phong de in hoa don");
        }
    }

    @FXML
    void btnThanhToan(ActionEvent event) {
        
        DonDatPhongDAO donDatPhongDAO = new DonDatPhongDAO();
        PhongDAO phongDAO = new PhongDAO();
        HoaDonDao hoaDonDao = new HoaDonDao();
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();

        if (row >= 0) {
            ChiTietHoaDon chiTietHD = phongSuDungData.get(row);
            if (chiTietHD.getGioRa() == null) {
                MessageDialogHelpers.showAlert(Alert.AlertType.WARNING, "Canh bao", "Ban phai nhan nut Tra Phong truoc khi thanh toan.");
                return;
            }

            Date thoiGianVao = (Date) colThoiGianVao.getCellData(row);
            int gioVao = thoiGianVao.getHours();
            int phutVao = thoiGianVao.getMinutes();
            int gioRaFinal = chiTietHD.getGioRa().getHour();
            int phutRaFinal = chiTietHD.getGioRa().getMinute();
            double donGia = colDongia.getCellData(row);
            double tienDichVu = lblTienDichVu.getText().equals("") ? 0 : Double.parseDouble(lblTienDichVu.getText().replaceAll(" VND", ""));

            int tongGio = gioRaFinal - gioVao;
            int tongPhut = phutRaFinal - phutVao;
            int thoiGianSuDung = tongGio * 60 + tongPhut;
            double tienSuDungPhong = thoiGianSuDung * (donGia / 60);
            double tongTien = tienSuDungPhong + tienDichVu;

            Locale locale = new Locale("vi", "VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            String pattern = "dd-MM-yyyy hh:mm a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            if (MessageDialogHelpers.showConfirmDialog(null, "Xac nhan", "Ban co chac muon thanh toan cho phong nay?") != 0) {
                return;
            }

            String maHoaDon = hoaDonDao.getMaHoaDon(lblKhachHang.getText());

            String thoiGianRa = dateFormat.format(new Date());
            String thoiGianVaoFormatted = dateFormat.format(thoiGianVao);
            String message = String.format(
                    "Khach hang: %s\nMa hoa don: %s\nThoi gian ra: %s\nThoi gian vao: %s\nThoi gian su dung: %d phut\nTien dich vu: %s\nTong tien: %s\nDon gia: %s\nTien su dung phong: %s",
                    lblKhachHang.getText(), maHoaDon, thoiGianRa, thoiGianVaoFormatted, thoiGianSuDung, format.format(tienDichVu),
                    format.format(tongTien), format.format(donGia), format.format(tienSuDungPhong));

            MessageDialogHelpers.showMessageDialog(null, "Thong tin thanh toan", message);

            donDatPhongDAO.updateTrangThaiDonDat_DaThanhToan(viewPhongDangSD.getItems().get(row).getHoaDon().getMaHoaDon());
            Phong p = phongDAO.getPhongTheoMa(viewPhongDangSD.getItems().get(row).getPhong().getMaPhong());
            p.setTrangThai("Trong");
            phongDAO.updatePhong(p);
            hoaDonDao.updateTongTienHoaDon(tongTien, maHoaDon);

        } else {
            MessageDialogHelpers.showAlert(Alert.AlertType.WARNING, "Canh bao", "Chua chon phong de thanh toan");
        }
        // Load lại dữ liệu bảng
        btnLamMoi(new ActionEvent());
    }
    

    @FXML
    void btnCapNhat(ActionEvent event) {
        int row = viewDichVu.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            ChiTietHoaDonDao chiTietHoaDonDao = new ChiTietHoaDonDao();
            HoaDonDao hoaDonDao = new HoaDonDao();

            StringBuilder sb = new StringBuilder();
            dataValidateThemTP(sb);

            if (sb.length() > 0) {
                Window window = viewPhongDangSD.getScene().getWindow();
                MessageDialogHelpers.showErrorDialog(window, "Loi", sb.toString());
                return;
            }

            String maHoaDon = hoaDonDao.getMaHoaDon(lblKhachHang.getText());
            String tenSanPham = colSanPham.getCellData(row);

            if (chiTietHoaDonDao.updateSoLuongCTHD(maHoaDon, Integer.parseInt(txtSoLuong.getText()), tenSanPham)) {
                Window window = viewDichVu.getScene().getWindow();
                MessageDialogHelpers.showMessageDialog(window, "Thong bao", "Cap nhat thanh cong");

                dichVuData.clear();
                loadDataCTHD();
                txtSoLuong.setText("");

                int rowPhong = viewPhongDangSD.getSelectionModel().getSelectedIndex();
                hienThiTongTien(rowPhong);
            } else {
                Window window = viewDichVu.getScene().getWindow();
                MessageDialogHelpers.showErrorDialog(window, "Loi", "Cap nhat khong thanh cong");
            }
        } else {
            Window window = viewDichVu.getScene().getWindow();
            MessageDialogHelpers.showErrorDialog(window, "Loi", "Phai chon 1 san pham truoc khi cap nhat");
        }
    }

    @FXML
    void btnLamMoi(ActionEvent event) {
        lamMoiText();
        cmbTenThucPham.getItems().clear();
        loadDataToCmbTenSanPham();

        // Xóa dữ liệu bảng dịch vụ
        dichVuData.clear();

        // Tải lại dữ liệu bảng phòng đang sử dụng từ cơ sở dữ liệu
        loadTablePhongDangSuDungTheoTrangThai("Chua Thanh Toan");

        // Cập nhật lại bảng hiển thị
        viewPhongDangSD.refresh();
    }
    
    @FXML
    void btnThemThucPham(ActionEvent event) {
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();

        if (row >= 0) {
            HoaDonDao hoaDonDao = new HoaDonDao();
            ChiTietHoaDon cthd = new ChiTietHoaDon();
            PhongDAO phongDao = new PhongDAO();
            ChiTietHoaDonDao cthdDao = new ChiTietHoaDonDao();

            StringBuilder sb = new StringBuilder();
            dataValidateThemTP(sb);
            if (sb.length() > 0) {
                Window window = viewPhongDangSD.getScene().getWindow();
                MessageDialogHelpers.showErrorDialog(window, "Loi", sb.toString());
                return;
            }

            try {
                String maHoaDon = hoaDonDao.getMaHoaDon(lblKhachHang.getText());
                HoaDon hoaDon = hoaDonDao.getHoaDonTheoMa(maHoaDon);

                cthd.setHoaDon(hoaDon);
                cthd.setPhong(phongDao.getPhongTheoMa(viewPhongDangSD.getItems().get(row).getPhong().getMaPhong()));
                SanPham sanPham = createSanPham();
                cthd.setSanPham(sanPham);

                String soLuongText = txtSoLuong.getText().trim();
                if (soLuongText.isEmpty()) {
                    Window window = viewDichVu.getScene().getWindow();
                    MessageDialogHelpers.showErrorDialog(window, "Loi", "So luong khong duoc de trong");
                    return;
                }

                int soLuong;
                try {
                    soLuong = Integer.parseInt(soLuongText);
                } catch (NumberFormatException e) {
                    Window window = viewDichVu.getScene().getWindow();
                    MessageDialogHelpers.showErrorDialog(window, "Loi", "So luong phai la so");
                    return;
                }

                cthd.setSoLuong(soLuong);

                if (cthdDao.checkExist(maHoaDon, sanPham.getTenSanPham())) {
                    Window window = viewDichVu.getScene().getWindow();
                    MessageDialogHelpers.showErrorDialog(window, "Canh bao", "San pham da ton tai");
                    return;
                } else {
                    if (cthdDao.addChiTietHoaDon(cthd)) {
                        Window window = viewDichVu.getScene().getWindow();
                        MessageDialogHelpers.showMessageDialog(window, "Thong bao", "Them thanh cong");
                        dichVuData.clear();
                        loadDataCTHD();
                        txtSoLuong.setText("");

                        hienThiTongTien(row);
                    } else {
                        Window window = viewDichVu.getScene().getWindow();
                        MessageDialogHelpers.showErrorDialog(window, "Loi", "Them khong thanh cong");
                    }
                }
            } catch (Exception e) {
                Window window = viewDichVu.getScene().getWindow();
                MessageDialogHelpers.showErrorDialog(window, "Loi", "Co loi xay ra: " + e.getMessage());
            }
        } else {
            Window window = viewPhongDangSD.getScene().getWindow();
            MessageDialogHelpers.showErrorDialog(window, "Loi", "Phai chon 1 phong truoc khi them");
        }
    }
    
    @FXML
    void btnXoa(ActionEvent event) {
        int row = viewDichVu.getSelectionModel().getSelectedIndex();

        if (row >= 0) {
            ChiTietHoaDonDao chiTietHoaDonDao = new ChiTietHoaDonDao();
            HoaDonDao hoaDonDao = new HoaDonDao();

            String maHoaDon = hoaDonDao.getMaHoaDon(lblKhachHang.getText());
            String tenSanPham = colSanPham.getCellData(row);

            if (chiTietHoaDonDao.deleteCTHD(maHoaDon, tenSanPham)) {
                Window window = viewDichVu.getScene().getWindow();
                MessageDialogHelpers.showMessageDialog(window, "Thong bao", "Xoa thanh cong");

                dichVuData.clear();
                loadDataCTHD();

                int rowPhong = viewPhongDangSD.getSelectionModel().getSelectedIndex();
                hienThiTongTien(rowPhong);
            } else {
                Window window = viewDichVu.getScene().getWindow();
                MessageDialogHelpers.showErrorDialog(window, "Loi", "Xoa khong thanh cong");
            }
        } else {
            Window window = viewDichVu.getScene().getWindow();
            MessageDialogHelpers.showErrorDialog(window, "Loi", "Phai chon 1 san pham truoc khi xoa");
        }
    }

    @FXML
    void btnXemDanhSachHD(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogDanhSachHoaDon.fxml"));
            Scene scene = new Scene(loader.load());

            // Create the stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Danh sach hoa don");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            // Show the dialog and wait for it to be closed
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }

    @FXML
    void cmbLoaiThucPham(ActionEvent event) {
        cmbLoaiThucPhamActionPerformed(event);
    }

    @FXML
    void cmbTenThucPham(ActionEvent event) {
        // Implementation for cmbTenThucPham
    }

    @FXML
    void cmbTimKiem(ActionEvent event) {
        // Implementation for cmbTimKiem
    }

    @FXML
    void tblDichVu(MouseEvent event) {
        int row = viewDichVu.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            SanPhamDAO sanPhamDao = new SanPhamDAO();
            String tenSanPham = colSanPham.getCellData(row);
            SanPham sanPham = sanPhamDao.getSanPhamTheoTen(tenSanPham);

            cmbLoaiThucPham.getSelectionModel().select(sanPham.getLoaiSanPham().getTenLoaiSP());
            cmbTenThucPham.getSelectionModel().select(sanPham.getTenSanPham());
            txtSoLuong.setText(colSoLuong.getCellData(row).toString());
        }
    }

    @FXML
    void tblPhongDangSuDung(MouseEvent event) {
        int row = viewPhongDangSD.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            // Xóa dữ liệu hiện tại trong dichVuData (nếu cần thiết)
            dichVuData.clear();

            // Đổ dữ liệu đã chọn vào các label lblPhong, lblDonGiaPhong, lblKhachHang
            lblPhong.setText(phongSuDungData.get(row).getPhong().getTenPhong());
            lblDonGiaPhong.setText("Don gia phong: " + phongSuDungData.get(row).getPhong().getLoaiPhong().getDonGia());
            lblKhachHang.setText(phongSuDungData.get(row).getHoaDon().getTenKhachHang());

            // Tải dữ liệu chi tiết cho hàng đã chọn vào dichVuData
            loadDataCTHD(); 
            // Phương thức này sẽ điền dichVuData với các đối tượng ChiTietHoaDon tương ứng

            // Tính toán và hiển thị tổng hóa đơn dựa trên hàng đã chọn
            hienThiTongTien(row); 
            // Phương thức này sẽ tính tổng hóa đơn dựa trên dichVuData
        }
    }
    
    private void search(String str) {
        filteredData.setPredicate(item -> {
            String selectedCriteria = cmbTimKiem.getSelectionModel().getSelectedItem();
            if (selectedCriteria == null || selectedCriteria.isEmpty()) {
                return true; // If no filter criteria is selected, show all items
            }

            String lowerCaseFilter = str.toLowerCase();
            switch (selectedCriteria) {
                case "Tim theo ma phong":
                    return item.getPhong().getMaPhong().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo ten phong":
                    return item.getPhong().getTenPhong().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo loai phong":
                    return item.getPhong().getLoaiPhong().getTenLoaiPhong().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo don gia":
                    return String.valueOf(item.getPhong().getLoaiPhong().getDonGia()).toLowerCase().contains(lowerCaseFilter);
                case "Tim theo thoi gian vao":
                    return item.getHoaDon().getNgayTao().toString().toLowerCase().contains(lowerCaseFilter);
                case "Tim theo khach hang":
                    return item.getHoaDon().getTenKhachHang().toLowerCase().contains(lowerCaseFilter);
                default:
                    return true;
            }
        });
    }
    
    private void handleKeyReleased(KeyEvent e) {
        String searchStr = txtTimPhong.getText();
        search(searchStr);
    }
}
