/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duong
 */
package model;
public class HoaDonBanHang {
    private int idHoaDonBanHang;
    private int soLuong;
    private String ngay;
    private SanPham sp;
    private int soTien;
    private NhanVien nv;

    public HoaDonBanHang() {
    }

    public int getIdHoaDonBanHang() {
        return idHoaDonBanHang;
    }

    public void setIdHoaDonBanHang(int idHoaDonBanHang) {
        this.idHoaDonBanHang = idHoaDonBanHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public SanPham getSp() {
        return sp;
    }

    public void setSp(SanPham sp) {
        this.sp = sp;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public NhanVien getNv() {
        return nv;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }
    
}
