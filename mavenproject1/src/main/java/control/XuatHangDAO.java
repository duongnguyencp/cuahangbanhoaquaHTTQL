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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import model.*;

/**
 *
 * @author Duong
 */
public class XuatHangDAO extends DAO {

    public XuatHangDAO() {
        super();
    }

    public ArrayList<RecordSanPham> loadSanPhamXuatKho(Kho kho) {
        ArrayList<Integer> listIdSp = new ArrayList<>();
        ArrayList<Integer> listConLai = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql2 = "select SanPham.idSanPham, (BienLaiKho.soLuong-HoaDonBanHang.soLuong) as conLai  from [HoaDonBanHang] inner join [SanPham] on SanPham.idSanPham=HoaDonBanHang.idHoaDonBanHang inner join [BienLaiKho] on BienLaiKho.idBienLaiKho=SanPham.idSanPham ";
        String sql = "   select * from [SanPham] sp inner join   [BienLaiKho] blk on sp.idBienLaiKho=blk.idBienLaiKho \n"
                + "               inner join  [BienLaiXuat] blx on sp.idBienLaiKho=blx.idBienLaiKho inner join [MatHang] mh on sp.idMatHang=mh.idMatHang where idKho=" + kho.getId();
        ArrayList<RecordSanPham> listSanPhamDX = new ArrayList<RecordSanPham>();
        try {
            stm = con.prepareStatement(sql2);
            rs = stm.executeQuery();
            while (rs.next()) {
                listIdSp.add(rs.getInt("idSanPham"));
                listConLai.add(rs.getInt("conLai"));
            }
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSp(rs.getString("maSp"));
                sp.setIdSanPham(rs.getInt("idSanPham"));
                RecordSanPham recordSanPham = new RecordSanPham();
                boolean check=false;
                for (int i = 0; i < listIdSp.size(); i++) {
                    if (sp.getIdSanPham() == listIdSp.get(i).intValue()) {
                        int conLai = listConLai.get(i);
                        recordSanPham.setSoLuong(conLai);
                        check=true;
                    }

                }
                sp.setTenMatHang(rs.getString("tenMatHang"));
                sp.setMaMatHang(rs.getString("maMatHang"));
                sp.setGia(rs.getInt("gia"));
                sp.setHanSuDung(rs.getString("hanSuDung"));
                sp.setIdMatHang(rs.getInt("idMatHang"));
                BienLaiKho blk = new BienLaiKho();
                int soLuong = rs.getInt("soLuong");
                blk.setId(rs.getInt("idBienLaiKho"));
                sp.setBienLaiKho(blk);
                sp.setDonViTinh(rs.getString("donVi"));
                recordSanPham.setPham(sp);
                if(!check)
                recordSanPham.setSoLuong(soLuong);
                listSanPhamDX.add(recordSanPham);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
            }
        }
        return listSanPhamDX;
    }
//    public BienLaiNhap loadBienLaiNhapTheoKho(SanPham sp) {
//        PreparedStatement stm = null;
//        ResultSet rs = null;
//        String sql = "  select bln.idBienLaiNhap,bln.idBienLaiKho, bln.idHopDong, bln.idNhanVien from [CuaHangHoaQua].[dbo].[BienLaiNhap] bln inner join [CuaHangHoaQua].[dbo].[BienLaiKho] blk on bln.idBienLaiKho=blk.idBienLaiKho inner join [CuaHangHoaQua].[dbo].[SanPham] sp on sp.idBienLaiKho=bln.idBienLaiKho where idSanPham="+sp.getIdSanPham();
//        BienLaiNhap bln=new BienLaiNhap();
//        try {
//            stm = con.prepareStatement(sql);
//            rs = stm.executeQuery();
//            while (rs.next()) {
//                sp.setIdSanPham(rs.getInt("idSanPham"));
//                sp.setTenMatHang(rs.getString("tenMatHang"));
//                sp.setMaMatHang(rs.getString("maMatHang"));
//                sp.setGia(rs.getInt("gia"));
//                sp.setHanSuDung(rs.getString("hanSuDung"));
//                sp.setIdMatHang(rs.getInt("idMatHang"));
//                BienLaiKho blk=new BienLaiKho();
//                blk.setId(rs.getInt("idBienLaiKho"));
//                sp.setBienLaiKho(blk);
//                int soLuong=rs.getInt("soLuong");
//                sp.setDonViTinh(rs.getString("donVi"));
//                listMHTrongKho.put(sp, new Integer(soLuong));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                stm.close();
//                con.close();
//            } catch (SQLException ex) {
//                //
//            }
//        }
//        return listMHTrongKho;
//    }

    public ArrayList<RecordSanPham> loadMatHangTrongKhoTheoKho(Kho kho) {
        System.out.println("a");
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "  select sp.maSp, sp.idSanPham,mh.tenMatHang,mh.maMatHang, sp.gia,sp.hanSuDung,sp.idMatHang,blk.idBienLaiKho,blk.soLuong,mh.donVi from [SanPham] sp inner join   [BienLaiKho] blk on sp.idBienLaiKho=blk.idBienLaiKho \n"
                + "  inner join  [BienLaiNhap] bln on sp.idBienLaiKho=bln.idBienLaiKho inner join [MatHang] mh on sp.idMatHang=mh.idMatHang where idKho=" + kho.getId();
        ArrayList<RecordSanPham> listMHTrongKho = new ArrayList<RecordSanPham>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSp(rs.getString("maSp"));
                sp.setIdSanPham(rs.getInt("idSanPham"));
                sp.setTenMatHang(rs.getString("tenMatHang"));
                sp.setMaMatHang(rs.getString("maMatHang"));
                sp.setGia(rs.getInt("gia"));
                sp.setHanSuDung(rs.getString("hanSuDung"));
                sp.setIdMatHang(rs.getInt("idMatHang"));
                BienLaiKho blk = new BienLaiKho();
                blk.setId(rs.getInt("idBienLaiKho"));
                sp.setBienLaiKho(blk);
                int soLuong = rs.getInt("soLuong");
                sp.setDonViTinh(rs.getString("donVi"));
                RecordSanPham recordSanPham = new RecordSanPham();
                recordSanPham.setPham(sp);
                recordSanPham.setSoLuong(soLuong);
                listMHTrongKho.add(recordSanPham);
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
        return listMHTrongKho;
    }
}
