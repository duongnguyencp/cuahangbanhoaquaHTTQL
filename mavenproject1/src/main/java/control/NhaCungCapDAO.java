/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.*;

/**
 *
 * @author Duong
 */
public class NhaCungCapDAO extends DAO {

    public NhaCungCapDAO() {
        super();
    }
   
    public ArrayList<NhaCungCap> getAllNhaCungCap() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [NhaCungCap]";
        ArrayList<NhaCungCap> listNhaCC = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setId(rs.getInt("idNhaCungCap"));
                ncc.setTen(rs.getString("ten"));
                ncc.setSodienthoai(rs.getString("sodienthoai"));
                ncc.setEmail(rs.getString("email"));
                listNhaCC.add(ncc);
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
        return listNhaCC;
    }
}
