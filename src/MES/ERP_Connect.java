/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Morten
 */
public class ERP_Connect {

    private Connection con = null;

    public static void main(String[] args) {
//        ERP_Connect obj = new ERP_Connect();
//        obj.run(args);
        ERP_Connect obj2 = new ERP_Connect();
        obj2.getConnection();
    }

    public Connection getConnection() {

        try {
            // The atributes of the server
            //  jdbc:sqlserver:[localhost[\instanceName][:portNumber]][;property=value[;property=value]]; 
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // NetDirect JDBC driver 
            String serverName = "10.137.0.21";
            String portNumber = "1433";
            String mydatabase = serverName + ":" + portNumber + ";databaseName=Dynamics09";
            String url = "jdbc:sqlserver://" + mydatabase + ";user=TEK\\TEK-AX09-074;password=uliNithurt90"; // a JDBC url 
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
