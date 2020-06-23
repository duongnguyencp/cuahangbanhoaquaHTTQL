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
public class KhoDAO extends DAO{
    public KhoDAO() {
        super();
    }

    public ArrayList<Kho> getAllKho() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [Kho]";
        ArrayList<Kho> listKho = new ArrayList<>();
        try {
            
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Kho kho = new Kho();
                NhanVien nhanVien=new NhanVien();
                nhanVien.setIdNhanVien(rs.getInt("idNhanVien"));
                kho.setId(rs.getInt("idKho"));
                kho.setDiaChi(rs.getString("diaChi"));
                kho.setNv(nhanVien);
                listKho.add(kho);
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
        return listKho;
    }
}
