package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import connectDB.MSSQLConnection;
import entity.KhachHang;
import entity.Phong;
import entity.DonDatPhong;

public class DonDatPhongDAO {

    public boolean addThongTinDatPhong(DonDatPhong donDatPhong) {
        String sql = "INSERT INTO [dbo].[Room_reservation] ([CheckinTime], [CustomerID], [RoomID], [OrderStatus])"
                + " VALUES(?,?,?,?)";
        int rs = 0;
        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {

            LocalDateTime ldt = LocalDateTime.of(1900 + donDatPhong.getThoiGianVao().getYear(),
                    1 + donDatPhong.getThoiGianVao().getMonth(), donDatPhong.getThoiGianVao().getDate(),
                    donDatPhong.getThoiGianVao().getHours(), donDatPhong.getThoiGianVao().getMinutes(),
                    donDatPhong.getThoiGianVao().getSeconds());
            prepareStatement.setTimestamp(1, Timestamp.valueOf(ldt));
            String mkh = donDatPhong.getKhachHang().getMaKhachHang().replaceAll("KH", "");
            prepareStatement.setInt(2, Integer.parseInt(mkh));
            String mp = donDatPhong.getPhong().getMaPhong().replaceAll("MP", "");
            prepareStatement.setInt(3, Integer.parseInt(mp));
            prepareStatement.setString(4, donDatPhong.getTrangThaiDon());

            rs = prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs > 0;
    }

    public List<DonDatPhong> getDanhSachDonDatPhong() {
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        PhongDAO phongDAO = new PhongDAO();
        List<DonDatPhong> listdonDatPhong = new ArrayList<>();

        String sql = "SELECT * FROM Room_reservation";
        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql);
             ResultSet rs = prepareStatement.executeQuery()) {

            while (rs.next()) {
                DonDatPhong ttdp = new DonDatPhong();

                int maTTDP = rs.getInt(1);
                String maTTDatPhong = "DP" + maTTDP;
                Timestamp thoiGianVao = rs.getTimestamp(2);

                KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString(3));
                Phong phong = phongDAO.getPhongTheoMa(rs.getString(4));
                String trangThaiPhong = rs.getString(5);

                ttdp.setMaDatPhong(maTTDatPhong);
                ttdp.setThoiGianVao(thoiGianVao);
                ttdp.setKhachHang(khachHang);
                ttdp.setPhong(phong);
                ttdp.setTrangThaiDon(trangThaiPhong);

                listdonDatPhong.add(ttdp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listdonDatPhong;
    }

    public boolean updateTrangThaiDonDat_Huy(String maDonDat) {
        String sql = "UPDATE Room_reservation SET OrderStatus = N'Huy Don' WHERE BookingCode = ?";
        int rs = 0;

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {

            String stt = maDonDat.replaceAll("DP", "");
            prepareStatement.setInt(1, Integer.parseInt(stt));

            rs = prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs > 0;
    }

    public boolean updateTrangThaiDonDat_DaThanhToan(String maDonDat) {
        String sql = "UPDATE Room_reservation SET OrderStatus = N'Da Thanh Toan' WHERE BookingCode= ?";
        int rs = 0;

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {

            String stt = maDonDat.replaceAll("DP", "");
            prepareStatement.setInt(1, Integer.parseInt(stt));

            rs = prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs > 0;
    }

    public boolean updateTrangThaiDonDat_ChoThanhToan(String maDatPhong) {
        String sql = "UPDATE Room_reservation SET OrderStatus = N'Chua Thanh Toan', CheckinTime = ? WHERE BookingCode = ?";
        int rs = 0;

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {

            String stt = maDatPhong.replaceAll("DP", "");
            prepareStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            prepareStatement.setInt(2, Integer.parseInt(stt));

            rs = prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs > 0;
    }

    public DonDatPhong getDonDatPhongTheoMa(String maDP) {
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        PhongDAO phongDAO = new PhongDAO();
        DonDatPhong ddp = new DonDatPhong();
        String sql = "SELECT * FROM Room_reservation WHERE BookingCode = ?";
        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {
            String stt = maDP.replaceAll("DP", "");
            prepareStatement.setInt(1, Integer.parseInt(stt));
            try (ResultSet rs = prepareStatement.executeQuery()) {
                while (rs.next()) {
                    ddp = new DonDatPhong();
                    int maTTDP = rs.getInt(1);
                    String maTTDatPhong = "DP" + maTTDP;
                    Timestamp thoiGianVao = rs.getTimestamp(2);
                    KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString(3));
                    Phong phong = phongDAO.getPhongTheoMa(rs.getString(4));
                    String trangThaiPhong = rs.getString(5);

                    ddp.setMaDatPhong(maTTDatPhong);
                    ddp.setThoiGianVao(thoiGianVao);
                    ddp.setKhachHang(khachHang);
                    ddp.setPhong(phong);
                    ddp.setTrangThaiDon(trangThaiPhong);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ddp;
    }

    public List<DonDatPhong> getDanhSachDonDatPhong(String trangThaiDon) {
        List<DonDatPhong> dsDonDatPhong = new ArrayList<>();
        String sql = "SELECT * FROM Room_reservation WHERE OrderStatus = ?";

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql)) {
            prepareStatement.setString(1, trangThaiDon);
            try (ResultSet rs = prepareStatement.executeQuery()) {
                KhachHangDAO khachHangDAO = new KhachHangDAO();
                PhongDAO phongDAO = new PhongDAO();

                while (rs.next()) {
                    DonDatPhong ddp = new DonDatPhong();
                    int maTTDP = rs.getInt(1);
                    String maTTDatPhong = "DP" + maTTDP;
                    Timestamp thoiGianVao = rs.getTimestamp(2);
                    KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString(3));
                    Phong phong = phongDAO.getPhongTheoMa(rs.getString(4));
                    String trangThaiPhong = rs.getString(5);

                    ddp.setMaDatPhong(maTTDatPhong);
                    ddp.setThoiGianVao(thoiGianVao);
                    ddp.setKhachHang(khachHang);
                    ddp.setPhong(phong);
                    ddp.setTrangThaiDon(trangThaiPhong);

                    dsDonDatPhong.add(ddp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDonDatPhong;
    }

    public List<DonDatPhong> getDanhSachDonDatTruoc() {
        List<DonDatPhong> dsDonDatPhong = new ArrayList<>();
        String sql = "SELECT * FROM Room_reservation WHERE OrderStatus = N'Dat Truoc' OR OrderStatus = N'Hủy Đơn'";

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql);
             ResultSet rs = prepareStatement.executeQuery()) {

            KhachHangDAO khachHangDAO = new KhachHangDAO();
            PhongDAO phongDAO = new PhongDAO();

            while (rs.next()) {
                DonDatPhong ddp = new DonDatPhong();
                int maTTDP = rs.getInt(1);
                String maTTDatPhong = "DP" + maTTDP;
                Timestamp thoiGianVao = rs.getTimestamp(2);
                KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString(3));
                Phong phong = phongDAO.getPhongTheoMa(rs.getString(4));
                String trangThaiPhong = rs.getString(5);

                ddp.setMaDatPhong(maTTDatPhong);
                ddp.setThoiGianVao(thoiGianVao);
                ddp.setKhachHang(khachHang);
                ddp.setPhong(phong);
                ddp.setTrangThaiDon(trangThaiPhong);

                dsDonDatPhong.add(ddp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDonDatPhong;
    }

    public List<DonDatPhong> getDanhSachDonThuePhong() {
        List<DonDatPhong> dsDonDatPhong = new ArrayList<>();
        String sql = "SELECT * FROM Room_reservation WHERE OrderStatus != N'Dat Truoc'";

        try (Connection con = MSSQLConnection.getJDBCConnection();
             PreparedStatement prepareStatement = con.prepareStatement(sql);
             ResultSet rs = prepareStatement.executeQuery()) {

            KhachHangDAO khachHangDAO = new KhachHangDAO();
            PhongDAO phongDAO = new PhongDAO();

            while (rs.next()) {
                DonDatPhong ddp = new DonDatPhong();
                int maTTDP = rs.getInt(1);
                String maTTDatPhong = "DP" + maTTDP;
                Timestamp thoiGianVao = rs.getTimestamp(2);
                KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString(3));
                Phong phong = phongDAO.getPhongTheoMa(rs.getString(4));
                String trangThaiPhong = rs.getString(5);

                ddp.setMaDatPhong(maTTDatPhong);
                ddp.setThoiGianVao(thoiGianVao);
                ddp.setKhachHang(khachHang);
                ddp.setPhong(phong);
                ddp.setTrangThaiDon(trangThaiPhong);

                dsDonDatPhong.add(ddp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDonDatPhong;
    }
}
