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
public class ChiNhanhDAO extends DAO {

    public ChiNhanhDAO() {
        super();
    }

    public ChiNhanh getChiNhanhById(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [ChiNhanh] where idChiNhanh="+id+"";
        ChiNhanh chiNhanh=new ChiNhanh();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                chiNhanh.setId(rs.getInt(1));
                chiNhanh.setTenChiNhanh(rs.getString(2));
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
        return chiNhanh;

    }

    public ArrayList<ChiNhanh> getAllChiNhanh() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [ChiNhanh]";
        ArrayList<ChiNhanh> listChiNhanh = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                ChiNhanh chinhanh = new ChiNhanh();
                chinhanh.setId(rs.getInt(1));
                chinhanh.setTenChiNhanh(rs.getString(2));
                listChiNhanh.add(chinhanh);
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
        return listChiNhanh;
    }
}
