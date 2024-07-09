
package entity;

import java.util.Date;
import java.util.Objects;

public class DonDatPhong {
	private String maDatPhong;
	private Date thoiGianVao;
	private String trangThaiDon;
	
	private KhachHang khachHang;
	private Phong phong;
	/**
	 * 
	 */
	public DonDatPhong() {
	}
	/**
	 * @param maDatPhong
	 */
	public DonDatPhong(String maDatPhong) {
		this.maDatPhong = maDatPhong;
	}
	public DonDatPhong(String maDatPhong, Date thoiGianVao, String trangThaiDon, KhachHang khachHang, Phong phong) {
		super();
		this.maDatPhong = maDatPhong;
		this.thoiGianVao = thoiGianVao;
		this.trangThaiDon = trangThaiDon;
		this.khachHang = khachHang;
		this.phong = phong;
	}
	public String getMaDatPhong() {
		return maDatPhong;
	}
	public void setMaDatPhong(String maDatPhong) {
		this.maDatPhong = maDatPhong;
	}
	public Date getThoiGianVao() {
		return thoiGianVao;
	}
	public void setThoiGianVao(Date thoiGianVao) {
		this.thoiGianVao = thoiGianVao;
	}
	public String getTrangThaiDon() {
		return trangThaiDon;
	}
	public void setTrangThaiDon(String trangThaiDon) {
		this.trangThaiDon = trangThaiDon;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public Phong getPhong() {
		return phong;
	}
	public void setPhong(Phong phong) {
		this.phong = phong;
	}
	@Override
	public String toString() {
		return "DonDatPhong [maDatPhong=" + maDatPhong + ", thoiGianVao=" + thoiGianVao + ", trangThaiDon="
				+ trangThaiDon + ", khachHang=" + khachHang + ", phong=" + phong + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(maDatPhong);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DonDatPhong other = (DonDatPhong) obj;
		return Objects.equals(maDatPhong, other.maDatPhong);
	}
	
}
