package de.th_nuernberg.harwedu.labcert.database;

import android.os.AsyncTask;

import java.sql.*;

/**
 * Created by Edu on 22.07.2016.
 */
public class OracleJDBC extends AsyncTask<String, Void, String>{


    @Override
    protected String doInBackground(String... strings) {
        System.out.println("JDBC: Datenbankverbindung wird getestet... ");

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {

            System.out.println("JDBC: Treiber nicht gefunden!");
            e.printStackTrace();
            return null;

        }

        System.out.println("JDBC: Treiber registriert!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.178.42:1521/orcl", "system",
                    "oracle");

        } catch (SQLException e) {

            System.out.println("JDBC: Verbindung fehlgeschlagen! Fehlermeldung siehe Konsole");
            e.printStackTrace();
            return null;

        }

        if (connection != null) {
            System.out.println("JDBC: Verbindung erfolgreich hergestellt!");
        } else {
            System.out.println("JDBC: Verbindung fehlgeschlagen!");
        }
        return null;
    }
}
