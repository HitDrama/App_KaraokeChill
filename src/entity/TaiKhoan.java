

package entity;

import java.util.Objects;

public class TaiKhoan {
	private String tenDangNhap;
	private String matKhau;
	private String vaiTro;
	private NhanVien nhanVien;
	private String cauHoi;
	private String traLoi;
        private String cardNumber;

	public TaiKhoan() {
		super();
	}
        
        public TaiKhoan(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

	public TaiKhoan(String tenDangNhap, String matKhau, String vaiTro, NhanVien nhanVien, String cauHoi,
			String traLoi, String cardNumber) {
		super();
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.vaiTro = vaiTro;
		this.nhanVien = nhanVien;
		this.cauHoi = cauHoi;
		this.traLoi = traLoi;
                this.cardNumber = cardNumber;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public String getCauHoi() {
		return cauHoi;
	}

	public void setCauHoi(String cauHoi) {
		this.cauHoi = cauHoi;
	}

	public String getTraLoi() {
		return traLoi;
	}

	public void setTraLoi(String traLoi) {
		this.traLoi = traLoi;
	}
        
        public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

    @Override
    public String toString() {
        return "TaiKhoan{" + "tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro + ", nhanVien=" + nhanVien + ", cauHoi=" + cauHoi + ", traLoi=" + traLoi + ", cardNumber=" + cardNumber + '}';
    }

	

	@Override
	public int hashCode() {
		return Objects.hash(tenDangNhap);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(tenDangNhap, other.tenDangNhap);
	}

}
