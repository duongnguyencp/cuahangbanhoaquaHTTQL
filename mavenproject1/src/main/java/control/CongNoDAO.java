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
import model.*;

/**
 *
 * @author Duong
 */
public class CongNoDAO extends DAO {

    public CongNoDAO() {
        super();
    }

    public ArrayList<CongNo> getAllCongNo() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CongNo]";
        ArrayList<CongNo> listCongNo = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                CongNo cn = new CongNo();
                cn.setIdCongNo(rs.getInt("idCongNo"));
                cn.setMaSoThue(rs.getString("maSoThue"));
                cn.setSoTienNo(rs.getInt("soTienNo"));
                NhaCungCap cap = new NhaCungCap();
                cap.setId((rs.getInt("idNhaCungCap")));
                PhieuThuChi chi = new PhieuThuChi();
                chi.setIdPhieuThuChi(rs.getInt("idPhieuThuChi"));
                cn.setPhieuThuChi(chi);
                cn.setCap(cap);
                listCongNo.add(cn);
            }
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
        return listCongNo;
    }

    public ArrayList<RecordCongNoNCC> loadBangCongNo() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        String sql1 = " select sum(soTienNo) as tongNo, cn.idNhaCungCap from [CongNo] cn group by idNhaCungCap ";
        String sql3 = "  select sum(ptc.soTien) as tongTienChi, cn.idNhaCungCap from [PhieuThuChi] ptc inner join  [CongNo] cn on cn.idPhieuThuChi=ptc.idPhieuThuChi  group by cn.idNhaCungCap ";
        ArrayList<RecordCongNoNCC> listRecordCongNoNCC = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql1);
            rs = stm.executeQuery();
            stm = con.prepareStatement(sql3);
            rs3 = stm.executeQuery();
            while (rs.next()&&rs3.next()) {
                RecordCongNoNCC recordCongNoNCC = new RecordCongNoNCC();
                int soTienNo = rs.getInt("tongNo");
                int soTienChi = rs3.getInt("tongTienChi");
                soTienNo=soTienNo-soTienChi;
                int idNhaCungCap = rs.getInt("idNhaCungCap");
                String sql2 = "select * from [NhaCungCap] where idNhaCungCap=" + idNhaCungCap;
                stm = con.prepareStatement(sql2);
                rs2 = stm.executeQuery();
                rs2.next();
                NhaCungCap ncc = new NhaCungCap();
                ncc.setId(rs2.getInt("idNhaCungCap"));
                ncc.setTen(rs2.getString("ten"));
                ncc.setSodienthoai(rs2.getString("email"));
                ncc.setEmail(rs2.getString("sodienthoai"));
                recordCongNoNCC.setNcc(ncc);
                recordCongNoNCC.setTienConNo(soTienNo);
                listRecordCongNoNCC.add(recordCongNoNCC);
            }
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
        return listRecordCongNoNCC;

    }

    public boolean traCongNo(CongNo cn, PhieuThuChi phieuThuChi) {
        System.out.println(con);
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

        String maSoThue = "N'" + cn.getMaSoThue() + "',";
        String soTienNo = "N'" + cn.getSoTienNo() + "',";
        String idNhaCungCap = "N'" + cn.getCap().getId() + "'";
        String sql2 = "select * from [PhieuThuChi]";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con.setAutoCommit(false);
            stm = con.prepareStatement(sql);
            stm.executeUpdate();
            con.commit();
            stm = con.prepareStatement(sql2);
            rs = stm.executeQuery();
            int maxId = 0;
            while (rs.next()) {
                int tmp = rs.getInt("idPhieuThuChi");
                if (maxId < tmp) {
                    maxId = tmp;
                }
            }
            String idPhieuThuChi = "N'" + (maxId) + "',";

            sql = "insert into [CongNo] (maSoThue,soTienNo,idPhieuThuChi,idNhaCungCap)"
                    + " values(" + maSoThue + soTienNo + idPhieuThuChi + idNhaCungCap + ")";
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
                    System.out.println("roll back...CongNoDAO");
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

    public boolean themCongNo(CongNo cn) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String maSoThue = "N'" + cn.getMaSoThue() + "',";
        String soTienNo = "N'" + cn.getSoTienNo() + "',";
        String idNhaCungCap = "N'" + cn.getCap().getId() + "'";
        String sql2 = "select * from [PhieuThuChi]";

        try {
            stm = con.prepareStatement(sql2);
            rs = stm.executeQuery();
            int maxId = 0;
            while (rs.next()) {
                int tmp = rs.getInt("idPhieuThuChi");
                if (maxId < tmp) {
                    maxId = tmp;
                }
            }
            String idPhieuThuChi = "N'" + (maxId) + "',";
            String sql = "insert into [CongNo] (maSoThue,soTienNo,idPhieuThuChi,idNhaCungCap)"
                    + " values(" + maSoThue + soTienNo + idPhieuThuChi + idNhaCungCap + ")";
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
                    System.out.println("roll back...CongNoDAO");
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
