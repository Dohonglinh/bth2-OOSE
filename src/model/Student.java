package model;

import java.time.LocalDate;

public class Student {
    private String maSV;
    private String hoTen;
    private LocalDate ngaySinh;
    private Major nganh;
    private double diemTB;
    private String lopSinhHoat;

    public Student(String maSV, String hoTen, LocalDate ngaySinh, Major nganh, double diemTB, String lopSinhHoat) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.nganh = nganh;
        this.diemTB = diemTB;
        this.lopSinhHoat = lopSinhHoat;
    }

    public String getMaSV() { return maSV; }
    public String getHoTen() { return hoTen; }
    public LocalDate getNgaySinh() { return ngaySinh; }
    public Major getNganh() { return nganh; }
    public double getDiemTB() { return diemTB; }
    public String getLopSinhHoat() { return lopSinhHoat; }

    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    public void setNganh(Major nganh) { this.nganh = nganh; }
    public void setDiemTB(double diemTB) { this.diemTB = diemTB; }
    public void setLopSinhHoat(String lopSinhHoat) { this.lopSinhHoat = lopSinhHoat; }

    @Override
    public String toString() {
        return String.format("%s | %-25s | %s | %-5s | %.2f | %-10s",
                maSV, hoTen, ngaySinh, nganh, diemTB, lopSinhHoat);
    }
}
