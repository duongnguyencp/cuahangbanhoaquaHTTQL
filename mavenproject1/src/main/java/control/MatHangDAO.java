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
import model.MatHang;

/**
 *
 * @author Duong
 */
public class MatHangDAO extends DAO {

    public MatHangDAO() {
        super();
    }
    public ArrayList<MatHang> getAllMatHang() {
        System.out.println("dang get All Mat Hang");
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [MatHang]";
        ArrayList<MatHang> listMatHang = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                MatHang mh = new MatHang();
                mh.setIdMatHang(rs.getInt(1));
                mh.setMaMatHang(rs.getString(2));
                mh.setTenMatHang(rs.getString(3));
                mh.setMoTa(rs.getString(4));
                mh.setDonViTinh(rs.getString(5));
                listMatHang.add(mh);
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
        return listMatHang;
    }
    public boolean themMatHang(MatHang hang) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String ma = "N'" + hang.getMaMatHang() + "',";
        String ten = "N'" + hang.getTenMatHang() + "',";
        String donVi = "N'" + hang.getDonViTinh() + "',";
        String mota = "N'" + hang.getMoTa() + "'";
        String sql = "insert into [MatHang] (maMatHang,tenMatHang,donVi,moTa)"
                + " values(" + ma + ten + donVi + mota + ")";
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
                    System.out.println("roll back...MatHangDAO");
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

    public static void main(String[] args) {
        MatHangDAO mhdao = new MatHangDAO();
        MatHang hang = new MatHang();
        hang.setMaMatHang("fasdf");
        hang.setMoTa("àdasd");
        hang.setTenMatHang("àdsaf");
        //mhdao.themMatHang(hang);
        ArrayList<MatHang> listMh=mhdao.getAllMatHang();
        System.out.println(listMh.size());
    }
}
