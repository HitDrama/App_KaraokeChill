

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.MSSQLConnection;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {

	
        public TaiKhoan checkLoginByCardNumber(String cardNumber) {
            TaiKhoan taiKhoan = new TaiKhoan();
            String sql = "SELECT * FROM Account WHERE Cardnumber=?";

            Connection con = MSSQLConnection.getJDBCConnection();
            PreparedStatement prepareStatement = null;

            try {
                prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, cardNumber);
                ResultSet rs = prepareStatement.executeQuery();
                if (rs.next()) {
                    taiKhoan.setTenDangNhap(rs.getString("Username"));
                    taiKhoan.setVaiTro(rs.getString("Role"));
                    taiKhoan.setCardNumber(cardNumber);

                    int maNV = rs.getInt("EmployeeID");
                    String maNhanVien = "NV" + maNV;

                    NhanVienDAO nvDAO = new NhanVienDAO();
                    NhanVien nhanVien = nvDAO.getNhanVienTheoMa(maNhanVien);

                    taiKhoan.setNhanVien(nhanVien);
                    return taiKhoan;
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
    
	public TaiKhoan checkLogin(String tenDangNhap, String matKhau) {
		TaiKhoan taiKhoan = new TaiKhoan();
		String sql = "SELECT * FROM Account" + " WHERE Username=? and Password=?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenDangNhap);
			prepareStatement.setString(2, matKhau);
			ResultSet rs = prepareStatement.executeQuery();
			if (rs.next()) {
				taiKhoan.setTenDangNhap(tenDangNhap);
				taiKhoan.setVaiTro(rs.getString("Role"));

				int maNV = rs.getInt("EmployeeID");
				String maNhanVien = "NV" + maNV;

				NhanVienDAO nvDAO = new NhanVienDAO();
				NhanVien nhanVien = nvDAO.getNhanVienTheoMa(maNhanVien);

				taiKhoan.setNhanVien(nhanVien);
				return taiKhoan;
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
	 * Lấy danh sách tài khoản từ database
	 * 
	 * @return
	 */
	public List<TaiKhoan> getDanhSachTaiKhoan() {
		List<TaiKhoan> listTaiKhoan = new ArrayList<TaiKhoan>();
		String sql = "SELECT * FROM Account";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				TaiKhoan tk = new TaiKhoan();

				tk.setTenDangNhap(rs.getString("Username"));
				tk.setVaiTro(rs.getString("Role"));
				int maNhanVien = rs.getInt("EmployeeID");
				NhanVienDAO nvDAO = new NhanVienDAO();
				tk.setNhanVien(nvDAO.getNhanVienTheoMa("NV" + maNhanVien));
                                tk.setCardNumber(rs.getString("Cardnumber"));
				listTaiKhoan.add(tk);
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
		return listTaiKhoan;
	}

	/**
	 * Thêm 1 tài khoản
	 * 
	 * @param tk
	 * @return true nếu thêm thành công . false nếu thêm thất bại
	 */
	public boolean addTaiKhoan(TaiKhoan tk) {
		String sql = "INSERT INTO [dbo].[Account] ([Username], [Password], [Role], [EmployeeID], [SecurityQuestion], [SecurityAnswer], [Cardnumber])"
				+ " VALUES(?,?,?,?,?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {

			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tk.getTenDangNhap());
			prepareStatement.setString(2, tk.getMatKhau());
			prepareStatement.setString(3, tk.getVaiTro());

			String maNV = tk.getNhanVien().getMaNhanVien();
			String maNhanVien = maNV.replaceAll("NV", "");
			prepareStatement.setInt(4, Integer.parseInt(maNhanVien));

			prepareStatement.setString(5, tk.getCauHoi());
			prepareStatement.setString(6, tk.getTraLoi());
                        prepareStatement.setString(7, tk.getCardNumber());
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

	
	public boolean updateMatKhau(TaiKhoan tk) {
		String sql = "UPDATE Account SET Password = ?" + " where Username = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tk.getMatKhau());
			prepareStatement.setString(2, tk.getTenDangNhap());
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

	
	public boolean deleteTaiKhoan(String tenDN) {
		String sql = "DELETE FROM Account" + " where Username = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenDN);
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

	public TaiKhoan getTaiKhoanTheoTen(String tenDN) {
		TaiKhoan tk = new TaiKhoan();
		String sql = "SELECT * FROM Account" + " where Username = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenDN);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				tk.setTenDangNhap(tenDN);
				tk.setVaiTro(rs.getString("VaiTro"));
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
		return tk;
	}

	/**
	 * lấy câu hỏi bảo mật theo tên tài khoản
	 * 
	 * @param tên đăng nhập
	 * @return
	 */
	public String getCauHoiTheoTen(String tenDN) {
		String cauHoi = null;
		String sql = "SELECT SecurityQuestion FROM Account" + " where Username = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, tenDN);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				cauHoi = rs.getString("SecurityQuestion");
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
		return cauHoi;
	}


	public boolean updateMatKhauTheoTenVaTraLoi(String tenDN, String cauTraLoi, String matKhauMoi) {
		String sql = "UPDATE Account SET Password = ?" + " WHERE Username = ? AND SecurityAnswer = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, matKhauMoi);
			prepareStatement.setString(2, tenDN);
			prepareStatement.setString(3, cauTraLoi);

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

	public boolean checkExist(int maNV) {
		boolean check = false;
		String sql = "select * from Account where EmployeeID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setInt(1, maNV);
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
        
        public boolean checkCardExist(String cardNumber) {
            boolean check = false;
            String sql = "SELECT * FROM Account WHERE Cardnumber = ?";

            Connection con = MSSQLConnection.getJDBCConnection();
            PreparedStatement prepareStatement = null;

            try {
                prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, cardNumber);
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
