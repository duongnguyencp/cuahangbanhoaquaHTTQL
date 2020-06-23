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
import model.BienLaiNhap;
import model.SanPham;
import model.BienLaiXuat;

/**
 *
 * @author Duong
 */
public class BienLaiXuatDAO extends DAO {

    public BienLaiXuatDAO() {
        super();
    }

    public boolean themBienLaiXuat(BienLaiXuat bienLaiXuat, SanPham pham) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String idBienLaiKho;
        System.out.println("BienLaiXuat" + bienLaiXuat);
        String idBienLaiXuat = "'" + bienLaiXuat.getIdBienLaiXuat() + "',";
        String tiLeThue = "'" + bienLaiXuat.getTiLeThue() + "',";
        String tiLeLai = "'" + bienLaiXuat.getTiLeLai() + "'";
        String idCuaHang = "'" + bienLaiXuat.getCuaHang().getId() + "',";
        String idNhanVien = "'" + bienLaiXuat.getNv().getIdNhanVien()+ "',";

        String maBienLaiKho = "'" + bienLaiXuat.getMaBienLai() + "',";
        String ngayLap = "N'" + bienLaiXuat.getNgayLap() + "',";
        String soLuong = "N'" + bienLaiXuat.getSoLuong() + "',";
        String idKho = "N'" + bienLaiXuat.getKho().getId() + "',";
        String tongTien = "N'" + bienLaiXuat.getTongCong() + "'";
        ////////////////////////--------SanPham
        String idSanPham = "'" + pham.getGia() + "',";
        String maSanPham = "'" + pham.getMaSp() + "',";
        String hanSuDung = "'" + pham.getHanSuDung() + "',";
        String idMatHang = "'" + pham.getIdMatHang() + "'";
        String gia = "'" + pham.getGia() + "',";
        System.out.println("BienLaiNhapDAO " + pham.getIdMatHang());
        ////////////// // //
        try {
            String sql2 = "insert into [BienLaiKho] (maBienLaiKho,ngayLap,idKho,soLuong,tongCong)"
                    + " values(" + maBienLaiKho + ngayLap + idKho + soLuong + tongTien + ")";
            System.out.println(sql2);
            con.setAutoCommit(false);
            stm = con.prepareStatement(sql2);
            stm.executeUpdate();
            con.commit();
            
            String sql3 = "select idBienLaiKho from [BienLaiKho]";
            stm = con.prepareStatement(sql3);
            rs = stm.executeQuery();
            int maxId = 0;
            while (rs.next()) {
                int tmp = rs.getInt("idBienLaiKho");
                if (maxId < tmp) {
                    maxId = tmp;
                }
            }
            idBienLaiKho = "'" + (maxId) + "',";
            String sql4 = "update [BienLaiKho] set soLuong = soLuong - " + bienLaiXuat.getSoLuong() + " where idBienLaiKho=" + pham.getBienLaiKho().getId();
            System.out.println(sql4);
            sql3 = "insert into [SanPham] (idBienLaiKho,maSp,gia,hanSuDung,idMatHang)"
                    + " values(" + idBienLaiKho + maSanPham + gia + hanSuDung + idMatHang + ")";
            String sql = "insert into [BienLaiXuat] (idBienLaiKho,tiLeThue,idCuaHang,idNhanVien,tiLeLai)"
                    + " values(" + idBienLaiKho + tiLeThue + idCuaHang + idNhanVien + tiLeLai + ")";
            
            stm = con.prepareStatement(sql3);
            stm.executeUpdate();
            con.commit();
            stm = con.prepareStatement(sql);
            stm.executeUpdate();
            con.commit();
            stm = con.prepareStatement(sql4);
            stm.executeUpdate();
            con.commit();
            con.close();
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("roll back...BienLaiXuatDAO");
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
