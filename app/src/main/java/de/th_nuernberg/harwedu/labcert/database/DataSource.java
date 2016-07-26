package de.th_nuernberg.harwedu.labcert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.interfaces.TaskCompleted;


/**
 * DataSource
 * <p/>
 * Diese Klasse enhält die Datenbank-Schnittstelle und wird als Datenquelle verwendet.
 * Sie ermöglicht CRUD Operationen.
 * <p/>
 * <p/>
 * TODO
 * - CreateStudent Fach-ID übergeben -> Switch-Case-Anweisung mit entsprechenden Einträgen
 * - cursorToStudent + getStudent anpassen
 * - Query (joins)
 */

public class DataSource implements TaskCompleted {
    private static final String LOG_TAG = DataSource.class.getSimpleName();
    private static final String YES = "yes";
    private static final String NO = "no";

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    private String columns[] = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_SURNAME,
            DbHelper.COLUMN_FIRSTNAME,
            DbHelper.COLUMN_COMMENT,
            DbHelper.COLUMN_LABGROUP,
            DbHelper.COLUMN_LABTEAM,
            DbHelper.COLUMN_MATR,
            DbHelper.COLUMN_BIB,
    };

    Context context;
    ArrayList<HashMap<String, String>> rs_list;

    /**
     * Konstruktor
     *
     * @param app_context Übergabe der ausführenden Activity
     */
    public DataSource(Context app_context) {
        context = app_context;
        Log.d(LOG_TAG, "DataSource erzeugt jetzt dbHelper.");
        dbHelper = new DbHelper(context);
    }

    /**
     * Datenbank mit Lese- und Schreibzugriff öffnen
     */
    public void openW() {
        Log.d(LOG_TAG, "Referenz auf die Datenbank wird angefragt...");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    /**
     * Datenbank nur mit Lesezugriff öffnen
     */
    public void openR() {
        Log.d(LOG_TAG, "Referenz auf die Datenbank wird angefragt...");
        database = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    /**
     * Datenbank schließen
     */
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank geschlossen.");
    }

    /**
     * Erzeugen eines Studenten in der Datenbank
     *
     * @param surname
     * @param firstname
     * @param comment
     * @param group
     * @param team
     * @param matr
     * @param bib
     * @return
     */
    public void createStudent(String surname, String firstname, String comment,
                              String group, String team, String matr,
                              String bib) {

        ContentValues valuesStudent = new ContentValues();
        ContentValues valuesAttendance = new ContentValues();
        ContentValues valuesTasks = new ContentValues();

        // Strings zu Testzwecken
        String editor = "17";
        String date = "2016-06-01";
        String upd_status = "no";
        String new_entry = "yes";

        // Tabelle Student füllen
        valuesStudent.put(DbHelper.COLUMN_SURNAME, surname);
        valuesStudent.put(DbHelper.COLUMN_FIRSTNAME, firstname);
        valuesStudent.put(DbHelper.COLUMN_COMMENT, comment);
        valuesStudent.put(DbHelper.COLUMN_LABGROUP, group);
        valuesStudent.put(DbHelper.COLUMN_LABTEAM, team);
        valuesStudent.put(DbHelper.COLUMN_MATR, matr);
        valuesStudent.put(DbHelper.COLUMN_BIB, bib);

        /*
        // Tabelle Anwesenheit füllen
        valuesAttendance.put(DbHelper.COLUMN_MATR, matr);
        valuesAttendance.put(DbHelper.COLUMN_TS,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        valuesAttendance.put(DbHelper.COLUMN_EDITOR, editor );
        valuesAttendance.put(DbHelper.COLUMN_DATE, date);
        valuesAttendance.put(DbHelper.COLUMN_COMMENT, comment);
        valuesAttendance.put(DbHelper.COLUMN_LAB_ID, upd_status);
        valuesAttendance.put(DbHelper.COLUMN_NEW_ENTRY, new_entry);

        valuesTasks.put(DbHelper.COLUMN_MATR, matr);
        */

        database.insert(DbHelper.TABLE_STUDENT, null, valuesStudent);
//        database.insert(DbHelper.TABLE_ATTENDANCE, null, valuesAttendance);

        /*
        Cursor cursor = database.query(DbHelper.TABLE_STUDENT,
                columns, DbHelper.COLUMN_ID + "=" + insertIdStudent,
                null, null, null, null);

        cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        cursor.close();
*/
        //return getStudent(bib);
    }

    /**
     * Löscht die Daten eines Studenten vollständig
     *
     * @param student zu löschender Student (Objekt)
     */
    public void deleteStudent(Student student) {
        long id = student.getId();
        openW();
        database.delete(DbHelper.TABLE_STUDENT,
                DbHelper.COLUMN_ID + "=" + id,
                null);
        close();
        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + student.toString());
    }

    /**
     * Erzeugt aus Datenbankeinträgen ein Objekt Student
     *
     * @param cursor Zeiger auf Tabelle
     * @return
     */
    private Student cursorToStudent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idSurname = cursor.getColumnIndex(DbHelper.COLUMN_SURNAME);
        int idFirstname = cursor.getColumnIndex(DbHelper.COLUMN_FIRSTNAME);
        int idComment = cursor.getColumnIndex(DbHelper.COLUMN_COMMENT);
        int idGroup = cursor.getColumnIndex(DbHelper.COLUMN_LABGROUP);
        int idTeam = cursor.getColumnIndex(DbHelper.COLUMN_LABTEAM);
        int idMatr = cursor.getColumnIndex(DbHelper.COLUMN_MATR);
        int idBib = cursor.getColumnIndex(DbHelper.COLUMN_BIB);

        String surname = cursor.getString(idSurname);
        String firstname = cursor.getString(idFirstname);
        String comment = cursor.getString(idComment);
        String group = cursor.getString(idGroup);
        String team = cursor.getString(idTeam);
        String matr = cursor.getString(idMatr);
        String bib = cursor.getString(idBib);
        long id = cursor.getLong(idIndex);

        int[] attendance = new int[20];
        int[] tasks = new int[20];

        /** Routine für
         for (int i=0; i<5; i++)
         attendance[i] = cursor.getInt(attd1+i++);
         for (int i=0; i<5; i++)
         attendance[i] = cursor.getInt(attd1+i++);
         */

        // Anwesenheit und Tasks auf 0 initialisieren
        for (int i = 0; i < 5; i++) {
            attendance[i] = 0;
            tasks[i] = 0;
        }

        return new Student(surname, firstname, comment,
                group, team, matr, bib, tasks, attendance, id);
    }

    /**
     * Routine, um den Datensatz eines bestimmten Studenten als Objekt zu erhalten.
     *
     * @param bibNo Übergabe der Bib.-Nr. (String)
     * @return Objekt Student, dass alle Daten enthält.
     */
    public Student getStudent(String bibNo) {
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_STUDENT +
                " WHERE " + DbHelper.COLUMN_BIB + " = '" + bibNo + "'", null);
        if (cursor != null)
            cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        assert cursor != null;
        cursor.close();
        close();
        return student;
    }

    /**
     * Routine, um aus der Datenbank Objekte für alle Studenten zu erzeugen.
     *
     * @return Liste aller Studenten (Objekte)
     */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<>();
        Student student;
        String query = "SELECT * FROM " + DbHelper.TABLE_STUDENT;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            student.setAttd(getAttdCount(student));
            studentList.add(student);
            Log.d(LOG_TAG, "ID: " + student.getId() + ", Inhalt: " + student.getSurname()
                    + " " + student.getFirstname() + " " + student.getGroup());
            cursor.moveToNext();
        }
        cursor.close();

        return studentList;
    }

    /**
     * Routine, die prüft, ob der Student bereits in der Datenbank existiert.
     *
     * @param bibNo Die Prüfung erfolgt anhand der übergebenen Bib.-Nr.
     * @return Boolean: true = vorhanden, false = nicht vorhanden
     */
    public boolean studentExists(String bibNo) {
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + "student_list" +
                " WHERE bib = '" + bibNo + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        close();
        return exists;
    }

    /**
     * Ermöglicht das Bearbeiten von Kommentaren in der App
     *
     * @param id
     * @param newComment
     */
    public void updateComment(long id, String newComment) {
        openW();
        ContentValues commentValue = new ContentValues();
        commentValue.put(DbHelper.COLUMN_COMMENT, newComment);

        database.update(DbHelper.TABLE_STUDENT,
                commentValue,
                DbHelper.COLUMN_ID + "=" + id,
                null);

        setSyncMissing(id);
        close();
    }

    /**
     * @param id
     */
    private void setSyncMissing(long id) {
        ContentValues updateValue = new ContentValues();
        updateValue.put(DbHelper.COLUMN_LAB_ID, "no");

        database.update(DbHelper.TABLE_ATTENDANCE,
                updateValue,
                DbHelper.COLUMN_ID + "=" + id,
                null);

        database.update(DbHelper.TABLE_TASKS,
                updateValue,
                DbHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void insertBib(Student student) {
        openW();
        String updateQuery = "Update " + DbHelper.TABLE_STUDENT +
                " set " + DbHelper.COLUMN_BIB + " = '" + student.getBib()
                + "' WHERE " + DbHelper.COLUMN_MATR + " = '" + student.getMatr() + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        close();
    }

    /**
     * Anwesenheit einfügen
     *
     * @param queryValues
     */
    public void insertAttd(HashMap<String, String> queryValues) {
        openW();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_MATR, queryValues.get("matr"));
        values.put(DbHelper.COLUMN_TS, queryValues.get("ts"));
        values.put(DbHelper.COLUMN_EDITOR, queryValues.get("editor"));
        values.put(DbHelper.COLUMN_DATE, queryValues.get("a_date"));
        values.put(DbHelper.COLUMN_COMMENT, queryValues.get("comment"));
        values.put(DbHelper.COLUMN_LAB_ID, queryValues.get("status"));
        values.put(DbHelper.COLUMN_NEW_ENTRY, queryValues.get("new_entry"));
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE + ";", null);
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    /**
     * Anwesenheit einfügen
     *
     * @param queryValues
     */
    public void insertAttd(HashMap<String, String> queryValues, String newEntry) {
        openW();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_MATR, queryValues.get("MATR"));
        values.put(DbHelper.COLUMN_TS, queryValues.get("TS"));
        values.put(DbHelper.COLUMN_EDITOR, queryValues.get("EDITOR"));
        values.put(DbHelper.COLUMN_DATE, queryValues.get("DATE_"));
        values.put(DbHelper.COLUMN_COMMENT, queryValues.get("COMMENT_"));
        values.put(DbHelper.COLUMN_COMMENT, queryValues.get("LAB_ID"));
        values.put(DbHelper.COLUMN_NEW_ENTRY, newEntry);
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    /**
     * Anwesenheit einfügen
     *
     * @param queryValues
     */
    public void insertAttd(ArrayList<HashMap<String, String>> queryValues, String newEntry) {
        openW();
        ContentValues values = new ContentValues();
        // Zeilenanzahl bestimmen!!!
        for (int i = 0; i < queryValues.size(); i++) {
            values.put(DbHelper.COLUMN_MATR,
                    String.valueOf(queryValues.get(i).get("MATR")));
            values.put(DbHelper.COLUMN_TS,
                    String.valueOf(queryValues.get(i).get("TS")));
            values.put(DbHelper.COLUMN_EDITOR,
                    String.valueOf(queryValues.get(i).get("EDITOR")));
            values.put(DbHelper.COLUMN_DATE,
                    String.valueOf(queryValues.get(i).get("DATE_")));
            values.put(DbHelper.COLUMN_COMMENT,
                    String.valueOf(queryValues.get(i).get("COMMENT_")));
            values.put(DbHelper.COLUMN_LAB_ID,
                    String.valueOf(queryValues.get(i).get("LAB_ID")));
            values.put(DbHelper.COLUMN_NEW_ENTRY, newEntry);
            database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        }
        close();
    }

    /**
     * Anwesenheit einfügen
     *
     * @param matr
     * @param editor
     */
    public void insertAttd(String matr, String editor) {
        openW();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_MATR, matr);
        values.put(DbHelper.COLUMN_TS,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(DbHelper.COLUMN_EDITOR, editor);
        values.put(DbHelper.COLUMN_DATE,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        values.put(DbHelper.COLUMN_COMMENT, "");
        values.put(DbHelper.COLUMN_LAB_ID, "no");
        values.put(DbHelper.COLUMN_NEW_ENTRY, "yes");
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    /**
     * Anwesenheit einfügen
     *
     * @param matr
     * @param editor
     */
    public void insertAttd(String matr, String editor, String date, String comment) {
        openW();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_MATR, matr);
        values.put(DbHelper.COLUMN_TS,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(DbHelper.COLUMN_EDITOR, editor);
        values.put(DbHelper.COLUMN_DATE,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        values.put(DbHelper.COLUMN_COMMENT, "");
        values.put(DbHelper.COLUMN_LAB_ID, "no");
        values.put(DbHelper.COLUMN_NEW_ENTRY, "yes");
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    /**
     * Ermittelt die Anzahl wahrgenommener Termine
     *
     * @param student
     * @return
     */
    public int[] getAttdCount(Student student) {
        int[] attdRecords = new int[5];
        for (int i = 0; i < 5; i++)
            attdRecords[i] = 0;
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_MATR + " = '" + student.getBib() + "'", null);
        int n = cursor.getCount();
        if (n > 5)
            n = 5;
        for (int i = 0; i < n; i++)
            attdRecords[i] = 1;
        cursor.close();
        close();
        return attdRecords;
    }

    /**
     * Synchronisiert neue Einträge in lokaler Datenbank mit Oracle-Datenbank
     * <p/>
     * Bedingung:
     * Spalte NEW_ENTRY = 1 und Kombination Matr+Datum in Oracle-DB nicht vorhanden
     */
    public boolean uploadNewAttdRecords() {
        Log.d(LOG_TAG, "Aufruf uploadNewAttdRecords");
        OracleDataSource oracleDS = new OracleDataSource(context);
        openR();
        // Abfrage lokal: Alle neuen Einträge in Anwesenheit
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_NEW_ENTRY + " = 'yes'", null);

        Log.d(LOG_TAG, "Neue Einträge: " + cursor.getCount());

        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idMatr = cursor.getColumnIndex(DbHelper.COLUMN_MATR);
        int idTS = cursor.getColumnIndex(DbHelper.COLUMN_TS);
        int idEditor = cursor.getColumnIndex(DbHelper.COLUMN_EDITOR);
        int idDate = cursor.getColumnIndex(DbHelper.COLUMN_DATE);
        int idComment = cursor.getColumnIndex(DbHelper.COLUMN_MATR);
        int idLab = cursor.getColumnIndex(DbHelper.COLUMN_MATR);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            oracleDS.insertAttd(cursor.getString(idMatr), cursor.getString(idTS),
                    cursor.getString(idEditor), cursor.getString(idDate),
                    cursor.getString(idComment), cursor.getString(idLab));

            Log.d(LOG_TAG, "Matr: " + cursor.getString(idMatr) +
                    ", TS: " + cursor.getString(idTS) +
                    ", Editor: " + cursor.getString(idEditor) +
                    ", Datum: " + cursor.getString(idDate) +
                    ", Kommentar: " + cursor.getString(idComment) +
                    ", Praktikum_ID: " + cursor.getString(idLab) +
                    "in Oracle DB eingefügt");

            updateAttdEntryStatus(cursor.getString(idIndex), NO);

            cursor.moveToNext();
        }
        //oracleDS.closeCon();
        cursor.close();
        close();
        return true;
    }

    /**
     * Überschreibt lokale Attendance-Einträge mit den Einträgen der Oracle-Datenbank
     *
     * @return
     */
    public void syncAttdToRemote() throws
            InterruptedException, ExecutionException, TimeoutException {
        Log.d(LOG_TAG, "Aufruf syncAttdWithRemote");
        deleteAttdRecords();
        GetRemoteAttdTask getRemoteAttdTask = new GetRemoteAttdTask(context);
        getRemoteAttdTask.delegate = this;
        getRemoteAttdTask.execute();
    }

    /**
     * Löscht die Anwesenheitsdaten zu einer Matrikelnummer
     *
     * @param student
     */
    public void deleteAttdRecords(Student student) {
        openW();

        database.delete(DbHelper.TABLE_ATTENDANCE,
                DbHelper.COLUMN_MATR + "= '" + student.getBib() + "'",
                null);

        /*
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_MATR + " = '" +  student.getBib() + "';", null);
                */
        close();
        Log.d(LOG_TAG, "Einträge gelöscht! Matrikel: " + student.getBib());
    }


    /**
     * Löscht ALLE Anwesenheitsdaten
     */
    public void deleteAttdRecords() {
        openW();
        database.delete(DbHelper.TABLE_ATTENDANCE, null, null);
        /*
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_MATR + " = '" +  student.getBib() + "';", null);
                */
        close();
        Log.d(LOG_TAG, "Alle Einträge gelöscht!");
    }

    /**
     * Routine, um CSV-Datei in Datenbank zu importieren.
     *
     * @param context
     * @param file_name
     */
    public void importCSV(Context context, String file_name) {
        InputStream instream = null;
        try {
            instream = context.getAssets().open(file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert instream != null;
        InputStreamReader instreamreader = new InputStreamReader(instream);
        BufferedReader buffer = new BufferedReader(instreamreader);
        String line;

        openW();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] str = line.split(context.getString(R.string.comma_split));

                createStudent(str[0], str[1], str[2], str[3], str[4], str[5], str[6]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * Funktion zum Abruf aller Anwesenheitsdaten als Hashmap
     *
     * @return Daten als String-Hashmap
     */
    public ArrayList<HashMap<String, String>> getAttendance() {

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DbHelper.TABLE_ATTENDANCE;

        openW();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idMatr = cursor.getColumnIndex(DbHelper.COLUMN_MATR);
        int idTimestamp = cursor.getColumnIndex(DbHelper.COLUMN_TS);
        int idEditor = cursor.getColumnIndex(DbHelper.COLUMN_EDITOR);
        int idDate = cursor.getColumnIndex(DbHelper.COLUMN_DATE);
        int idComment = cursor.getColumnIndex(DbHelper.COLUMN_COMMENT);
        int idNewEntry = cursor.getColumnIndex(DbHelper.COLUMN_NEW_ENTRY);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                //System.out.println("##############");
                map.put(DbHelper.COLUMN_ID, String.valueOf(cursor.getInt(idIndex)));
                //System.out.println(String.valueOf(cursor.getInt(idIndex)));
                map.put(DbHelper.COLUMN_MATR, cursor.getString(idMatr));
                //System.out.println(String.valueOf(cursor.getString(idMatr)));
                map.put(DbHelper.COLUMN_TS, cursor.getString(idTimestamp));
                //System.out.println(String.valueOf(cursor.getString(idTimestamp)));
                map.put(DbHelper.COLUMN_EDITOR, cursor.getString(idEditor));
                //System.out.println(String.valueOf(cursor.getString(idEditor)));
                map.put(DbHelper.COLUMN_DATE, cursor.getString(idDate));
                //System.out.println(String.valueOf(cursor.getString(idDate)));
                map.put(DbHelper.COLUMN_COMMENT, cursor.getString(idComment));
                //System.out.println(String.valueOf(cursor.getString(idComment)));
                map.put(DbHelper.COLUMN_NEW_ENTRY, cursor.getString(idNewEntry));
                //System.out.println(String.valueOf(cursor.getString(idNewEntry)));
                //System.out.println("##############");
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        close();
        return wordList;
    }

    /**
     * JSON erstellen
     */
    public String composeJSONfromSQLite() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(getAttendance());
    }

    /**
     * @return Anzahl der SQLite-Datensätze , die noch nicht synchronisiert wurden
     */
    public int dbSyncCount() {
        int count = 0;
        String selectQuery = "SELECT  * FROM " + DbHelper.TABLE_ATTENDANCE + " WHERE " +
                DbHelper.COLUMN_LAB_ID + " = 'no'";
        openW();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        close();
        //return count;
        return 1;
    }

    /**
     * Sync Status Update (auf 'yes')
     */
    public void updateSyncStatus(String id, String status) {
        openW();
        String updateQuery = "Update " + DbHelper.TABLE_ATTENDANCE +
                " set " + DbHelper.COLUMN_LAB_ID + " = '" + status
                + "' WHERE " + DbHelper.COLUMN_ID + " = '" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    /**
     * Tabelle Attendence: NEW_ENTRY auf parameter 'status' setzen
     * Achtung: Die Datenbank muss bereits geöffnet sein
     */
    private void updateAttdEntryStatus(String id, String status) {
        String updateQuery = "Update " + DbHelper.TABLE_ATTENDANCE +
                " set " + DbHelper.COLUMN_NEW_ENTRY + " = '" + status
                + "' WHERE " + DbHelper.COLUMN_ID + " = '" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }

    @Override
    public void onTaskComplete(ArrayList<HashMap<String, String>> result) {
        insertAttd(result, NO);
    }
}
