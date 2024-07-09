
package entity;

import java.util.Objects;

public class LoaiSanPham {
	private String maLoaiSP;
	private String tenLoaiSP;

	public LoaiSanPham() {

	}

	public LoaiSanPham(String maLoaiSP, String tenLoaiSP) {
		super();
		this.maLoaiSP = maLoaiSP;
		this.tenLoaiSP = tenLoaiSP;
	}

	public String getMaLoaiSP() {
		return maLoaiSP;
	}

	public void setMaLoaiSP(String maLoaiSP) {
		this.maLoaiSP = maLoaiSP;
	}

	public String getTenLoaiSP() {
		return tenLoaiSP;
	}

	public void setTenLoaiSP(String tenLoaiSP) {
		this.tenLoaiSP = tenLoaiSP;
	}

	@Override
	public String toString() {
		return "LoaiSanPham [maLoaiSP=" + maLoaiSP + ", tenLoaiSP=" + tenLoaiSP + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLoaiSP);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoaiSanPham other = (LoaiSanPham) obj;
		return Objects.equals(maLoaiSP, other.maLoaiSP);
	}

}
