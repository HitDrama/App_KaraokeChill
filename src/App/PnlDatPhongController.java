/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import helpers.ShareData;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Chi
 */
public class PnlDatPhongController implements Initializable {

    @FXML
    private TableView<Phong> TableViewDSPhong;

    @FXML
    private TableView<DonDatPhong> TableViewDonDatPhong;

    @FXML
    private ComboBox<String> cmbTimDon;

    @FXML
    private ComboBox<String> cmb_LocPhongTheoTrangThai;

    @FXML
    private TableColumn<Phong, Double> colDonGia;

    @FXML
    private TableColumn<DonDatPhong, String> colKhachHang;

    @FXML
    private TableColumn<Phong, String> colLoaiPhong;

    @FXML
    private TableColumn<DonDatPhong, String> colLoaiPhong2;

    @FXML
    private TableColumn<DonDatPhong, String> colMaDatPhong;

    @FXML
    private TableColumn<Phong, String> colMaPhong;

    @FXML
    private TableColumn<DonDatPhong, String> colSoDienThoai;

    @FXML
    private TableColumn<Phong, String> colTenPhong;

    @FXML
    private TableColumn<DonDatPhong, String> colTenPhong2;

    @FXML
    private TableColumn<DonDatPhong, String> colThoiGianVao;

    @FXML
    private TableColumn<DonDatPhong, String> colTinhTrang;

    @FXML
    private TableColumn<DonDatPhong, String> colTrangThai;

    @FXML
    private ImageView imgthem;

    @FXML
    private ImageView imgthem1;

    @FXML
    private ImageView imgthem2;

    @FXML
    private ImageView imgthem21;

    @FXML
    private AnchorPane mainfrom;
    
    @FXML
    private Button nutLamMoi;

    @FXML
    private Button nutThueNgay;

    @FXML
    private Button nutTim;

    @FXML
    private Button nutDatTruoc;

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

    @FXML
    void btn_DatTruoc(ActionEvent event) {
        try {
        // Tạo FXMLLoader để tải FXML của form PnlDatTruoc
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PnlDatTruoc.fxml"));
            Parent root = loader.load(); // Tải FXML và tạo ra đối tượng Parent

            // Tạo một Scene mới với đối tượng Parent
            Scene scene = new Scene(root);

            // Tạo Stage mới (cửa sổ) và cài đặt cho nó
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Form Dat Truoc"); // Đặt tiêu đề cho cửa sổ

            // Hiển thị cửa sổ
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restTable(){
        danhSachPhong.clear();
        loadDataToTablePhong();
        danhSachDonDatPhong.clear();
        loadDataToTableDonThuePhong();
    }
    public void restTxt(){
        txtTimDon.clear();
        txt_LoaiKH.clear();
        txt_MaKH.clear();
        txt_TenKH.clear();
        txt_TimSDT.clear();
    }
    @FXML
    void btn_LamMoi(ActionEvent event) {
        restTable();
        restTxt();
    }
    private Window mainFrame;
    
    @FXML
    void btn_ThueNgay(ActionEvent event) {
        try {
            DonDatPhongDAO ddpDAO = new DonDatPhongDAO();
            HoaDonDao hoaDonDao = new HoaDonDao();
            KhachHangDAO khDao = new KhachHangDAO();

            // Tạo đối tượng DonDatPhong từ các thông tin được nhập vào
            DonDatPhong ddp = createThongTinDatPhong();

            // Tạo đối tượng HoaDon và đặt thông tin
            HoaDon hoaDon = new HoaDon();
            hoaDon.setTenKhachHang(ddp.getKhachHang().getHoTenKH()); // Đặt tên khách hàng từ đối tượng DonDatPhong
            hoaDon.setNhanVien(ShareData.taiKhoanDangNhap.getNhanVien()); // Nếu cần đặt nhân viên từ dữ liệu đã chia sẻ

            // Thêm hóa đơn vào cơ sở dữ liệu
            hoaDonDao.addHoaDon(hoaDon);

            // Thêm đơn đặt phòng vào cơ sở dữ liệu và xử lý kết quả
            if (ddpDAO.addThongTinDatPhong(ddp)) {
                MessageDialogHelpers.showMessageDialog(mainFrame, "Thong bao", "Thue phong thanh cong");
                khDao.updateSoLanDen(ddp.getKhachHang());
                restTable();
                restTxt();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    private final ObservableList<DonDatPhong> dfDanhSachDonDatPhong = FXCollections.observableArrayList();

    private final ObservableList<DonDatPhong> dfDanhSachPhong = FXCollections.observableArrayList();
    
    private DonDatPhong createThongTinDatPhong() {
        DonDatPhong donDatPhong = new DonDatPhong();
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        PhongDAO phongDAO = new PhongDAO();

        // Lấy thông tin khách hàng từ số điện thoại nhập vào
        KhachHang kh = khachHangDAO.getKhachHangTheoSĐT(txt_TimSDT.getText());

        // Lấy phòng đã chọn từ TableViewDSPhong
        Phong selectedPhong = TableViewDSPhong.getSelectionModel().getSelectedItem();

        if (selectedPhong != null) {
            // Cập nhật trạng thái phòng và lưu vào cơ sở dữ liệu
            selectedPhong.setTrangThai("Dang Su Dung");
            phongDAO.updatePhong(selectedPhong);

            // Thiết lập thông tin cho đơn đặt phòng
            donDatPhong.setPhong(selectedPhong);
            donDatPhong.setKhachHang(kh);
            donDatPhong.setThoiGianVao(new Date());
            donDatPhong.setTrangThaiDon("Chua Thanh Toan");
        } else {
            // Hiển thị thông báo nếu không có phòng nào được chọn
            MessageDialogHelpers.showErrorDialog(nutThueNgay.getScene().getWindow(), "Canh bao", "Chon 1 phong de thue");
        }

        return donDatPhong;
    }


    @FXML
    void btn_Tim(ActionEvent event) {
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        KhachHang kh = khachHangDAO.getKhachHangTheoSĐT(txt_TimSDT.getText());
        if (kh != null) {
            txt_MaKH.setText(kh.getMaKhachHang());
            txt_TenKH.setText(kh.getHoTenKH());
            txt_LoaiKH.setText(kh.getLoaiKhachHang());
            txt_TimSDT.setText(kh.getSoDienThoai());
        } else {
            showDialogChuaCoKhachHang();
        }
    }
    
    private void showDialogChuaCoKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogChuaCoKhachHang.fxml"));
            Parent parent = loader.load();

            // Get the controller instance
            DialogChuaCoKhachHangController controller = loader.getController();

            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.setTitle("Thông Báo");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private ObservableList<Phong> danhSachPhong = FXCollections.observableArrayList();
    private ObservableList<DonDatPhong> danhSachDonDatPhong = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTablePhong();
        loadDataToTablePhong();
        initTableDonDatPhong();
        loadDataToTableDonThuePhong(); // Chỉ gọi một lần trong phương thức initialize

        txtTimDon.textProperty().addListener((observable, oldValue, newValue) -> search(newValue));

        cmbTimDon.getItems().addAll(
        "Tim theo ten khach hang",
        "Tim theo ngay đat",
        "Tim theo ten phong",
        "Tim theo so dien thoai"
        );

        cmb_LocPhongTheoTrangThai.getItems().addAll(
        "Da Thanh Toan",
        "Chua Thanh Toan",
        "Dat Truoc"
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
    
    private void initTablePhong() {
        colMaPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhong()));
        colTenPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTenPhong()));
        colLoaiPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLoaiPhong().getTenLoaiPhong()));
        colDonGia.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getLoaiPhong().getDonGia()));
        colTinhTrang.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        TableViewDSPhong.setItems(danhSachPhong);
    }

    private void loadDataToTablePhong() {
        try {
            PhongDAO phongDAO = new PhongDAO();
            List<Phong> listPhong = phongDAO.getDanhSachPhongTheoTinhTrang("Trong");

            danhSachPhong.addAll(listPhong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initTableDonDatPhong() {
        colMaDatPhong.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMaDatPhong()));
        colKhachHang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKhachHang().getHoTenKH()));
        colLoaiPhong2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhong().getLoaiPhong().getTenLoaiPhong()));
        colTenPhong2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhong().getTenPhong()));
        colSoDienThoai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKhachHang().getSoDienThoai()));
        colThoiGianVao.setCellValueFactory(new PropertyValueFactory<>("thoiGianVao"));
        colTrangThai.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTrangThaiDon()));
        
        TableViewDonDatPhong.setItems(danhSachDonDatPhong);
    }

    private void loadDataToTableDonThuePhong() {
        try {
            DonDatPhongDAO donDatPhongDAO = new DonDatPhongDAO();
            List<DonDatPhong> listDonDatPhong = donDatPhongDAO.getDanhSachDonDatPhong();

            // Xóa danh sách cũ và thêm danh sách mới
            danhSachDonDatPhong.clear();
            danhSachDonDatPhong.addAll(listDonDatPhong);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
