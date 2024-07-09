
package dao;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import connectDB.MSSQLConnection;
import entity.NhanVien;

public class NhanVienDAO {

	/**
	 * Hàm dùng để insert 1 nhân viên vào cơ sở dữ liệu
	 * 
	 * @param nv tham số truyền vào là NhanVien
	 * @return trả về true nếu insert thành công trả về false nếu insert thất bại
	 * @throws IOException
	 */
	public boolean addNhanVien(NhanVien nv) {
		String sql = "INSERT INTO [dbo].[Staff] ([FullName], [Gender], [PhoneNumber], [Email], [Address], [DateOfBirth], [Image])"
				+ " VALUES(?,?,?,?,?,?,?)";
		int rs = 0;
		
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, nv.getHoTen());
			prepareStatement.setInt(2, nv.getGioiTinh());
			prepareStatement.setString(3, nv.getSoDT());
			prepareStatement.setString(4, nv.getEmail());
			prepareStatement.setString(5, nv.getDiaChi());
			prepareStatement.setString(6, nv.getNamSinh());

			if (nv.getHinh() != null) {
				Blob hinh = new SerialBlob(nv.getHinh());
				prepareStatement.setBlob(7, hinh);
			} else {
				Blob hinh = null;
				prepareStatement.setBlob(7, hinh);
			}

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
	 * update thông tin cho 1 nhân viên
	 * 
	 * @param nv nhân viên cần update
	 * @return
	 */
	public boolean updateNhanVien(NhanVien nv) {
		String sql = "UPDATE Staff SET FullName = ?, Gender = ?, PhoneNumber = ?, Email = ?, Address = ?, DateOfBirth = ?, Image = ?"
				+ " where EmployeeID = ?";
		int rs = 0;
		
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		
		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, nv.getHoTen());
			prepareStatement.setInt(2, nv.getGioiTinh());
			prepareStatement.setString(3, nv.getSoDT());
			prepareStatement.setString(4, nv.getEmail());
			prepareStatement.setString(5, nv.getDiaChi());
			prepareStatement.setString(6, nv.getNamSinh());

			if (nv.getHinh() != null) {
				Blob hinh = new SerialBlob(nv.getHinh());
				prepareStatement.setBlob(7, hinh);
			} else {
				Blob hinh = null;
				prepareStatement.setBlob(7, hinh);
			}

			String maNhanVien = nv.getMaNhanVien();

			String stt = maNhanVien.replaceAll("NV", "");

			prepareStatement.setInt(8, Integer.parseInt(stt));

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
	 * lấy tất cả nhân viên hiện có trong csdl
	 * 
	 * @return danh sách nhân viên
	 */
	public List<NhanVien> getDanhSachNhanVien() {
		List<NhanVien> listNhanVien = new ArrayList<NhanVien>();
		String sql = "SELECT * FROM Staff";
		
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		
		try {
			prepareStatement = con.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				NhanVien nv = createNV(rs);
				int ma = rs.getInt("EmployeeID");
				nv.setMaNhanVien("NV" + ma);

				listNhanVien.add(nv);
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
		return listNhanVien;
	}

	/**
	 * Lấy 1 nhân viên theo mã
	 * 
	 * @param maNV
	 * @return NhanVien
	 */
	public NhanVien getNhanVienTheoMa(String maNV) {
		NhanVien nv = new NhanVien();
		String sql = "SELECT * FROM Staff" + " where EmployeeID = ?";
		
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		
		
		try {
			prepareStatement = con.prepareStatement(sql);

			String stt = maNV.replaceAll("NV", "");
			prepareStatement.setInt(1, Integer.parseInt(stt));
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				nv = createNV(rs);
				nv.setMaNhanVien(maNV);
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
		return nv;
	}

	/**
	 * Tạo 1 nhân viên , truyền dữ liệu từ cơ sở dữ liệu
	 * 
	 * @param rs resultset truyền vào
	 * @return nhân viên
	 */
	private NhanVien createNV(final ResultSet rs) {
		NhanVien nv = new NhanVien();
		try {
			nv.setHoTen(rs.getString("FullName"));
			nv.setEmail(rs.getString("Email"));
			nv.setDiaChi(rs.getString("Address"));
			nv.setGioiTinh(rs.getInt("Gender"));
			nv.setNamSinh(rs.getString("DateOfBirth"));
			nv.setSoDT(rs.getString("PhoneNumber"));
			Blob blob = rs.getBlob("Image");
			if (blob != null) {
				nv.setHinh(blob.getBytes(1, (int) blob.length()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nv;
	}
	
	
	public boolean checkExist(String email, String sdt) {
		boolean check = false;
		String sql = "select * from Staff where Email = ? or PhoneNumber = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, email);
			prepareStatement.setString(2, sdt);
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
