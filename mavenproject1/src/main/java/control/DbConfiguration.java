/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author Duong
 */
public class DbConfiguration {
    public static final String HOST_NAME = "localhost";
    public static final String DB_NAME = "CuaHangHoaQua2";
    public static final String DB_PORT = "1433";
    public static final String USER_NAME = "sa";
    public static final String PASSWORD = "123";
    public static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final int DB_MIN_CONNECTIONS = 2;
    public static final int DB_MAX_CONNECTIONS = 10;
    // jdbc:mysql://hostname:port/dbname
    public static final String CONNECTION_URL = "jdbc:sqlserver://" + HOST_NAME + ":" + DB_PORT + ";databaseName=" + DB_NAME;
     private DbConfiguration() {
        super();
    }
}
