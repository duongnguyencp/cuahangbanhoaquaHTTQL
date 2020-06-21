/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.*;

/**
 *
 * @author Duong
 */
public class DAO {

    protected static Connection con = null;

    public DAO() {
        
            try {
//                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//                con = DriverManager.getConnection(dbURL, user, pass);
                  con=HikariCPDataSource.getConnection();
                // Code here.
            } // Handle any errors that may have occurred.
            catch (Exception e) {
                e.printStackTrace();
            }
        
    }

    public Connection getCon() {
        return con;
    }

    public static void main(String[] args) {
        DAO dao = new DAO();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql2 = "select * from taikhoan";
        try {
                    con=HikariCPDataSource.getConnection();
            stm = con.prepareStatement(sql2);
            rs=stm.executeQuery();
            while(rs.next()){
               System.out.println("id:\t"+rs.getString(1));
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
