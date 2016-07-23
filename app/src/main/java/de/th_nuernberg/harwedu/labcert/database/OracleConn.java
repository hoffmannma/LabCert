package de.th_nuernberg.harwedu.labcert.database;

/**
 * Created by Edu on 23.07.2016.
 */
import java.sql.*;

public class OracleConn {
    public static void connect () throws Exception
    {
        Class.forName ("oracle.jdbc.OracleDriver");

        Connection conn = DriverManager.getConnection
                ("jdbc:oracle:thin:@//192.168.178.42:1521:1521/TestDB", "system", "oracle");
        // @//machineName:port/SID,   userid,  password
        try {
            Statement stmt = conn.createStatement();
            try {
                ResultSet rset = stmt.executeQuery("select * from HELP");
                try {
                    while (rset.next())
                        System.out.println (rset.getString(1));   // Print col 1
                }
                finally {
                    try { rset.close(); } catch (Exception ignore) {}
                }
            }
            finally {
                try { stmt.close(); } catch (Exception ignore) {}
            }
        }
        finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
    }
}
