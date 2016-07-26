package de.th_nuernberg.harwedu.labcert.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.th_nuernberg.harwedu.labcert.interfaces.TaskCompleted;

/**
 * Created by Edu on 25.07.2016.
 */
public class GetRemoteAttdTask extends AsyncTask<String, Integer, String> {

    private static final String LOG_TAG = DataSource.class.getSimpleName();
    private static final String query = "SELECT * FROM ATTENDANCE";
    private static final String FAILED =
            "Verbindung zum Datenbankserver konnte nicht hergestellt werden";
    private static final String SUCCESS = "Datenbank synchronisiert";

    public TaskCompleted delegate = null;

    private ArrayList<HashMap<String, String>> data;

    private boolean fail = false;

    Context context;
    ProgressDialog prgDialog;

    public GetRemoteAttdTask(Context context) {
        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Datenbank wird synchronisiert. Bitte warten...");
        prgDialog.show();
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
            Log.d(LOG_TAG, "Versuche Verbindung herzustellen...");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.178.42:1521/orcl", "system",
                    "oracle");
            Log.d(LOG_TAG, "Verbindung hergestellt.");
            Statement stmt = con.createStatement();
            Log.d(LOG_TAG, "Führe Query aus...");
            ResultSet rs = stmt.executeQuery(query);
            Log.d(LOG_TAG, "...fertig.");
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columns = new ArrayList<>(rsmd.getColumnCount());
            Log.d(LOG_TAG, "Schreibe Column Names...");

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columns.add(rsmd.getColumnName(i));
                Log.d(LOG_TAG, rsmd.getColumnName(i));
            }
            Log.d(LOG_TAG, "...fertig");
            Log.d(LOG_TAG, "Schreibe Werte in Tabelle");

            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
                HashMap<String, String> row;
                row = new HashMap<>(columns.size());
                for (String col : columns) {
                    String s = rs.getString(col);
                    row.put(col, s);
                    Log.d(LOG_TAG, s + " in " + col);
                }
                data.add(row);
            }
            rs.close();
            stmt.close();
/*
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
*/
        } catch (SQLException e) {

            System.out.println("JDBC: Verbindung fehlgeschlagen! Fehlermeldung siehe Konsole");
            fail = true;
            e.printStackTrace();
            return null;

        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("JDBC: Verbindung fehlgeschlagen!");
            fail = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (prgDialog.isShowing())
            prgDialog.dismiss();
        if (fail)
            new AlertDialog.Builder(context).setMessage("Synchronisation fehlgeschlagen: Server " +
                    "nicht erreichbar").setNeutralButton("Schließen", null).show();
        else Toast.makeText(context, SUCCESS, Toast.LENGTH_SHORT).show();
        delegate.onTaskComplete(data);
    }
}
