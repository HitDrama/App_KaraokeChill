package App;

import dao.LoaiPhongDAO;
import dao.PhongDAO;
import entity.LoaiPhong;
import entity.Phong;
import helpers.MessageDialogHelpers;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.swing.JOptionPane;

public class PnlQuanLyPhongController implements Initializable {

    @FXML
    private Button btn_CapNhat;

    @FXML
    private Button btn_ThemPhong;

    @FXML
    private Button btn_XoaPhong;

    @FXML
    private ComboBox<String> cmb_MaLoaiPhong;

    @FXML
    private ImageView imgthem;

    @FXML
    private AnchorPane mainfrom;

    @FXML
    private TableView<Phong> tableView;

    @FXML
    private TextField txt_DonGia;

    @FXML
    private TextField txt_MaPhong;

    @FXML
    private TextField txt_TenLoaiPhong;

    @FXML
    private TextField txt_TenPhong;

    @FXML
    private TextField txt_TrangThai;

    private ObservableList<Phong> phongData = FXCollections.observableArrayList();
    private Window mainFrame;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        loadDataToTable();
        loadLoaiPhongToComboBox();

        cmb_MaLoaiPhong.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
                    LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(newValue);
                    txt_TenLoaiPhong.setText(loaiPhong.getTenLoaiPhong());
                    txt_DonGia.setText(String.valueOf(loaiPhong.getDonGia()));
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phong>() {
            @Override
            public void changed(ObservableValue<? extends Phong> observable, Phong oldValue, Phong newValue) {
                if (newValue != null) {
                    cmb_MaLoaiPhong.setValue(newValue.getLoaiPhong().getMaLoaiPhong());
                    txt_MaPhong.setText(newValue.getMaPhong());
                    txt_TenPhong.setText(newValue.getTenPhong());
                    txt_TrangThai.setText(newValue.getTrangThai());
                }
            }
        });
    }

    public void initTable() {
        TableColumn<Phong, String> maPhongCol = new TableColumn<>("Room ID");
        maPhongCol.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        maPhongCol.setPrefWidth(100); // Set column width
        maPhongCol.setStyle("-fx-alignment: CENTER;"); // Center-align text

        TableColumn<Phong, String> tenPhongCol = new TableColumn<>("Room Name");
        tenPhongCol.setCellValueFactory(new PropertyValueFactory<>("tenPhong"));
        tenPhongCol.setPrefWidth(150); // Set column width
        tenPhongCol.setStyle("-fx-alignment: CENTER-LEFT;"); // Left-align text

        TableColumn<Phong, String> tenLoaiPhongCol = new TableColumn<>("Room Type Name");
        tenLoaiPhongCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Phong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Phong, String> param) {
                return new SimpleStringProperty(param.getValue().getLoaiPhong().getTenLoaiPhong());
            }
        });
        tenLoaiPhongCol.setPrefWidth(150); // Set column width
        tenLoaiPhongCol.setStyle("-fx-alignment: CENTER-LEFT;"); // Left-align text

        TableColumn<Phong, Double> donGiaCol = new TableColumn<>("Invoice");
        donGiaCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Phong, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Phong, Double> param) {
                return new SimpleObjectProperty<>(param.getValue().getLoaiPhong().getDonGia());
            }
        });
        donGiaCol.setPrefWidth(100); // Set column width
        donGiaCol.setStyle("-fx-alignment: CENTER-RIGHT;"); // Right-align text

        TableColumn<Phong, String> trangThaiCol = new TableColumn<>("Status");
        trangThaiCol.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        trangThaiCol.setPrefWidth(100); // Set column width
        trangThaiCol.setStyle("-fx-alignment: CENTER;"); // Center-align text

        tableView.getColumns().addAll(maPhongCol, tenPhongCol, tenLoaiPhongCol, donGiaCol, trangThaiCol);
        tableView.setItems(phongData);

    }

    public void loadDataToTable() {
        try {
            PhongDAO phongDAO = new PhongDAO();
            List<Phong> listPhong = phongDAO.getDanhSachPhong();
            phongData.clear();
            phongData.addAll(listPhong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tableView(ActionEvent event) {
        Phong selectedPhong = tableView.getSelectionModel().getSelectedItem();

        if (selectedPhong != null) {
            cmb_MaLoaiPhong.setValue(selectedPhong.getLoaiPhong().getMaLoaiPhong());
            txt_MaPhong.setText(selectedPhong.getMaPhong());
            txt_TenPhong.setText(selectedPhong.getTenPhong());
            txt_TrangThai.setText(selectedPhong.getTrangThai());
        }

    }

    @FXML
    void btn_CapNhat(ActionEvent event) {
        int row = tableView.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            int isCapNhat = MessageDialogHelpers.showConfirmDialog(mainFrame, "Warning",
                    "Are you sure you want to update this room?");
            if (isCapNhat == JOptionPane.NO_OPTION) {
                return;
            } else if (isCapNhat == JOptionPane.CLOSED_OPTION) {
                return;
            }

            try {
                Phong phong = createPhong();
                phong.setMaPhong(txt_MaPhong.getText());

                LoaiPhong loaiPhong = new LoaiPhong();
                loaiPhong.setMaLoaiPhong(cmb_MaLoaiPhong.getValue());
                loaiPhong.setTenLoaiPhong(txt_TenLoaiPhong.getText());
                loaiPhong.setDonGia(Double.parseDouble(txt_DonGia.getText()));

                LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
                PhongDAO phongDAO = new PhongDAO();

                if (phongDAO.updatePhong(phong)) {
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Information",
                            "Room updated successfully");
                    loadDataToTable();
                    lamMoiText();
                } else {
                    MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Update failed");
                }

                if (loaiPhongDAO.updateLoaiPhong(loaiPhong)) {
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Information",
                            "Room type updated successfully");
                    loadDataToTable();
                    lamMoiText();
                } else {
                    MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Update failed");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "An error occurred while updating room");
            }
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Please select a row in the table");
        }
        lamMoiText();
    }

    @FXML
    void btn_ThemPhong(ActionEvent event) {
        Phong phong = createPhong(); // Tạo đối tượng Phong với trạng thái "Trống"
        PhongDAO phongDAO = new PhongDAO();
        if (phongDAO.addPhong(phong)) {
            MessageDialogHelpers.showMessageDialog(mainFrame, "Thông báo", "Phòng đã thêm thành công");
            loadDataToTable();
            lamMoiText();
        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Lỗi", "Thêm không thành công");
        }
    }

    @FXML
    void btn_XoaPhong(ActionEvent event) {
        PhongDAO phongDAO = new PhongDAO();
        int row = tableView.getSelectionModel().getSelectedIndex();

        if (row >= 0) {
            Phong selectedPhong = tableView.getSelectionModel().getSelectedItem();
            String maPhong = selectedPhong.getMaPhong(); // Get room code from selected row

            int isXoa = MessageDialogHelpers.showConfirmDialog(mainFrame, "Warning", "Are you sure you want to delete this room?");
            if (isXoa == JOptionPane.NO_OPTION) {
                return;
            } else if (isXoa == JOptionPane.CLOSED_OPTION) {
                return;
            }

            try {
                if (phongDAO.deletePhong(maPhong)) {
                    MessageDialogHelpers.showMessageDialog(mainFrame, "Information", "Deleted successfully");
                    loadDataToTable();
                } else {
                    MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "Deletion failed");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                MessageDialogHelpers.showErrorDialog(mainFrame, "Error", "An error occurred while deleting room");
            }

        } else {
            MessageDialogHelpers.showErrorDialog(mainFrame, "Warning", "Please select a row to delete");
        }

    }

    public void lamMoiText() {
    txt_MaPhong.clear();
    txt_TenPhong.clear();
    txt_TenLoaiPhong.clear();
    txt_DonGia.clear();
    txt_TrangThai.clear();
    txt_TrangThai.setText("Trong"); // Set trạng thái là "Trống"
    cmb_MaLoaiPhong.setValue(null);
}

    private Phong createPhong() {
    Phong phong = new Phong();
    LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
    phong.setMaPhong(txt_MaPhong.getText());
    phong.setTenPhong(txt_TenPhong.getText());
    LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(cmb_MaLoaiPhong.getValue());
    phong.setLoaiPhong(loaiPhong);
    phong.setTrangThai("Trong"); // Set trạng thái là "Trống"
    return phong;
}

    public void loadLoaiPhongToComboBox() {
        try {
            LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
            List<LoaiPhong> listLP = loaiPhongDAO.getDanhSachLoaiPhong();
            for (LoaiPhong lp : listLP) {
                cmb_MaLoaiPhong.getItems().add(lp.getMaLoaiPhong());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
