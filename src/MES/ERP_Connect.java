/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Morten
 */
public class ERP_Connect {

    public ERP_Connect getConnection() {

        try {
            // The atributes of the server

            //  jdbc:sqlserver:[localhost[\instanceName][:portNumber]][;property=value[;property=value]]; 
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // NetDirect JDBC driver 

            String serverName = "10.137.0.21";

            String portNumber = "1433";

            String mydatabase = serverName + ":" + portNumber + ";databaseName=Dynamics09";

            String url = "jdbc:sqlserver://" + mydatabase + ";user=AXReader;password=AXReader"; // a JDBC url 

            System.out.println("Connecting to database... ");//  + "(" + url + ")");

// Load the JDBC driver 
            Class.forName(driverName);
            // Create a connection to the database 

            DriverManager.setLoginTimeout(1);

            con = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {

            System.err.println("No connection to database... Using hardcoded values instead!");

// Could not find the database driver 
        }

        return con;

    }
}
