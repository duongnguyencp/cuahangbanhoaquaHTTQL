/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import static control.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.BienLaiKho;
import model.*;

/**
 *
 * @author Duong
 */
public class HoaDonBanHangDAO extends DAO {

    public HoaDonBanHangDAO() {
        super();
    }

    public void themHoaDonBanHang(HoaDonBanHang hdbh) {
        String soLuong = "'" + hdbh.getSoLuong() + "',";
        String ngay = "'" + hdbh.getNgay() + "',";
        String idSanPham = "'" + hdbh.getSp().getIdSanPham() + "',";
        String soTien = "N'" + hdbh.getSoTien() + "',";
        String idNhanVien = "N'" + hdbh.getNv().getIdNhanVien() + "'";
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql2 = "insert into [HoaDonBanHang] (soLuong,ngay,idSanPham,soTien,idNhanVien) values "
                + " (" + soLuong + ngay + idSanPham + soTien + idNhanVien + ")";
        try {
            con.setAutoCommit(false);
           
            stm = con.prepareStatement(sql2);
            stm.executeUpdate();
            con.commit();
            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.rollback();
                stm.close();
                con.close();
            } catch (SQLException ex) {
                //
            }
        }
    }

    public void themHoaDonBanHang(HoaDonBanHang hdbh, PhieuThuChi phieuThuChi) {
        String loaiPhieu = "N'" + phieuThuChi.getLoaiPhieu() + "',";
        String soPhieu = "N'" + phieuThuChi.getSoPhieu() + "',";
        String ngayLap = "N'" + phieuThuChi.getNgayLap() + "',";
        String tenDoiTuong = "N'" + phieuThuChi.getTenDoiTuong() + "',";
        String lyDo = "N'" + phieuThuChi.getLyDo() + "',";
        String dienGiai = "N'" + phieuThuChi.getDienGiai() + "',";
        String idNhanVien = "N'" + phieuThuChi.getNv().getIdNhanVien() + "',";
        String soTien = "N'" + phieuThuChi.getSoTien() + "',";
        String chuyenKhoan = "N'" + phieuThuChi.getChuyenKhoan() + "'";

        String sql = "insert into [PhieuThuChi] (loaiPhieu,soPhieu,ngayLap,tenDoiTuong,lyDo,dienGiai"
                + ",idNhanVien,soTien,chuyenKhoan)"
                + " values(" + loaiPhieu + soPhieu + ngayLap + tenDoiTuong + lyDo + dienGiai + idNhanVien + soTien + chuyenKhoan + ")";

        String soLuong = "'" + hdbh.getSoLuong() + "',";
        String ngay = "'" + hdbh.getNgay() + "',";
        String idSanPham = "'" + hdbh.getSp().getIdSanPham() + "',";
        soTien = "N'" + hdbh.getSoTien() + "',";
        idNhanVien = "N'" + hdbh.getNv().getIdNhanVien() + "'";
        PreparedStatement stm = null;
        ResultSet rs = null;

        String sql2 = "insert into [HoaDonBanHang] (soLuong,ngay,idSanPham,soTien,idNhanVien) values "
                + " values(" + soLuong + ngay + idSanPham + soTien + idNhanVien + ")";
        try {
            con.setAutoCommit(false);
            stm = con.prepareStatement(sql);
            stm.executeUpdate();
            con.commit();
            stm = con.prepareStatement(sql2);
            stm.executeUpdate();
            con.commit();
            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                //
            }
        }
    }


    public ArrayList<HoaDonBanHang> getHoaDonBanHangs() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [HoaDonBanHang]";
        ArrayList<HoaDonBanHang> hoaDonBanHangs = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                HoaDonBanHang hoaDonBanHang = new HoaDonBanHang();
                hoaDonBanHang.setIdHoaDonBanHang(rs.getInt("idHoaDonBanHang"));
                hoaDonBanHang.setNgay(rs.getString("ngay"));
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("idNhanVien"));
                SanPham sp = new SanPham();
                sp.setIdSanPham(rs.getInt("idSanPham"));
                hoaDonBanHang.setNv(nv);
                hoaDonBanHang.setSp(sp);
                hoaDonBanHang.setSoLuong(rs.getInt("soLuong"));
                hoaDonBanHang.setSoTien(rs.getInt("soTien"));
                hoaDonBanHangs.add(hoaDonBanHang);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                //
            }
        }
        return hoaDonBanHangs;
    }
}
