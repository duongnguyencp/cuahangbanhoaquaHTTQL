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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.*;

/**
 *
 * @author Duong
 */
public class PhieuThuChiDAO extends DAO {

    public PhieuThuChiDAO() {
        super();
    }

    public ArrayList<RecordChiTietCongNo> getAlPTCByName(String name) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "  select *  from [PhieuThuChi] inner join [CongNo] on CongNo.idPhieuThuChi=PhieuThuChi.idPhieuThuChi  where tenDoiTuong='" + name + "'";
        ArrayList<RecordChiTietCongNo> listPTC = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                RecordChiTietCongNo chiTietCongNo = new RecordChiTietCongNo();
                PhieuThuChi phieuThuChi = new PhieuThuChi();
                phieuThuChi.setIdPhieuThuChi(rs.getInt("idPhieuThuChi"));
                phieuThuChi.setChuyenKhoan(rs.getString("chuyenKhoan"));
                phieuThuChi.setDienGiai(rs.getString("dienGiai"));
                phieuThuChi.setNgayLap(rs.getString("ngayLap"));
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setLyDo(rs.getString("lyDo"));
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("idNhanVien"));
                phieuThuChi.setNv(nv);
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setSoTien(rs.getInt("soTien"));
                phieuThuChi.setLoaiPhieu(rs.getString("loaiPhieu"));
                phieuThuChi.setSoPhieu(rs.getString("soPhieu"));
                CongNo cn = new CongNo();
                cn.setIdCongNo(rs.getInt("idCongNo"));
                cn.setMaSoThue(rs.getString("maSoThue"));
                cn.setSoTienNo(rs.getInt("soTienNo"));
                NhaCungCap cap = new NhaCungCap();
                cap.setId((rs.getInt("idNhaCungCap")));
                cn.setPhieuThuChi(phieuThuChi);
                cn.setCap(cap);
                chiTietCongNo.setCn(cn);
                chiTietCongNo.setPhieuThuChi(phieuThuChi);
                listPTC.add(chiTietCongNo);
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
        return listPTC;
    }

    public ArrayList<PhieuThuChi> getAllPhieuThuChi() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [PhieuThuChi]";
        ArrayList<PhieuThuChi> phieuThuChis = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                PhieuThuChi phieuThuChi = new PhieuThuChi();
                phieuThuChi.setIdPhieuThuChi(rs.getInt("idPhieuThuChi"));
                phieuThuChi.setChuyenKhoan(rs.getString("chuyenKhoan"));
                phieuThuChi.setDienGiai(rs.getString("dienGiai"));
                String ngayLap = rs.getString("ngayLap");

                phieuThuChi.setNgayLap(rs.getString("ngayLap"));
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setSoPhieu(rs.getString("soPhieu"));
                phieuThuChi.setLyDo(rs.getString("lyDo"));
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("idNhanVien"));
                phieuThuChi.setNv(nv);
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setSoTien(rs.getInt("soTien"));
                phieuThuChi.setLoaiPhieu(rs.getString("loaiPhieu"));
                phieuThuChis.add(phieuThuChi);
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
        return phieuThuChis;
    }

    public Date converToDate(String date) {
        Date date2 = new Date();
        if(date.equals("")) return null;
        try {
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            return date2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date2;
    }

    public ArrayList<PhieuThuChi> getAllPhieuThuChiByDate(String eDate, String sDate) {
        Date eDateD = converToDate(eDate);
        Date sDateD = converToDate(sDate);
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [PhieuThuChi]";
        ArrayList<PhieuThuChi> phieuThuChis = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                PhieuThuChi phieuThuChi = new PhieuThuChi();
                phieuThuChi.setIdPhieuThuChi(rs.getInt("idPhieuThuChi"));
                phieuThuChi.setChuyenKhoan(rs.getString("chuyenKhoan"));
                phieuThuChi.setDienGiai(rs.getString("dienGiai"));
                String ngayLap = rs.getString("ngayLap");
                Date ngayLapDate = converToDate(ngayLap);
                phieuThuChi.setNgayLap(rs.getString("ngayLap"));
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setSoPhieu(rs.getString("soPhieu"));
                phieuThuChi.setLyDo(rs.getString("lyDo"));
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("idNhanVien"));
                phieuThuChi.setNv(nv);
                phieuThuChi.setTenDoiTuong(rs.getString("tenDoiTuong"));
                phieuThuChi.setSoTien(rs.getInt("soTien"));
                phieuThuChi.setLoaiPhieu(rs.getString("loaiPhieu"));
                    if (ngayLapDate.after(eDateD) && ngayLapDate.before(sDateD)) {
                        phieuThuChis.add(phieuThuChi);
                    }

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
        return phieuThuChis;
    }

    public boolean themPhieuThuChi(PhieuThuChi phieuThuChi) {
        PreparedStatement stm = null;
        ResultSet rs = null;
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
        try {
            con.setAutoCommit(false);
            stm = con.prepareStatement(sql);
            stm.executeUpdate();
            con.commit();
            con.close();
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("roll back...PhieuThuChiDAO");
                }
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            return false;
        } finally {
            try {
                stm.close();
                con.close();
            } catch (SQLException ex3) {
                //
                ex3.printStackTrace();
            }
        }
        return true;
    }

}
