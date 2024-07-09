
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

public class LoaiSanPhamDAO {
	/**
	 * lấy tất cả loại sản phẩm hiện có trong csdl
	 * 
	 * @return danh sách loại sản phẩm
	 */

	public List<LoaiSanPham> getDanhSachLoaiSanPham() {
		List<LoaiSanPham> listLoaiSanPham = new ArrayList<LoaiSanPham>();
		String sql = "SELECT * FROM Product_type";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				LoaiSanPham loaisp = createLoaiSanPham(rs);

				int maLoaiSp = rs.getInt("ProductTypeID");
				loaisp.setMaLoaiSP("LSP" + maLoaiSp);

				listLoaiSanPham.add(loaisp);
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

		return listLoaiSanPham;
	}

	/**
	 * Hàm dùng để insert 1 loại sản phẩm vào cơ sở dữ liệu
	 * 
	 * @param lsp tham số truyền vào là LoaiSanPham
	 * @return trả về true nếu insert thành công trả về false nếu insert thất bại
	 * @throws IOException
	 */
	public boolean addLoaiSanPham(LoaiSanPham loaiSanPham) {
		String sql = "INSERT INTO [dbo].[Product_type] ([ProductTypeName])" + " VALUES(?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, loaiSanPham.getTenLoaiSP());

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
	 * update thông tin cho 1 loại sản phẩm
	 * 
	 * @param lsp loại sản phẩm cần update
	 * @return
	 */
	public boolean updateLoaiSanPham(LoaiSanPham loaiSanPham) {
		String sql = "UPDATE Product_type SET ProductTypeName=?" + " where ProductTypeID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, loaiSanPham.getTenLoaiSP());

			String maLoaiSanPham = loaiSanPham.getMaLoaiSP();
			String ma = maLoaiSanPham.replaceAll("LSP", "");

			prepareStatement.setInt(2, Integer.parseInt(ma));

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
	 * Lấy 1 sản phẩm theo mã loại
	 * 
	 * @param maLoaiSanPham
	 * @return LoaiSanPham
	 */
	public LoaiSanPham getLoaiSanPhamTheoMaLoai(String maLoaiSanPham) {
		LoaiSanPham loaiSanPham = null;
		String sql = "SELECT * FROM Product_type " + " where ProductTypeID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			String ma = maLoaiSanPham.replaceAll("LSP", "");
			prepareStatement.setInt(1, Integer.parseInt(ma));

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				loaiSanPham = createLoaiSanPham(rs);
				loaiSanPham.setMaLoaiSP(maLoaiSanPham);
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

		return loaiSanPham;
	}

	/**
	 * xóa thông tin của 1 loại sản phẩm
	 * 
	 * @param mã loại sản phẩm cần xóa
	 * @return
	 */
	public boolean deleteLoaiSanPham(String maLoaiSanPham) {
		String sql = "DELETE FROM Product_type" + " where ProductTypeID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, maLoaiSanPham);

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
	 * Tạo 1 loại sản phẩm , truyền dữ liệu từ cơ sở dữ liệu
	 * 
	 * @param rs resultset truyền vào
	 * @return loại sản phẩm
	 */
	private LoaiSanPham createLoaiSanPham(final ResultSet rs) {
		LoaiSanPham loaisp = new LoaiSanPham();

		try {
			loaisp.setTenLoaiSP(rs.getString("ProductTypeName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loaisp;
	}

	/**
	 * lấy thông tin của 1 sản phẩm theo tên sản phẩm
	 * 
	 * @param
	 * @return
	 */
	public String getMaTheoTenLoai(String ten) {
		String maLoaiSanPham = null;
		String sql = "select ProductTypeID from  Product_type" + " where ProductTypeName=? ";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, ten);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				int ma = rs.getInt("ProductTypeID");
				maLoaiSanPham = "LSP" + ma;
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

		return maLoaiSanPham;
	}
	//kiểm tra trùng
		public boolean checkExist(String tenLoaiSanPham) {
			boolean check = false;
			String sql = "select * from Product_type where ProductTypeName = ?";

			Connection con = MSSQLConnection.getJDBCConnection();
			PreparedStatement prepareStatement = null;

			try {
				prepareStatement = con.prepareStatement(sql);

				prepareStatement.setString(1, tenLoaiSanPham);
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
