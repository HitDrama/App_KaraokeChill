
package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connectDB.MSSQLConnection;
import entity.LoaiPhong;
import entity.Phong;

public class PhongDAO {
	/**
	 * lấy tất cả Phòng hiện có trong csdl
	 * 
	 * @return danh sách Phòng
	 */
	public List<Phong> getDanhSachPhong() {
		LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
		List<Phong> listPhong = new ArrayList<Phong>();
		String sql = "SELECT * FROM Room";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				Phong phong = new Phong();
				LoaiPhong loaiPhong = new LoaiPhong();

				int maP = rs.getInt(1);
				String maPhong = "MP" + maP;
				phong.setMaPhong(maPhong);

				phong.setTenPhong(rs.getString(2));
				loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(rs.getString(3));

				phong.setLoaiPhong(loaiPhong);
				phong.setTrangThai(rs.getString(4));

				listPhong.add(phong);
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

		return listPhong;
	}

	/**
	 * Hàm dùng để insert 1 Phòng vào cơ sở dữ liệu
	 * 
	 * @param p tham số truy�?n vào là Phong
	 * @return trả v�? true nếu insert thành công trả v�? false nếu insert thất bại
	 * @throws IOException
	 */
	public boolean addPhong(Phong p) {
		String sql = "INSERT INTO [dbo].[Room] ([RoomName], [RoomTypeID], [Status])" + " VALUES(?,?,?)";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			prepareStatement.setString(1, p.getTenPhong());
			prepareStatement.setString(2, p.getLoaiPhong().getMaLoaiPhong());
			prepareStatement.setString(3, p.getTrangThai());

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
	 * Lấy 1 Phòng theo mã
	 * 
	 * @param maPhong
	 * @return Phong
	 */
	public Phong getPhongTheoMa(String maPhong) {
		Phong phong = new Phong();
		LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
		String sql = "SELECT * FROM Room" + " where RoomID = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);

			String stt = maPhong.replaceAll("MP", "");
			prepareStatement.setInt(1, Integer.parseInt(stt));

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				phong.setMaPhong(maPhong);
				phong.setTenPhong(rs.getString("RoomName"));

				LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(rs.getString("RoomTypeID"));
				phong.setLoaiPhong(loaiPhong);
				phong.setTrangThai(rs.getString("Status"));
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

		return phong;
	}

	/**
	 * update thông tin cho 1 Phong
	 * 
	 * @param phong Phòng cần update
	 * @return
	 */
	public boolean updatePhong(Phong phong) {
		String sql = "UPDATE Room SET RoomName = ?, Status = ?" + " where RoomID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, phong.getTenPhong());
			prepareStatement.setString(2, phong.getTrangThai());

			String maPhong = phong.getMaPhong();

			String stt = maPhong.replaceAll("MP", "");

			prepareStatement.setInt(3, Integer.parseInt(stt));

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
	 * xóa thông tin của 1 Phòng
	 * 
	 * @param maPhong
	 * @return boolean
	 */
	public boolean deletePhong(String maPhong) {
		String sql = "DELETE FROM Room" + " where RoomID = ?";
		int rs = 0;

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			String stt = maPhong.replaceAll("MP", "");

			prepareStatement.setInt(1, Integer.parseInt(stt));

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
	 * lấy tất cả Phòng trống trong csdl
	 * 
	 * Quân sửa: lấy tất cả phòng theo tình trạng
	 * 
	 * @return danh sách Phòng trống
	 */
	public List<Phong> getDanhSachPhongTheoTinhTrang(String tinhTrang) {
		LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
		List<Phong> listPhongTrong = new ArrayList<Phong>();

		String sql = "SELECT * FROM Room WHERE Status = ?";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setString(1, tinhTrang);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				Phong phong = new Phong();
				LoaiPhong loaiPhong = new LoaiPhong();

				int maP = rs.getInt(1);
				String maPhong = "MP" + maP;
				phong.setMaPhong(maPhong);

				phong.setTenPhong(rs.getString(2));

				loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(rs.getString(3));
				phong.setLoaiPhong(loaiPhong);
				phong.setTrangThai(rs.getString(4));

				listPhongTrong.add(phong);
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

		return listPhongTrong;
	}
	
	public List<Phong> getDanhSachPhongTheoNgayDatTruoc(Date ngayDatTruoc) {
		LoaiPhongDAO loaiPhongDAO = new LoaiPhongDAO();
		List<Phong> listPhongDatTruoc = new ArrayList<Phong>();

		String sql = "SELECT Room.RoomID, Room.RoomName, Room.RoomTypeID, Room.Status\r\n"
				+ "FROM     Room_reservation INNER JOIN\r\n"
				+ "                  Room ON Room_reservation.RoomID = Room.RoomID\r\n"
				+ "where 	Day(CheckinTime) = ? and MONTH(CheckinTime) = ? and YEAR(CheckinTime) = ? and OrderStatus = N'Dat Truoc'";

		Connection con = MSSQLConnection.getJDBCConnection();
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = con.prepareStatement(sql);
			prepareStatement.setInt(1, ngayDatTruoc.getDate());
			prepareStatement.setInt(2, 1 + ngayDatTruoc.getMonth());
			prepareStatement.setInt(3, 1900 + ngayDatTruoc.getYear());
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				Phong phong = new Phong();
				LoaiPhong loaiPhong = new LoaiPhong();

				int maP = rs.getInt(1);
				String maPhong = "MP" + maP;
				phong.setMaPhong(maPhong);

				phong.setTenPhong(rs.getString(2));

				loaiPhong = loaiPhongDAO.getLoaiPhongTheoMa(rs.getString(3));
				phong.setLoaiPhong(loaiPhong);
				phong.setTrangThai(rs.getString(4));

				listPhongDatTruoc.add(phong);
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

		return listPhongDatTruoc;
	}
}
