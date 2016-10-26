/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;
 
import java.sql.*;
import java.util.logging.Logger;
 
/**
 *
 * @author Fuck dig, Morten
 */
public class ERP_Connect {
 
    private Connection con = null;
 
    public static void main(String[] args) {
//        ERP_Connect obj = new ERP_Connect();
//        obj.run(args);
        ERP_Connect obj2 = new ERP_Connect();
        Connection conn = obj2.getConnection();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT ITEMID, NAME, QTYCALC, SCHEDSTART, SCHEDEND FROM PRODTABLE WHERE DATAAREAID = 't074'");
            ResultSet rs = stmt.getResultSet();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
 
        } catch (SQLException ex) {
            //Logger.getLogger(ERP_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public Connection getConnection() {
 
        try {
            // The atributes of the server
            //  jdbc:sqlserver:[localhost[\instanceName][:portNumber]][;property=value[;property=value]];
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // NetDirect JDBC driver
            String serverName = "10.137.0.21";
            String portNumber = "1433";
            String mydatabase = serverName + ":" + portNumber + ";databaseName=Dynamics09";
            String url = "jdbc:sqlserver://" + mydatabase + ";;user=AXReader;password=AXReader"; // a JDBC url
            System.out.println("Connecting to database... " + "(" + url + ")");
           
            try {
// Load the JDBC driver
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
            }
 
// Create a connection to the database
            DriverManager.setLoginTimeout(1);
            con = DriverManager.getConnection(url);
 
        } catch (SQLException e) {
 
            System.err.println("No connection to database... Using hardcoded values instead!");
            System.out.println(e);
// Could not find the database driver
        }
       
        return con;
 
    }
}