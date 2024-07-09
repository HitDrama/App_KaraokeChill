
package entity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HoaDon {
	private String maHoaDon;
	private NhanVien nhanVien;
	private String tenKhachHang;
	private Date ngayTao;
	private double tongTien;

	public HoaDon() {
		super();
	}

	public HoaDon(String maHoaDon, NhanVien nhanVien, String tenKhachHang, Date ngayTao) {
		super();
		this.maHoaDon = maHoaDon;
		this.nhanVien = nhanVien;
		this.tenKhachHang = tenKhachHang;
		this.ngayTao = ngayTao;
	}
        
        public HoaDon(String maHoaDon, NhanVien nhanVien, String tenKhachHang, Date ngayTao, double tongTien) {
            this.maHoaDon = maHoaDon;
            this.nhanVien = nhanVien;
            this.tenKhachHang = tenKhachHang;
            this.ngayTao = ngayTao;
            this.tongTien = tongTien;
        }

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public Date getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}


	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
        
        public String getNgayTaoFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(ngayTao);
    }

    public String getTongTienFormatted() {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(tongTien);
    }

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", nhanVien=" + nhanVien + ", tenKhachHang=" + tenKhachHang
				+ ", ngayTao=" + ngayTao + ", tongTien=" + tongTien;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maHoaDon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHoaDon, other.maHoaDon);
	}

}
