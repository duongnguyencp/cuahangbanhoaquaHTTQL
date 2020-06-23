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
public class CongTyDAO extends DAO {

    public CongTyDAO() {
        super();
    }

    public CongTy getCongTyById(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CongTy] where idCongTy=" + id + "";
        CongTy congTy = new CongTy();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                congTy.setId(rs.getInt(1));
                congTy.setTenCongTy(rs.getString(2));
                congTy.setDiaChi(rs.getString(3));
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
        return congTy;

    }

    public ArrayList<CongTy> getAllCongTy() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CongTy]";
        ArrayList<CongTy> listCongTy = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                CongTy congty = new CongTy();
                congty.setId(rs.getInt(1));
                congty.setTenCongTy(rs.getString(2));
                congty.setDiaChi(rs.getString(3));
                listCongTy.add(congty);
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
        return listCongTy;
    }
}
