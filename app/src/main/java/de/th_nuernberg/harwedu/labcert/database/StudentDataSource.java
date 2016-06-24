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

import de.th_nuernberg.harwedu.labcert.R;


/**
 * StudentDataSource
 *
 * Diese Klasse enhält die Datenbank-Schnittstelle und wird als Datenquelle verwendet.
 * Sie ermöglicht CRUD Operationen.
 *
 *
 * TODO
 * - CreateStudent Fach-ID übergeben -> Switch-Case-Anweisung mit entsprechenden Einträgen
 * - cursorToStudent + getStudent anpassen
 * - Query (joins)
 *
 */

public class StudentDataSource {
    private static final String LOG_TAG = StudentDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private StudentDbHelper dbHelper;

    private String columns[] = {
            StudentDbHelper.COLUMN_ID,
            StudentDbHelper.COLUMN_SURNAME,
            StudentDbHelper.COLUMN_FIRSTNAME,
            StudentDbHelper.COLUMN_COMMENT,
            StudentDbHelper.COLUMN_LABGROUP,
            StudentDbHelper.COLUMN_LABTEAM,
            StudentDbHelper.COLUMN_MATR,
            StudentDbHelper.COLUMN_BIB,
    };

    /**
     * Konstruktor
     *
     * @param context Übergabe der ausführenden Activity
     */
    public StudentDataSource(Context context){
        Log.d(LOG_TAG, "StudentDataSource erzeugt jetzt dbHelper.");
        dbHelper = new StudentDbHelper(context);
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
     *  Erzeugen eines Studenten in der Datenbank
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
        valuesStudent.put(StudentDbHelper.COLUMN_SURNAME, surname);
        valuesStudent.put(StudentDbHelper.COLUMN_FIRSTNAME, firstname);
        valuesStudent.put(StudentDbHelper.COLUMN_COMMENT, comment);
        valuesStudent.put(StudentDbHelper.COLUMN_LABGROUP, group);
        valuesStudent.put(StudentDbHelper.COLUMN_LABTEAM, team);
        valuesStudent.put(StudentDbHelper.COLUMN_MATR, matr);
        valuesStudent.put(StudentDbHelper.COLUMN_BIB, bib);

        /*
        // Tabelle Anwesenheit füllen
        valuesAttendance.put(StudentDbHelper.COLUMN_MATR, matr);
        valuesAttendance.put(StudentDbHelper.COLUMN_TS,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        valuesAttendance.put(StudentDbHelper.COLUMN_EDITOR, editor );
        valuesAttendance.put(StudentDbHelper.COLUMN_DATE, date);
        valuesAttendance.put(StudentDbHelper.COLUMN_COMMENT, comment);
        valuesAttendance.put(StudentDbHelper.COLUMN_UPDATE_STATUS, upd_status);
        valuesAttendance.put(StudentDbHelper.COLUMN_NEW_ENTRY, new_entry);

        valuesTasks.put(StudentDbHelper.COLUMN_MATR, matr);
        */

        database.insert(StudentDbHelper.TABLE_STUDENT, null, valuesStudent);
//        database.insert(StudentDbHelper.TABLE_ATTENDANCE, null, valuesAttendance);

        /*
        Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
                columns, StudentDbHelper.COLUMN_ID + "=" + insertIdStudent,
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
     * @param student   zu löschender Student (Objekt)
     */
    public void deleteStudent(Student student) {
        long id = student.getId();
        openW();
        database.delete(StudentDbHelper.TABLE_STUDENT,
                StudentDbHelper.COLUMN_ID + "=" + id,
                null);
        close();
        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + student.toString());
    }


    /**
     * Erzeugt aus Datenbankeinträgen ein Objekt Student
     *
     * @param cursor    Zeiger auf Tabelle
     *
     * @return
     */
    private Student cursorToStudent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(StudentDbHelper.COLUMN_ID);
        int idSurname = cursor.getColumnIndex(StudentDbHelper.COLUMN_SURNAME);
        int idFirstname = cursor.getColumnIndex(StudentDbHelper.COLUMN_FIRSTNAME);
        int idComment = cursor.getColumnIndex(StudentDbHelper.COLUMN_COMMENT);
        int idGroup = cursor.getColumnIndex(StudentDbHelper.COLUMN_LABGROUP);
        int idTeam = cursor.getColumnIndex(StudentDbHelper.COLUMN_LABTEAM);
        int idMatr = cursor.getColumnIndex(StudentDbHelper.COLUMN_MATR);
        int idBib = cursor.getColumnIndex(StudentDbHelper.COLUMN_BIB);

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
        for (int i=0;i<5;i++){
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
     *
     * @return Objekt Student, dass alle Daten enthält.
     */
    public Student getStudent(String bibNo) {
        //Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
          //      columns, null, null, null, null, null);
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + StudentDbHelper.TABLE_STUDENT +
                " WHERE " + StudentDbHelper.COLUMN_BIB + " = '" + bibNo + "'", null);
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
     * @return  Liste aller Studenten (Objekte)
     */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<>();
    /*
        String query = "SELECT * FROM "
           /*     + StudentDbHelper.COLUMN_SURNAME + ", "
                + StudentDbHelper.COLUMN_FIRSTNAME + ", "
                + StudentDbHelper.COLUMN_ATTD1 + ", "
                + " FROM "
                + StudentDbHelper.TABLE_STUDENT/* + ", "
                + StudentDbHelper.TABLE_ATTENDANCE
                + " WHERE " + StudentDbHelper.TABLE_STUDENT + "."
                + StudentDbHelper.COLUMN_MATR + " = "
                + StudentDbHelper.TABLE_ATTENDANCE + "."
                + StudentDbHelper.COLUMN_MATR; */

        String query = "SELECT * FROM " + StudentDbHelper.TABLE_STUDENT;

        Cursor cursor = database.rawQuery(query, null);

        /* Query mit StudentDBHelper
        Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
                columns, null, null, null, null, null);
        */

        cursor.moveToFirst();
        Student student;

        while(!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            student.setAttd(getAttdRecord(student));
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
     *
     * @return  Boolean: true = vorhanden, false = nicht vorhanden
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

    public void updateComment(long id, String newComment) {
        openW();
        ContentValues commentValue = new ContentValues();
        commentValue.put(StudentDbHelper.COLUMN_COMMENT, newComment);

        database.update(StudentDbHelper.TABLE_STUDENT,
                commentValue,
                StudentDbHelper.COLUMN_ID + "=" + id,
                null);

        setSyncMissing(id);
        close();
    }

    public void updateAttendance(){

    }

    public void updateTask(){

    }

    private void setSyncMissing(long id){
        ContentValues updateValue = new ContentValues();
        updateValue.put(StudentDbHelper.COLUMN_UPDATE_STATUS, "no");

        database.update(StudentDbHelper.TABLE_ATTENDANCE,
                updateValue,
                StudentDbHelper.COLUMN_ID + "=" + id,
                null);

        database.update(StudentDbHelper.TABLE_TASKS,
                updateValue,
                StudentDbHelper.COLUMN_ID + "=" + id,
                null);
    }

    /**
     * Anwesenheit einfügen
     * @param queryValues
     */
    public void updateAttd(HashMap<String, String> queryValues) {
        openW();
        ContentValues values = new ContentValues();
        values.put(StudentDbHelper.COLUMN_MATR, queryValues.get("matr"));
        values.put(StudentDbHelper.COLUMN_TS, queryValues.get("ts"));
        values.put(StudentDbHelper.COLUMN_EDITOR, queryValues.get("editor"));
        values.put(StudentDbHelper.COLUMN_DATE, queryValues.get("a_date"));
        values.put(StudentDbHelper.COLUMN_COMMENT, queryValues.get("comment"));
        values.put(StudentDbHelper.COLUMN_UPDATE_STATUS, queryValues.get("status"));
        values.put(StudentDbHelper.COLUMN_NEW_ENTRY, queryValues.get("new_entry"));
        database.rawQuery("DELETE FROM " + StudentDbHelper.TABLE_ATTENDANCE + ";", null);
        database.insert(StudentDbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    public void insertAttd(String matr, String editor){
        openW();
        ContentValues values = new ContentValues();
        values.put(StudentDbHelper.COLUMN_MATR, matr);
        values.put(StudentDbHelper.COLUMN_TS,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(StudentDbHelper.COLUMN_EDITOR, editor);
        values.put(StudentDbHelper.COLUMN_DATE,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        values.put(StudentDbHelper.COLUMN_COMMENT, "");
        values.put(StudentDbHelper.COLUMN_UPDATE_STATUS, "no");
        values.put(StudentDbHelper.COLUMN_NEW_ENTRY, "yes");
        database.insert(StudentDbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }

    public int[] getAttdRecord(Student student){
        int[] attdRecords = new int[5];
        for (int i=0; i<5; i++)
            attdRecords[i] = 0;
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + StudentDbHelper.TABLE_ATTENDANCE +
                " WHERE " + StudentDbHelper.COLUMN_MATR + " = '" + student.getBib() + "'", null);
        int n = cursor.getCount();
        if (n>5)
            n = 5;
        for (int i=0; i<n; i++)
            attdRecords[i] = 1;
        cursor.close();
        close();
        return attdRecords;
    }

    /**
     * Löscht die Anwesenheitsdaten zu einer Matrikelnummer
     *
     * @param student
     */
    public void deleteAttdRecords(Student student) {
        openW();

        database.delete(StudentDbHelper.TABLE_ATTENDANCE,
                StudentDbHelper.COLUMN_MATR + "= '" + student.getBib() + "'",
                null);

        /*
        database.rawQuery("DELETE FROM " + StudentDbHelper.TABLE_ATTENDANCE +
                " WHERE " + StudentDbHelper.COLUMN_MATR + " = '" +  student.getBib() + "';", null);
                */
        close();
        Log.d(LOG_TAG, "Einträge gelöscht! Matrikel: " + student.getBib());
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
     * Funktion zum Abruf aller Anwesenheitsdaten
     *
     * @return  Daten als String-Hashmap
     */
    public ArrayList<HashMap<String, String>> getAttendance(){

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + StudentDbHelper.TABLE_ATTENDANCE;

        openW();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int idIndex = cursor.getColumnIndex(StudentDbHelper.COLUMN_ID);
        int idMatr = cursor.getColumnIndex(StudentDbHelper.COLUMN_MATR);
        int idTimestamp = cursor.getColumnIndex(StudentDbHelper.COLUMN_TS);
        int idEditor = cursor.getColumnIndex(StudentDbHelper.COLUMN_EDITOR);
        int idDate = cursor.getColumnIndex(StudentDbHelper.COLUMN_DATE);
        int idComment = cursor.getColumnIndex(StudentDbHelper.COLUMN_COMMENT);
        int idNewEntry = cursor.getColumnIndex(StudentDbHelper.COLUMN_NEW_ENTRY);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                //System.out.println("##############");
                map.put(StudentDbHelper.COLUMN_ID, String.valueOf(cursor.getInt(idIndex)));
                //System.out.println(String.valueOf(cursor.getInt(idIndex)));
                map.put(StudentDbHelper.COLUMN_MATR, cursor.getString(idMatr));
                //System.out.println(String.valueOf(cursor.getString(idMatr)));
                map.put(StudentDbHelper.COLUMN_TS, cursor.getString(idTimestamp));
                //System.out.println(String.valueOf(cursor.getString(idTimestamp)));
                map.put(StudentDbHelper.COLUMN_EDITOR, cursor.getString(idEditor));
                //System.out.println(String.valueOf(cursor.getString(idEditor)));
                map.put(StudentDbHelper.COLUMN_DATE, cursor.getString(idDate));
                //System.out.println(String.valueOf(cursor.getString(idDate)));
                map.put(StudentDbHelper.COLUMN_COMMENT, cursor.getString(idComment));
                //System.out.println(String.valueOf(cursor.getString(idComment)));
                map.put(StudentDbHelper.COLUMN_NEW_ENTRY, cursor.getString(idNewEntry));
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
    public String composeJSONfromSQLite(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(getAttendance());
    }

    /**
     * @return
     * Anzahl der SQLite-Datensätze , die noch nicht synchronisiert wurden
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM " + StudentDbHelper.TABLE_ATTENDANCE + " WHERE " +
                StudentDbHelper.COLUMN_UPDATE_STATUS + " = 'no'";
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
    public void updateSyncStatus(String id, String status){
        openW();
        String updateQuery = "Update " + StudentDbHelper.TABLE_ATTENDANCE +
                " set " + StudentDbHelper.COLUMN_UPDATE_STATUS + " = '" + status
                + "' WHERE " + StudentDbHelper.COLUMN_ID + " = '" + id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

}
