
package entity;

import java.util.Objects;

public class KhachHang {
	private String maKhachHang;
	private String hoTenKH;
	private String soDienThoai;
	private int soLanDen;
	private String loaiKhachHang;

	/**
	 * 
	 */
	public KhachHang() {
	}

	/**
	 * @param maKhachHang
	 */
	public KhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	/**
	 * @param maKhachHang
	 * @param hoTenKH
	 * @param soDienThoai
	 */
	
	
	public KhachHang(String maKhachHang, String hoTenKH, String soDienThoai, int soLanDen, String loaiKhachHang) {
		super();
		this.maKhachHang = maKhachHang;
		this.hoTenKH = hoTenKH;
		this.soDienThoai = soDienThoai;
		this.soLanDen = soLanDen;
		this.loaiKhachHang = loaiKhachHang;
	}
	
	public int getSoLanDen() {
		return soLanDen;
	}

	public void setSoLanDen(int soLanDen) {
		this.soLanDen = soLanDen;
	}

	public String getLoaiKhachHang() {
		return loaiKhachHang;
	}

	public void setLoaiKhachHang(String loaiKhachHang) {
		this.loaiKhachHang = loaiKhachHang;
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}


	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	public String getHoTenKH() {
		return hoTenKH;
	}

	public void setHoTenKH(String hoTenKH) {
		this.hoTenKH = hoTenKH;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", hoTenKH=" + hoTenKH + ", soDienThoai=" + soDienThoai
				+ ", soLanDen=" + soLanDen + ", loaiKhachHang=" + loaiKhachHang + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}

	

}
