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
public class NhanVienDAO extends DAO {

    public NhanVienDAO() {
        super();
    }
    public ArrayList<NhanVien> getAllNVKeToan(){
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = " select * from  [CuaHangHoaQua].[dbo].[NhanVien]  inner join [CuaHangHoaQua].[dbo].[Nguoi] on NhanVien.idNhanVien=Nguoi.idNguoi where vaiTro=N'Kế toán'";
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setId(rs.getInt(7));
                nhanVien.setEmail(rs.getString(10));
                nhanVien.setNgaySinh(rs.getString(11));
                nhanVien.setGioiTinh(rs.getString(12));
                nhanVien.setHocVan(rs.getString(13));
                nhanVien.setDiaChi(rs.getString(14));
                nhanVien.setHoTen(rs.getString(15));
                nhanVien.setIdNhanVien(rs.getInt(1));
                nhanVien.setVaiTro(rs.getString(2));
                nhanVien.setUserName(rs.getString(3));
                nhanVien.setPassword(rs.getString(4));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt(5));
                cuaHang.setId(rs.getInt(6));
                nhanVien.setBoPhan(boPhan);
                nhanVien.setCuaHang(cuaHang);
                listNhanVien.add(nhanVien);
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
        return listNhanVien;
        
    }
    public ArrayList<NhanVien> getAllNVKho(){
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = " select * from  [CuaHangHoaQua].[dbo].[NhanVien]  inner join [CuaHangHoaQua].[dbo].[Nguoi] on NhanVien.idNhanVien=Nguoi.idNguoi where vaiTro=N'Quản lí kho'";
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setId(rs.getInt(7));
                nhanVien.setEmail(rs.getString(10));
                nhanVien.setNgaySinh(rs.getString(11));
                nhanVien.setGioiTinh(rs.getString(12));
                nhanVien.setHocVan(rs.getString(13));
                nhanVien.setDiaChi(rs.getString(14));
                nhanVien.setHoTen(rs.getString(15));
                nhanVien.setIdNhanVien(rs.getInt(1));
                nhanVien.setVaiTro(rs.getString(2));
                nhanVien.setUserName(rs.getString(3));
                nhanVien.setPassword(rs.getString(4));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt(5));
                cuaHang.setId(rs.getInt(6));
                nhanVien.setBoPhan(boPhan);
                nhanVien.setCuaHang(cuaHang);
                listNhanVien.add(nhanVien);
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
        return listNhanVien;
        
    }
    public ArrayList<NhanVien> getAllNhanVien() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [CuaHangHoaQua].[dbo].[NhanVien] inner join [CuaHangHoaQua].[dbo].[Nguoi] on NhanVien.idNhanVien=Nguoi.idNguoi";
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setId(rs.getInt(8));
                nhanVien.setEmail(rs.getString(9));
                nhanVien.setNgaySinh(rs.getString(10));
                nhanVien.setGioiTinh(rs.getString(11));
                nhanVien.setHocVan(rs.getString(12));
                nhanVien.setDiaChi(rs.getString(13));
                nhanVien.setHoTen(rs.getString(14));
                nhanVien.setIdNhanVien(rs.getInt(1));
                nhanVien.setVaiTro(rs.getString(2));
                nhanVien.setUserName(rs.getString(3));
                nhanVien.setPassword(rs.getString(4));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt(5));
                cuaHang.setId(rs.getInt(6));
                nhanVien.setBoPhan(boPhan);
                nhanVien.setCuaHang(cuaHang);
                listNhanVien.add(nhanVien);
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
        return listNhanVien;
    }
}
