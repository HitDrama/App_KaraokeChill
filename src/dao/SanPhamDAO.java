
package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.MSSQLConnection;
import entity.LoaiSanPham;
import entity.SanPham;

public class SanPhamDAO {

	/**
	 * lấy tất cả sản phẩm hiện có trong csdl
	 * 
	 * @return danh sách sản phẩm
	 */
	public List<SanPham> getDanhSachSanPham() {
		LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();
		List<SanPham> listSanPham = new ArrayList<SanPham>();

		String sql = "SELECT * FROM Product";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				int ma = rs.getInt(1);
				String maSanPham = "SP" + ma;
				String tenSP = rs.getString(2);
				double giaTien = rs.getDouble(3);

				String maLoaiSanPham = rs.getString(4);
				LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamTheoMaLoai(maLoaiSanPham);

				SanPham sanPham = new SanPham(maSanPham, tenSP, giaTien);
				sanPham.setLoaiSanPham(loaiSanPham);

				listSanPham.add(sanPham);
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
		return listSanPham;
	}

	/**
	 * Insert 1 sản phẩm vào cơ sở dữ liệu
	 * 
	 * @param sp tham số truyền vào là SanPham
	 * @return trả về true nếu insert thành công trả về false nếu insert thất bại
	 * @throws IOException
	 */
	public boolean addSanPham(SanPham sanPham) {
		String sql = "INSERT INTO [dbo].[Product] ([ProductName],[UnitPrice],[ProductTypeID])" + " VALUES(?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, sanPham.getTenSanPham());
			prepareStatement.setDouble(2, sanPham.getDonGia());

			String ma = sanPham.getLoaiSanPham().getMaLoaiSP().replace("LSP", "");
			prepareStatement.setInt(3, Integer.parseInt(ma));

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
	 * update thông tin cho 1 sản phẩm
	 * 
	 * @param sp sản phẩm cần update
	 * @return
	 */
	public boolean updateSanPham(SanPham sp) {
		String sql = "UPDATE Product SET ProductName=?, UnitPrice=?, ProductTypeID=?" + " where ProductID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, sp.getTenSanPham());
			prepareStatement.setDouble(2, sp.getDonGia());
			int ma = Integer.parseInt(sp.getLoaiSanPham().getMaLoaiSP().replaceAll("LSP", ""));
			prepareStatement.setInt(3, ma);

			String maSanPham = sp.getMaSanPham();
			String msp = maSanPham.replaceAll("SP", "");

			prepareStatement.setInt(4, Integer.parseInt(msp));
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
	 * Lấy 1 sản phẩm theo mã
	 * 
	 * @param maSanPham
	 * @return SanPham
	 */
	public SanPham getSanPhamTheoMa(String maSanPham) {
		SanPham sanPham = new SanPham();
		LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();
		String sql = "SELECT * FROM Product " + " where ProductID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			String msp = maSanPham.replaceAll("SP", "");
			prepareStatement.setInt(1, Integer.parseInt(msp));

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				sanPham.setMaSanPham(rs.getString(1));
				sanPham.setTenSanPham(rs.getString(2));
				sanPham.setDonGia(rs.getDouble(3));

				LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamTheoMaLoai(rs.getString("ProductTypeID"));

				sanPham.setLoaiSanPham(loaiSanPham);
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

		return sanPham;
	}

	/**
	 * Lấy 1 sản phẩm theo tên
	 * 
	 * @param tenSanPham
	 * @return SanPham
	 */
	public SanPham getSanPhamTheoTen(String tenSanPham) {
		SanPham sanPham = new SanPham();
		LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();
		String sql = "SELECT * FROM Product " + " where ProductName = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenSanPham);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				sanPham.setMaSanPham("SP" + rs.getInt("ProductID"));
				sanPham.setTenSanPham(rs.getString("ProductName"));
				sanPham.setDonGia(rs.getDouble("UnitPrice"));

				int ma = rs.getInt("ProductTypeID");
				String maLoaiSP = "LSP" + ma;

				LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamTheoMaLoai(maLoaiSP);
				sanPham.setLoaiSanPham(loaiSanPham);
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
		return sanPham;

	}

	/**
	 * xóa thông tin của 1 sản phẩm theo mã
	 * 
	 * @param mã sản phẩm cần xóa
	 * @return true or false
	 */
	public boolean deleteSanPham(String maSanPham) {
		String sql = "DELETE FROM Product" + " where ProductID = ?";
		int rs = 0;
		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, maSanPham);

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
	
	//kiểm tra trùng
	public boolean checkExist(String tenSanPham) {
		boolean check = false;
		String sql = "select * from Product where ProductName = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenSanPham);
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
	
	public List<String> getSanPhamTheoLoai(String tenLoaiSP) {
		List<String> dsTenSanPham = new ArrayList<String>();
		String sql = "SELECT        Product.ProductName\r\n"
				+ "FROM            Product_type INNER JOIN\r\n"
				+ "                         Product ON Product_type.ProductTypeID = Product.ProductTypeID\r\n"
				+ "where Product_type.ProductTypeName =?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;
		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenLoaiSP);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				dsTenSanPham.add(rs.getString("ProductName"));
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
		return dsTenSanPham;
    }
    
    public boolean deleteSanPhamByLoai(String maLoaiSP) {
        String sql = "DELETE FROM Product WHERE ProductTypeID = ?";
        Connection con = MSSQLConnection.getJDBCConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            int maLoaiSPInt = Integer.parseInt(maLoaiSP.replaceAll("LSP", ""));
            pstmt.setInt(1, maLoaiSPInt);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
        
    
}
