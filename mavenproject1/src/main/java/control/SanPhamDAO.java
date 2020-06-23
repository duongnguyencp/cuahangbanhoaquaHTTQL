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
public class SanPhamDAO extends DAO{
    
    public SanPhamDAO() {
        super();
    }
    public ArrayList<SanPham> getAllSanPham() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [SanPham] inner join [MatHang] on SanPham.idMatHang=MatHang.idMatHang";
        ArrayList<SanPham> listSanPham = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setIdMatHang(rs.getInt("idMatHang"));
                sp.setMaMatHang(rs.getString("maMatHang"));
                sp.setTenMatHang(rs.getString("tenMatHang"));
                sp.setMoTa(rs.getString("moTa"));
                sp.setIdSanPham(rs.getInt("idSanPham"));
                sp.setGia(rs.getInt("gia"));
                sp.setHanSuDung(rs.getString("hanSuDung"));
                MatHang hang=new MatHang();
                hang.setIdMatHang(rs.getInt("idMatHang"));
                BienLaiKho bienLaiKho=new BienLaiKho();
                bienLaiKho.setId(rs.getInt("idBienLaiKho"));
                sp.setBienLaiKho(bienLaiKho);
                listSanPham.add(sp);
            }
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
        return listSanPham;
    }
    public boolean themSanPham(SanPham sp) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String gia = "N'" + sp.getGia()+ "',";
        String hanSd = "N'" + sp.getHanSuDung()+ "',";
        String idMatHang = "N'" + sp.getIdMatHang()+ "',";
        String idBienLaiKho = "N'" + sp.getBienLaiKho().getId()+ "'";
        String sql = "insert into [SanPham] (gia,hanSuDung,idMatHang,idBienLaiKho)"
                + " values(" + gia + hanSd + idMatHang + idBienLaiKho + ")";
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
                    System.out.println("roll back...SanPhamDAO");
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
