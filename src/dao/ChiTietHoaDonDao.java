
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.MSSQLConnection;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Phong;
import entity.SanPham;

public class ChiTietHoaDonDao {
	/**
	 * 
	 * @param HoaDon
	 * @return
	 */
	public boolean addChiTietHoaDon(ChiTietHoaDon cthd) {
		String sql = "INSERT INTO [dbo].[Invoice_Details] ([InvoiceID],[ProductID],[Quantity],[RoomID])"
				+ " VALUES(?,?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			String maHD = cthd.getHoaDon().getMaHoaDon().replaceAll("HD", "");
			prepareStatement.setInt(1, Integer.parseInt(maHD));

			String maSanPham = cthd.getSanPham().getMaSanPham().replaceAll("SP", "");
			prepareStatement.setInt(2, Integer.parseInt(maSanPham));

			prepareStatement.setInt(3, cthd.getSoLuong());

			String maPhong = cthd.getPhong().getMaPhong().replaceAll("MP", "");
			prepareStatement.setInt(4, Integer.parseInt(maPhong));

			rs = prepareStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs > 0;
	}

	public List<ChiTietHoaDon> getdsCTHD(String maHoaDon) {
		List<ChiTietHoaDon> dsCTHD = new ArrayList<ChiTietHoaDon>();
		String sql = "SELECT * from Invoice_Details where InvoiceID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			int maHD = Integer.parseInt(maHoaDon.replaceAll("HD", ""));
			prepareStatement.setInt(1, maHD);

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				ChiTietHoaDon cthd = new ChiTietHoaDon();
				HoaDonDao hoaDonDao = new HoaDonDao();
				PhongDAO phongDao = new PhongDAO();
				SanPhamDAO sanPhamDao = new SanPhamDAO();

				HoaDon hoaDon = hoaDonDao.getHoaDonTheoMa(maHoaDon);

				String maPhong = "MP" + rs.getInt("RoomID");
				Phong phong = phongDao.getPhongTheoMa(maPhong);

				String maSanPham = "SP" + rs.getInt("ProductID");
				SanPham sanPham = sanPhamDao.getSanPhamTheoMa(maSanPham);

				cthd.setHoaDon(hoaDon);
				cthd.setPhong(phong);
				cthd.setSanPham(sanPham);
				cthd.setSoLuong(rs.getInt("Quantity"));

				dsCTHD.add(cthd);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dsCTHD;
	}

	public List<ChiTietHoaDon> getdsCTHD() {
		List<ChiTietHoaDon> dsCTHD = new ArrayList<ChiTietHoaDon>();
		String sql = "SELECT * from Invoice_Details";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = con.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				ChiTietHoaDon cthd = new ChiTietHoaDon();
				HoaDonDao hoaDonDao = new HoaDonDao();
				PhongDAO phongDao = new PhongDAO();
				SanPhamDAO sanPhamDao = new SanPhamDAO();

				int maHoaDon = rs.getInt("InvoiceID");

				HoaDon hoaDon = hoaDonDao.getHoaDonTheoMa("HD" + maHoaDon);

				String maPhong = "MP" + rs.getInt("RoomID");
				Phong phong = phongDao.getPhongTheoMa(maPhong);

				String maSanPham = "SP" + rs.getInt("ProductID");
				SanPham sanPham = sanPhamDao.getSanPhamTheoMa(maSanPham);

				cthd.setHoaDon(hoaDon);
				cthd.setPhong(phong);
				cthd.setSanPham(sanPham);
				cthd.setSoLuong(rs.getInt("Quantity"));

				dsCTHD.add(cthd);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dsCTHD;
	}

	public boolean updateSoLuongCTHD(String maHoaDon, int soLuongMoi, String tenSanPham) {
		String sql = "UPDATE Invoice_Details SET Quantity=?" + " where InvoiceID = ? and ProductID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setInt(1, soLuongMoi);

			int maHD = Integer.parseInt(maHoaDon.replaceAll("HD", ""));
			prepareStatement.setInt(2, maHD);

			SanPhamDAO sanPhamDao = new SanPhamDAO();
			SanPham sanPham = sanPhamDao.getSanPhamTheoTen(tenSanPham);
			int maSanPham = Integer.parseInt(sanPham.getMaSanPham().replaceAll("SP", ""));
			prepareStatement.setInt(3, maSanPham);

			rs = prepareStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rs > 0;
	}

	public boolean deleteCTHD(String maHoaDon, String tenSanPham) {
		String sql = "DELETE FROM Invoice_Details" + " where InvoiceID = ? AND ProductID = ?";
		int rs = 0;
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			int maHD = Integer.parseInt(maHoaDon.replaceAll("HD", ""));

			SanPhamDAO sanPhamDao = new SanPhamDAO();
			SanPham sanPham = sanPhamDao.getSanPhamTheoTen(tenSanPham);
			int maSanPham = Integer.parseInt(sanPham.getMaSanPham().replaceAll("SP", ""));

			prepareStatement.setInt(1, maHD);
			prepareStatement.setInt(2, maSanPham);

			rs = prepareStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs > 0;
	}

	public boolean checkExist(String maHoaDon, String tenSanPham) {
		boolean check = false;
		String sql = "select * from Invoice_Details where  InvoiceID = ? and ProductID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			int maHD = Integer.parseInt(maHoaDon.replaceAll("HD", ""));

			SanPhamDAO sanPhamDao = new SanPhamDAO();
			SanPham sanPham = sanPhamDao.getSanPhamTheoTen(tenSanPham);
			int maSanPham = Integer.parseInt(sanPham.getMaSanPham().replaceAll("SP", ""));

			prepareStatement.setInt(1, maHD);
			prepareStatement.setInt(2, maSanPham);

			ResultSet rs = prepareStatement.executeQuery();
			check = rs.next();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return check;
	}
}
