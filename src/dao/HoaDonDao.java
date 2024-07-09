
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import connectDB.MSSQLConnection;
import entity.HoaDon;
import entity.NhanVien;
import helpers.ShareData;

public class HoaDonDao {
	/**
	 * 
	 * @param HoaDon
	 * @return
	 */
	public boolean addHoaDon(HoaDon hoaDon) {
		String sql = "INSERT INTO [dbo].[Invoice] ([EmployeeID],[CustomerName],[CreationDate])" + " VALUES(?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			NhanVien nhanVien = ShareData.taiKhoanDangNhap.getNhanVien();

			String maNV = nhanVien.getMaNhanVien().replaceAll("NV", "");

			prepareStatement.setInt(1, Integer.parseInt(maNV));
			prepareStatement.setString(2, hoaDon.getTenKhachHang());

			java.util.Date date = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			prepareStatement.setDate(3, sqlDate);

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

	public String getMaHoaDon(String tenKhachHang) {
		String sql = "SELECT Invoice.InvoiceID from Invoice\r\n" + "where Invoice.CustomerName = ?";

		String maHoaDon = null;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenKhachHang);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				int maHD = rs.getInt("InvoiceID");
				maHoaDon = "HD" + maHD;
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
		return maHoaDon;
	}

	public HoaDon getHoaDonTheoMa(String maHoaDon) {
		String sql = "select * from Invoice where InvoiceID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		HoaDon hoaDon = new HoaDon();

		try {

			prepareStatement = con.prepareStatement(sql);

			int maHD = Integer.parseInt(maHoaDon.replaceAll("HD", ""));

			prepareStatement.setInt(1, maHD);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				hoaDon.setMaHoaDon(maHoaDon);

				String maNhanVien = "NV" + rs.getInt("EmployeeID");
				NhanVienDAO nvDao = new NhanVienDAO();
				NhanVien nhanVien = nvDao.getNhanVienTheoMa(maNhanVien);

				hoaDon.setNhanVien(nhanVien);

				hoaDon.setTenKhachHang(rs.getString("CustomerName"));
				hoaDon.setNgayTao(rs.getDate("CreationDate"));
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
		return hoaDon;
	}
	
	public boolean updateTongTienHoaDon(double tongTien, String maHD) {
		String sql = "UPDATE Invoice SET TotalAmount = ?" + " where InvoiceID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setDouble(1, tongTien);
			prepareStatement.setInt(2, Integer.parseInt(maHD.replaceAll("HD", "")));

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
	
	public List<HoaDon> getDSHoaDon(NhanVien nv) {
		List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
		String sql = "select * from Invoice where EmployeeID = ? and TotalAmount != 0";
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);
			
			int maNV = Integer.parseInt(nv.getMaNhanVien().replaceAll("NV", ""));

			prepareStatement.setInt(1, maNV);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				HoaDon hoaDon = new HoaDon();
				hoaDon.setMaHoaDon(rs.getString("InvoiceID"));
				
				hoaDon.setNhanVien(nv);
				
				hoaDon.setNgayTao(rs.getDate("CreationDate"));
				
				hoaDon.setTenKhachHang(rs.getString("CustomerName"));
				hoaDon.setTongTien(rs.getDouble("TotalAmount"));
				dsHoaDon.add(hoaDon);
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
		return dsHoaDon;
	}
	
	public List<HoaDon> getDSHoaDon() {
		List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
		NhanVienDAO nvDao = new NhanVienDAO();
		String sql = "select * from Invoice where TotalAmount != 0";
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = con.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				HoaDon hoaDon = new HoaDon();
				hoaDon.setMaHoaDon(rs.getString("InvoiceID"));
				
				int maNV = rs.getInt("EmployeeID");
				NhanVien nv = nvDao.getNhanVienTheoMa("NV"+maNV);
				hoaDon.setNhanVien(nv);
				
				hoaDon.setNgayTao(rs.getDate("CreationDate"));
				hoaDon.setTenKhachHang(rs.getString("CustomerName"));
				hoaDon.setTongTien(rs.getDouble("TotalAmount"));
				
				dsHoaDon.add(hoaDon);
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
		return dsHoaDon;
	}
	
	public List<HoaDon> getDSHoaDonTheoNgay(Date from, Date to) {
		List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
		NhanVienDAO nvDao = new NhanVienDAO();
		
		String sql = "select * from Invoice where CreationDate  BETWEEN ? AND ?";
		
		java.sql.Date startDate = new java.sql.Date(from.getTime());
		java.sql.Date endDate = new java.sql.Date(to.getTime());
		
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setDate(1,  startDate);
			prepareStatement.setDate(2,  endDate);
			
			ResultSet rs = prepareStatement.executeQuery();
			
			while (rs.next()) {
				HoaDon hoaDon = new HoaDon();
				hoaDon.setMaHoaDon(rs.getString("InvoiceID"));
				
				int maNV = rs.getInt("EmployeeID");
				NhanVien nv = nvDao.getNhanVienTheoMa("NV"+maNV);
				hoaDon.setNhanVien(nv);
				
				hoaDon.setNgayTao(rs.getDate("CreationDate"));
				hoaDon.setTenKhachHang(rs.getString("CustomerName"));
				hoaDon.setTongTien(rs.getDouble("TotalAmount"));
				
				dsHoaDon.add(hoaDon);
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
		return dsHoaDon;
	}
}
