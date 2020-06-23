/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.*;
/**
 *
 * @author Duong
 */
public class HopDongDAO extends DAO{

    public HopDongDAO() {
        super();
    }
    public HopDong getHopDongByNcc(NhaCungCap cap){
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select *  from [HopDong] where idNhaCungCap="+cap.getId()+"";
        HopDong dong=new HopDong();
        try {
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                dong.setId(rs.getInt(1));
                dong.setTenHopDong(rs.getString("tenHopDong"));
                dong.setNgayKi(rs.getString("ngayKi"));
                dong.setDenNGay(rs.getString("denNgay"));
                NhanVien nv=new NhanVien();
                nv.setId(rs.getInt("idNhanVien"));
                dong.setNv(nv);
                dong.setNcc(cap);
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
        return dong;

    }
}
