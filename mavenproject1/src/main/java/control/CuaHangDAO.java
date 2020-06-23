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
public class CuaHangDAO extends DAO {

    public CuaHangDAO() {
        super();
    }

    public CuaHang getCuaHangById(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CuaHang] where idCuaHang=" + id + "";
        CuaHang cuaHang = new CuaHang();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                int idChiNhanh = rs.getInt(4);
                ChiNhanh chiNhanh = new ChiNhanh();
                chiNhanh.setId(idChiNhanh);
                cuaHang.setId(rs.getInt(1));
                cuaHang.setDiaChi(rs.getString(2));
                cuaHang.setTenCuaHang(rs.getString(3));
                cuaHang.setChiNhanh(chiNhanh);
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
        return cuaHang;

    }

    public ArrayList<CuaHang> getAllCuaHang() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CuaHang]";
        ArrayList<CuaHang> listCuaHang = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                CuaHang cuaHang = new CuaHang();
                int idChiNhanh = rs.getInt(4);
                ChiNhanh chiNhanh = new ChiNhanh();
                chiNhanh.setId(idChiNhanh);
                cuaHang.setId(rs.getInt(1));
                cuaHang.setDiaChi(rs.getString(2));
                cuaHang.setTenCuaHang(rs.getString(3));
                cuaHang.setChiNhanh(chiNhanh);
                listCuaHang.add(cuaHang);
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
        return listCuaHang;
    }
}
