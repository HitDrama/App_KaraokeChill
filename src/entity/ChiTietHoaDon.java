package entity;

import java.time.LocalTime;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
	private Phong phong;
	private SanPham sanPham;
	private int soLuong;
        private boolean trangThai;
        private LocalTime gioRa;

	public ChiTietHoaDon() {
		super();
	}

	public ChiTietHoaDon(HoaDon hoaDon, Phong phong, SanPham sanPham, int soLuong) {
		super();
		this.hoaDon = hoaDon;
		this.phong = phong;
		this.sanPham = sanPham;
		this.soLuong = soLuong;
	}
        
    public LocalTime getGioRa() {
        return gioRa;
    }

    public void setGioRa(LocalTime gioRa) {
        this.gioRa = gioRa;
    }

    // Getter và Setter cho trangThai
    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + hoaDon + ", phong=" + phong + ", sanPham=" + sanPham + ", soLuong=" + soLuong
				+ "]";
	}
	
	public double getThanhTien() {
		return soLuong * sanPham.getDonGia();
	}
}
