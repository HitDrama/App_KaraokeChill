
package entity;

import java.util.Arrays;
import java.util.Objects;

public class NhanVien {
	private String maNhanVien;
	private String hoTen;
	private int gioiTinh;
	private String soDT;
	private String email;
	private String diaChi;
	private String namSinh;
	private byte[] hinh;

	public NhanVien() {
		super();
	}
        
        public NhanVien(String hoTen, String maNhanVien) {
        this.hoTen = hoTen;
        this.maNhanVien = maNhanVien;
    }

	public NhanVien(String maNhanVien, String hoTen, int gioiTinh, String soDT, String email, String diaChi,
			String namSinh, byte[] hinh) {
		super();
		this.maNhanVien = maNhanVien;
		this.hoTen = hoTen;
		this.gioiTinh = gioiTinh;
		this.soDT = soDT;
		this.email = email;
		this.diaChi = diaChi;
		this.namSinh = namSinh;
		this.hinh = hinh;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public int getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(int gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSoDT() {
		return soDT;
	}

	public void setSoDT(String soDT) {
		this.soDT = soDT;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getNamSinh() {
		return namSinh;
	}

	public void setNamSinh(String namSinh) {
		this.namSinh = namSinh;
	}

	public byte[] getHinh() {
		return hinh;
	}

	public void setHinh(byte[] hinh) {
		this.hinh = hinh;
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", hoTen=" + hoTen + ", gioiTinh=" + gioiTinh + ", soDT=" + soDT
				+ ", email=" + email + ", diaChi=" + diaChi + ", namSinh=" + namSinh + ", hinh=" + Arrays.toString(hinh)
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}

}
