package SCADA;

import javax.xml.transform.Result;
import java.sql.*;

/**
 * Created by madsn on 14-12-2016.
 */
public class SQLConnection {
    private static Connection conn = null;
    private static Statement stmt = null;

    protected SQLConnection(){

    }

    private static Statement getStatement(){
        if (conn == null){

            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Making new Connection");
                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/greenhouselog","root","");

                stmt = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return stmt;

    }

    public static ResultSet execute(String Query){
        Statement stmt = getStatement();
        try {
            if(stmt.execute(Query)){
                return stmt.getResultSet();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(){
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
