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
public class BienLaiKhoDAO extends DAO{
    public BienLaiKhoDAO() {
        super();
    }
    public ArrayList<BienLaiKho> getAllBienLaiKho() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [BienLaiKho]";
        ArrayList<BienLaiKho> bienLaiKhos = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                BienLaiKho bienLaiKho = new BienLaiKho();
                bienLaiKho.setId(rs.getInt("idBienLaiKho"));
                bienLaiKho.setMaBienLai(rs.getString("maBienLaiKho"));
                bienLaiKho.setNgayLap(rs.getString("ngayLap"));
                Kho k=new Kho();
                k.setId(rs.getInt("idKho"));
                bienLaiKho.setKho(k);
                bienLaiKho.setSoLuong(rs.getInt("soLuong"));
                bienLaiKho.setTongCong(rs.getInt("tongCong"));
                bienLaiKhos.add(bienLaiKho);
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
        return bienLaiKhos;
    }
}
