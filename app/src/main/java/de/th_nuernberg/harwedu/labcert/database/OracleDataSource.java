package de.th_nuernberg.harwedu.labcert.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by Edu on 23.07.2016.
 *
 * TODO
 *
 * Weitere asynchrone Tasks einfügen
 */


public class OracleDataSource {

    /**
     * Spaltennamen Oracle-Datenbank
     */
    public static final String COLUMN_MATR = "MATR";
    public static final String COLUMN_TIMESTAMP = "TS";
    public static final String COLUMN_EDITOR = "EDITOR";
    public static final String COLUMN_DATE = "DATE_";
    public static final String COLUMN_COMMENT = "COMMENT_";
    public static final String COLUMN_LAB_ID = "LAB_ID";

    private static final String LOG_TAG = OracleDataSource.class.getSimpleName();
    private static final String TABLE_ATTD = "Attendance";
    private static final String TABLE_TASKS = "Tasks";

    //private Connection con;
    private Statement stmt = null;
    private ResultSet rs = null;

    ProgressDialog prgDialog;
    Context context;


    /**
     * Konstruktor:
     * Erlaubt das Ausführen einer Netzwerk-Verbindung im Main-Thread
     * Zeigt Progress-Dialog
     */
    public OracleDataSource(Context app_context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        context = app_context;
    }

    /**
     * Datensatz in Tabelle Anwesenheit einfügen
     *
     * @param matr      Matrikelnummer
     * @param ts        Zeistempel
     * @param editor    Editor
     * @param date_     Datum (Termin)
     * @param comment_  Kommentar
     * @param lab_id    Praktikums-ID
     */
    public void insertAttd(String matr, String ts, String editor, String date_,
                           String comment_, String lab_id) {
        String queryString = "Insert into " + TABLE_ATTD +
                " (" + COLUMN_MATR + "," +
                COLUMN_TIMESTAMP + "," +
                COLUMN_EDITOR + "," +
                COLUMN_DATE + "," +
                COLUMN_COMMENT + "," +
                COLUMN_LAB_ID + ") values ('" +
                matr + "','" + ts + "','" + editor + "','" +
                date_ + "','" + comment_ + "','" + lab_id +
                "')";
        System.out.println("OracleDataSource Query: " + queryString);
        InsertAttdTask insertAttdTask = new InsertAttdTask(queryString);
        insertAttdTask.execute();
        //insertAttdTask.runQuery(queryString);
        //conTask.closeCon();
    }



    private class InsertAttdTask extends AsyncTask<String, Integer, String>{

        String query;

        public InsertAttdTask(String queryString) {
            query = queryString;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection con = null;
            System.out.println("JDBC: Datenbankverbindung wird getestet... ");
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");

            } catch (ClassNotFoundException e) {

                System.out.println("JDBC: Treiber nicht gefunden!");
                e.printStackTrace();
                return null;

            }

            System.out.println("JDBC: Treiber registriert!");

            try {

                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@192.168.178.42:1521/orcl", "system",
                        "oracle");
                stmt = con.createStatement();
                stmt.executeQuery(query);
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {

                System.out.println("JDBC: Verbindung fehlgeschlagen! Fehlermeldung siehe Konsole");
                e.printStackTrace();
                return null;

            }

            if (con!= null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("JDBC: Verbindung fehlgeschlagen!");
            }
            return null;
        }
    }
}
