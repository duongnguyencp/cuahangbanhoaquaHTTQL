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
    public ArrayList<NhanVien> getAllNVBanHang(){
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = " select * from  [CuaHangHoaQua].[dbo].[NhanVien]  inner join [CuaHangHoaQua].[dbo].[Nguoi] on NhanVien.idNhanVien=Nguoi.idNguoi where vaiTro=N'Bán hàng'";
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setId(rs.getInt("idNhanVien"));
                nhanVien.setEmail(rs.getString("email"));
                nhanVien.setNgaySinh(rs.getString("ngaySinh"));
                nhanVien.setGioiTinh(rs.getString("gioiTinh"));
                nhanVien.setHocVan(rs.getString("hocVan"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setHoTen(rs.getString("hoTen"));
                nhanVien.setIdNhanVien(rs.getInt("idNhanVien"));
                nhanVien.setVaiTro(rs.getString("vaiTro"));
                nhanVien.setUserName(rs.getString("username"));
                nhanVien.setPassword(rs.getString("password"));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt("idBoPhan"));
                cuaHang.setId(rs.getInt("idCuaHang"));
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
                nhanVien.setId(rs.getInt("idNhanVien"));
                nhanVien.setEmail(rs.getString("email"));
                nhanVien.setNgaySinh(rs.getString("ngaySinh"));
                nhanVien.setGioiTinh(rs.getString("gioiTinh"));
                nhanVien.setHocVan(rs.getString("hocVan"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setHoTen(rs.getString("hoTen"));
                nhanVien.setIdNhanVien(rs.getInt("idNhanVien"));
                nhanVien.setVaiTro(rs.getString("vaiTro"));
                nhanVien.setUserName(rs.getString("username"));
                nhanVien.setPassword(rs.getString("password"));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt("idBoPhan"));
                cuaHang.setId(rs.getInt("idCuaHang"));
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
                nhanVien.setId(rs.getInt("idNhanVien"));
                nhanVien.setEmail(rs.getString("email"));
                nhanVien.setNgaySinh(rs.getString("ngaySinh"));
                nhanVien.setGioiTinh(rs.getString("gioiTinh"));
                nhanVien.setHocVan(rs.getString("hocVan"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setHoTen(rs.getString("hoTen"));
                nhanVien.setIdNhanVien(rs.getInt("idNhanVien"));
                nhanVien.setVaiTro(rs.getString("vaiTro"));
                nhanVien.setUserName(rs.getString("username"));
                nhanVien.setPassword(rs.getString("password"));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt("idBoPhan"));
                cuaHang.setId(rs.getInt("idCuaHang"));
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
                nhanVien.setId(rs.getInt("idNhanVien"));
                nhanVien.setEmail(rs.getString("email"));
                nhanVien.setNgaySinh(rs.getString("ngaySinh"));
                nhanVien.setGioiTinh(rs.getString("gioiTinh"));
                nhanVien.setHocVan(rs.getString("hocVan"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setHoTen(rs.getString("hoTen"));
                nhanVien.setIdNhanVien(rs.getInt("idNhanVien"));
                nhanVien.setVaiTro(rs.getString("vaiTro"));
                nhanVien.setUserName(rs.getString("username"));
                nhanVien.setPassword(rs.getString("password"));
                BoPhan boPhan = new BoPhan();
                CuaHang cuaHang = new CuaHang();
                boPhan.setId(rs.getInt("idBoPhan"));
                cuaHang.setId(rs.getInt("idCuaHang"));
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
