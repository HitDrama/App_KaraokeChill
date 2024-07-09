
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.MSSQLConnection;
import entity.KhachHang;

public class KhachHangDAO {

	/**
	 * lấy tất cả khách hàng hiện có trong csdl
	 * 
	 * @return danh sách sản phẩm
	 */
	public List<KhachHang> getDanhSachKhachHang() {
		List<KhachHang> listKhachHang = new ArrayList<KhachHang>();
		String sql = "SELECT * FROM Customer";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				int ma = rs.getInt(1);
				String maKhachHang = "KH" + ma;
				String tenKhachHang = rs.getString(2);
				String soDT = rs.getString(3);
				int soLanDen = rs.getInt(5);
				String loaiKhachHang = rs.getString(4);

				KhachHang khachHang = new KhachHang(maKhachHang, tenKhachHang, soDT, soLanDen, loaiKhachHang);

				listKhachHang.add(khachHang);
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
		return listKhachHang;
	}

	public boolean addKhachHang(KhachHang khachHang) {
		String sql = "INSERT INTO [dbo].[Customer] ([CustomerName], [Phone] ,[CustomerType],[VisitCount])"
				+ " VALUES(?,?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, khachHang.getHoTenKH());
			prepareStatement.setString(2, khachHang.getSoDienThoai());

			String loaiKhachHang = "Khach Hang Tiem Nang";
			prepareStatement.setString(3, loaiKhachHang);

			prepareStatement.setInt(4, 1);

			rs = prepareStatement.executeUpdate();
		} catch (SQLException e) {
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

	/**
	 * Lấy 1 khách hàng theo số điện thoại
	 * 
	 * @param soDienThoai
	 * @return KhachHang
	 */
	public KhachHang getKhachHangTheoSĐT(String soDienThoai) {
		KhachHang khachHang = new KhachHang();
		String sql = "SELECT * FROM Customer" + " where Phone = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, soDienThoai);

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				khachHang.setMaKhachHang("KH" + rs.getString(1));
				khachHang.setHoTenKH(rs.getString(2));
				khachHang.setLoaiKhachHang(rs.getString(4));
				khachHang.setSoDienThoai(soDienThoai);
				khachHang.setSoLanDen(rs.getInt("VisitCount"));
				return khachHang;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Lấy 1 khách hàng theo mã khách hàng
	 * 
	 * @param maKhachHang
	 * @return KhachHang
	 */
	public KhachHang getKhachHangTheoMa(String maKhachHang) {
		KhachHang khachHang = new KhachHang();
		String sql = "SELECT * FROM Customer" + " where CustomerID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			String maKH = maKhachHang.replaceAll("KH", "");
			prepareStatement.setInt(1, Integer.parseInt(maKH));

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				khachHang.setMaKhachHang("KH" + rs.getString(1));
				khachHang.setHoTenKH(rs.getString(2));
				khachHang.setSoDienThoai(rs.getString(3));
				khachHang.setLoaiKhachHang(rs.getString(4));
				khachHang.setSoLanDen(rs.getInt(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return khachHang;
	}

	/**
	 * update thông tin cho 1 khách hàng
	 * 
	 * @param khachHang khách hàng phẩm cần update
	 * @return
	 */
	public boolean updateKhachHang(KhachHang khachHang) {
		String sql = "UPDATE Customer SET CustomerName=?,Phone=?" + " where CustomerID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, khachHang.getHoTenKH());
			prepareStatement.setString(2, khachHang.getSoDienThoai());

			String maKhachHang = khachHang.getMaKhachHang();
			String stt = maKhachHang.replaceAll("KH", "");
			prepareStatement.setInt(3, Integer.parseInt(stt));

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
	
	public boolean updateLoaiKhachHang(KhachHang khachHang) {
		String sql = "UPDATE Customer SET CustomerType = ?" + " where CustomerID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, khachHang.getLoaiKhachHang());
			String maKhachHang = khachHang.getMaKhachHang();
			String stt = maKhachHang.replaceAll("KH", "");
			prepareStatement.setInt(2, Integer.parseInt(stt));

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
	
	/**
	 * update thông tin cho 1 khách hàng
	 * 
	 * @param khachHang khách hàng phẩm cần update
	 * @return
	 */
	public boolean updateSoLanDen(KhachHang khachHang) {
		String sql = "UPDATE Customer SET VisitCount = ? where CustomerID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			int soLanDen = khachHang.getSoLanDen() + 1;
			prepareStatement.setInt(1, soLanDen);

			String maKhachHang = khachHang.getMaKhachHang();
			String stt = maKhachHang.replaceAll("KH", "");
			prepareStatement.setInt(2, Integer.parseInt(stt));

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

	public boolean checkExist(String sdt) {
		boolean check = false;
		String sql = "select * from Customer where Phone = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, sdt);
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
	
	/**
	 * Lấy 1 khách hàng theo số điện thoại
	 * 
	 * @param soDienThoai
	 * @return KhachHang
	 */
	public KhachHang getKhachHangTheoTen(String tenKH) {
		KhachHang khachHang = new KhachHang();
		String sql = "SELECT * FROM Customer" + " where  = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, tenKH);

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				khachHang.setMaKhachHang("KH" + rs.getString(1));
				khachHang.setHoTenKH(rs.getString(2));
				khachHang.setLoaiKhachHang(rs.getString(4));
				khachHang.setSoDienThoai("");
				return khachHang;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				prepareStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
