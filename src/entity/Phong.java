

package entity;

import java.util.Objects;

public class Phong {
	private String maPhong;
	private String tenPhong;
	private String tinhTrang;
	
	private LoaiPhong loaiPhong;

	/**
	 * 
	 */
	public Phong() {
	}

	/**
	 * @param maPhong
	 */
	public Phong(String maPhong) {
		this.maPhong = maPhong;
	}

	/**
	 * @param maPhong
	 * @param tenPhong
	 * @param trangThai
	 * @param loaiPhong
	 */
	public Phong(String maPhong, String tenPhong, String trangThai, LoaiPhong loaiPhong) {
		this.maPhong = maPhong;
		this.tenPhong = tenPhong;
		this.tinhTrang = trangThai;
		this.loaiPhong = loaiPhong;
	}

	public String getMaPhong() {
		return maPhong;
	}

	public void setMaPhong(String maPhong) {
		this.maPhong = maPhong;
	}

	public String getTenPhong() {
		return tenPhong;
	}

	public void setTenPhong(String tenPhong) {
		this.tenPhong = tenPhong;
	}

	public String getTrangThai() {
		return tinhTrang;
	}

	public void setTrangThai(String trangThai) {
		this.tinhTrang = trangThai;
	}

	public LoaiPhong getLoaiPhong() {
		return loaiPhong;
	}

	public void setLoaiPhong(LoaiPhong loaiPhong) {
		this.loaiPhong = loaiPhong;
	}

	@Override
	public String toString() {
		return "Phong [maPhong=" + maPhong + ", tenPhong=" + tenPhong + ", trangThai=" + tinhTrang + ", loaiPhong="
				+ loaiPhong.getMaLoaiPhong() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPhong);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phong other = (Phong) obj;
		return Objects.equals(maPhong, other.maPhong);
	}
	
	
	
}
