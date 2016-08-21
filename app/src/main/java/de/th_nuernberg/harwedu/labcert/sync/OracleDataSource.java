package de.th_nuernberg.harwedu.labcert.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.th_nuernberg.harwedu.labcert.interfaces.TaskCompleted;


/**
 * Created by Edu on 23.07.2016.
 * <p/>
 * TODO
 * <p/>
 * Weitere asynchrone Tasks einfügen
 */


public class OracleDataSource implements TaskCompleted {

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
    private static final String CON_FAILED = "Datenbank nicht erreichbar";
    private static final String SYNC_SUCCESS = "Datenbank synchronisiert";

    private Statement stmt = null;
    private ArrayList<HashMap<String, String>> data;

    Context context;


    /**
     * Konstruktor:
     * Erlaubt das Ausführen einer Netzwerk-Verbindung im Main-Thread
     * Zeigt Progress-Dialog
     */
    public OracleDataSource(Context app_context) {
        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        */
        context = app_context;
    }

    /**
     * Datensatz in Tabelle Anwesenheit einfügen
     *
     * @param matr     Matrikelnummer
     * @param ts       Zeistempel
     * @param editor   Editor
     * @param date_    Datum (Termin)
     * @param comment_ Kommentar
     * @param lab_id   Praktikums-ID
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
        InsertAttdTask insertAttdTask = new InsertAttdTask(queryString, context);
        insertAttdTask.execute();
        //insertAttdTask.runQuery(queryString);
        //conTask.closeCon();
    }

    @Override
    public void onTaskComplete(ArrayList<HashMap<String, String>> list) {
        if (list != null)
            Log.d(LOG_TAG, "Interface übergibt GEFÜLLTE ArrayList!");
        else
            Log.d(LOG_TAG, "Interface übergibt LEERE ArrayList!");
        this.data = list;
        assert list != null;
        System.out.println("HIER DER ULTIMATIVE TEST: " + list.get(0).get("MATR"));
        System.out.println("HIER DER NÄCHSTE ULTIMATIVE TEST: " + data.get(0).get("MATR"));
    }

    /**
     * Holt die Anwesenheits-Tabelle vom Server und speichert diese in der lokalen Arraylist 'data'
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public void getAttd() throws
            InterruptedException, ExecutionException, TimeoutException {
        GetRemoteAttdTask getRemoteAttdTask = new GetRemoteAttdTask(context);
        getRemoteAttdTask.delegate = this;
        getRemoteAttdTask.execute().get();
        //getRemoteAttdTask.get(10000, TimeUnit.MILLISECONDS);
        System.out.println("HIER DER VORLETZTE ULTIMATIVE TEST: " + data.get(0).get("MATR"));
    }

    /**
     * Getter: data
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }


    /**
     * Fügt Anwesenheit in Server-Datenbank ein
     */
    private class InsertAttdTask extends AsyncTask<String, Integer, String> {

        String query;

        public InsertAttdTask(String queryString, Context app_context) {
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

            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
