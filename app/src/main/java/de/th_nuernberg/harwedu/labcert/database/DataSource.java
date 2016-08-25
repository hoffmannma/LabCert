package de.th_nuernberg.harwedu.labcert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import de.th_nuernberg.harwedu.labcert.objects.Group;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.sync.GetRemoteAttdTask;


/**
 * Diese Klasse enhält die Datenbank-Schnittstelle und wird als Datenquelle verwendet.
 * Sie ermöglicht CRUD Operationen.
 *
 * TODO
 * - CreateStudent Fach-ID übergeben -> Switch-Case-Anweisung mit entsprechenden Einträgen
 * - cursorToStudent + getStudentByBib anpassen
 * - Query (joins)
 */

public class DataSource implements TaskCompleted {
    private static final String LOG_TAG = DataSource.class.getSimpleName();
    private static final String YES = "yes";
    private static final String NO = "no";

    private SQLiteDatabase database;
    private DbHelper dbHelper;

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
     * ***************************************************************
     *                             Teilnehmer
     * ***************************************************************
     */

    /**
     * Erzeugen eines Studenten in der Datenbank
     *
     * @param lab_name
     * @param group
     * @param term
     * @param title
     * @param surname
     * @param firstname
     * @param matr
     * @param email
     * @param comment
     * @param bib
     * @return
     */
    public void createStudent(String lab_name, String group, String term,
                              String title, String surname, String firstname,
                              String matr, String email, String comment, String bib) {
        ContentValues valuesStudent = new ContentValues();

        // Tabelle Student füllen
        valuesStudent.put(DbHelper.COLUMN_LAB_NAME, lab_name);
        valuesStudent.put(DbHelper.COLUMN_GROUP, group);
        valuesStudent.put(DbHelper.COLUMN_TERM, term);
        valuesStudent.put(DbHelper.COLUMN_TITLE, title);
        valuesStudent.put(DbHelper.COLUMN_SURNAME, surname);
        valuesStudent.put(DbHelper.COLUMN_FIRSTNAME, firstname);
        valuesStudent.put(DbHelper.COLUMN_MATR, matr);
        valuesStudent.put(DbHelper.COLUMN_EMAIL, email);
        valuesStudent.put(DbHelper.COLUMN_COMMENT, comment);
        valuesStudent.put(DbHelper.COLUMN_BIB, bib);
        valuesStudent.put(DbHelper.COLUMN_TS, getTimestamp());

        database.insert(DbHelper.TABLE_STUDENT, null, valuesStudent);

        Log.d(LOG_TAG, "Neuer Student " + firstname + " " + surname + " wurde angelegt.");
    }

    /**
     * Erzeugen eines Studenten in der Datenbank ohne die BIB-Nummer
     *
     * @param lab_name
     * @param group
     * @param term
     * @param title
     * @param surname
     * @param firstname
     * @param matr
     * @param email
     * @param comment
     * @return
     */
    public void createStudent(String lab_name, String group, String term,
                              String title, String surname, String firstname,
                              String matr, String email, String comment) {
        ContentValues valuesStudent = new ContentValues();

        // Tabelle Student füllen
        valuesStudent.put(DbHelper.COLUMN_LAB_NAME, lab_name);
        valuesStudent.put(DbHelper.COLUMN_GROUP, group);
        valuesStudent.put(DbHelper.COLUMN_TERM, term);
        valuesStudent.put(DbHelper.COLUMN_TITLE, title);
        valuesStudent.put(DbHelper.COLUMN_SURNAME, surname);
        valuesStudent.put(DbHelper.COLUMN_FIRSTNAME, firstname);
        valuesStudent.put(DbHelper.COLUMN_MATR, matr);
        valuesStudent.put(DbHelper.COLUMN_EMAIL, email);
        valuesStudent.put(DbHelper.COLUMN_COMMENT, comment);
        valuesStudent.put(DbHelper.COLUMN_TS, getTimestamp());
        database.insert(DbHelper.TABLE_STUDENT, null, valuesStudent);

        Log.d(LOG_TAG, "Neuer Student " + firstname + " " + surname + " wurde angelegt.");
    }

    /**
     * Löscht die Daten eines Studenten vollständig
     *
     * @param student zu löschender Student (Objekt)
     */
    public void deleteStudent(Student student) {
        long id = student.getId();
        database.delete(DbHelper.TABLE_STUDENT,
                DbHelper.COLUMN_ID + "=" + id,
                null);
        Log.d(LOG_TAG, "Student " + student.getFirstname() + " " + student.getSurname() + " wurde gelöscht!");
    }

    /**
     * Erzeugt aus Datenbankeinträgen ein Objekt Student
     *
     * @param cursor Zeiger auf Tabelle
     * @return
     */
    private Student cursorToStudent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idLabName = cursor.getColumnIndex(DbHelper.COLUMN_LAB_NAME);
        int idGroup = cursor.getColumnIndex(DbHelper.COLUMN_GROUP);
        int idTerm = cursor.getColumnIndex(DbHelper.COLUMN_TERM);
        int idTitle = cursor.getColumnIndex(DbHelper.COLUMN_TITLE);
        int idSurname = cursor.getColumnIndex(DbHelper.COLUMN_SURNAME);
        int idFirstname = cursor.getColumnIndex(DbHelper.COLUMN_FIRSTNAME);
        int idMatr = cursor.getColumnIndex(DbHelper.COLUMN_MATR);
        int idEmail = cursor.getColumnIndex(DbHelper.COLUMN_EMAIL);
        int idBib = cursor.getColumnIndex(DbHelper.COLUMN_BIB);
        int idComment = cursor.getColumnIndex(DbHelper.COLUMN_COMMENT);

        long id = cursor.getLong(idIndex);
        String labName = cursor.getString(idLabName);
        String group = cursor.getString(idGroup);
        String term = cursor.getString(idTerm);

        String title = cursor.getString(idTitle);
        String surname = cursor.getString(idSurname);
        String firstname = cursor.getString(idFirstname);
        String matr = cursor.getString(idMatr);
        String email = cursor.getString(idEmail);
        String bib = cursor.getString(idBib);
        String comment = cursor.getString(idComment);

        //neuen Studenten erzeugen
        Student student = new Student (id, labName, group, term, title, surname, firstname, matr, email, bib, comment);

        //aktuellen Fortschritt ermitteln
        student.setProgress(determine_progress(student));

        return student;
    }

    /**
     * Routine, um den Fortschritt eines einzelnen Studenten zu ermitteln
     *
     * @param student
     * @return Floatwert, wie weit der Student mit dem Praktikum fortgeschritten ist.
     */
    public float determine_progress(Student student) {
        float progress = 0;
        //TODO Fortschritt ermitteln

        return progress;
    }

    /**
     * Routine, um den Datensatz eines bestimmten Studenten als Objekt zu erhalten.
     *
     * @param matr Übergabe der Matr.-Nr. (String)
     * @return Objekt Student, dass alle Daten enthält.
     */
    public Student getStudentByMatr(String labName, String term, String matr) {
        Student student = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_STUDENT +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + labName + "' AND " +
                DbHelper.COLUMN_TERM + " = '" + term + "' AND " +
                DbHelper.COLUMN_MATR + " = '" + matr + "'", null);
        if (cursor != null && cursor.moveToFirst())
            student = cursorToStudent(cursor);
        assert cursor != null;
            cursor.close();
        return student;
    }

    /**
     * Routine, um den Datensatz eines bestimmten Studenten als Objekt zu erhalten.
     *
     * @param bibNo Übergabe der Bib.-Nr. (String)
     * @return Objekt Student, dass alle Daten enthält.
     */
    public Student getStudentByBib(String labName, String term, String bibNo) {
        Student student = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_STUDENT +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + labName + "' AND " +
                DbHelper.COLUMN_TERM + " = '" + term + "' AND " +
                DbHelper.COLUMN_BIB + " = '" + bibNo + "'", null);
        if (cursor != null && cursor.moveToFirst())
            student = cursorToStudent(cursor);
        assert cursor != null;
        cursor.close();
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

        Log.d(LOG_TAG, "Erstelle Liste mit allen Studenten --------------------------------------");

        while (!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            student.setProgress(determine_progress(student));
            studentList.add(student);
            Log.d(LOG_TAG, "Student " + student.getFirstname() + " " + student.getSurname() +
                    " aus dem Praktikum " + student.getLabName() + " Gruppe " +
                    student.getGroup() + " hinzugefügt.");
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(LOG_TAG, "Studentenliste erstellt -------------------------------------------------");

        return studentList;
    }

    /**
     *
     * @param lab
     * @param group
     * @return ArrayList<Student> in welchem alle lokalen Studenten eingetragen sind.
     */
    public ArrayList<Student> getStudentsFromGrp(String lab, String group) {
        ArrayList<Student> studentList = new ArrayList<>();
        Student student;
        String query = "SELECT * FROM " + DbHelper.TABLE_STUDENT + " WHERE "
                + DbHelper.COLUMN_LAB_NAME + " = '" + lab + "' AND "
                + DbHelper.COLUMN_GROUP + " = '" + group + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Log.d(LOG_TAG, "Erstelle Liste aus dem Praktikum " + lab + " Gruppe " + group + " ----------------");

        while (!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            student.setProgress(determine_progress(student));
            studentList.add(student);
            Log.d(LOG_TAG, "Student " + student.getFirstname() + " " + student.getSurname() +
                    " aus dem Praktikum " + student.getLabName() + " Gruppe " +
                    student.getGroup() + " hinzugefügt.");
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(LOG_TAG, "Studentenliste erstellt -------------------------------------------------");

        return studentList;
    }

    /**
     * Routine, die prüft, ob der Student bereits in der Datenbank existiert.
     * Die Prüfung erfolgt anhand der übergebenen Matr.-Nr.
     *
     * @param labName
     * @param term
     * @param bibNo
     * @return Boolean: true = vorhanden, false = nicht vorhanden
     */
    public boolean studentExistsByBib(String labName, String term, String bibNo) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_STUDENT +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + labName + "' AND " +
                DbHelper.COLUMN_TERM + " = '" + term + "' AND " +
                DbHelper.COLUMN_MATR + " = '" + bibNo + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        if (exists)
            Log.d(LOG_TAG, "Student existiert " + bibNo);
        else
            Log.d(LOG_TAG, "Student existiert nicht " + bibNo);

        return exists;
    }

    /**
     * Routine, die prüft, ob der Student bereits in der Datenbank existiert.
     * Die Prüfung erfolgt anhand der übergebenen Matr.-Nr.
     *
     * @param labName
     * @param term
     * @param matr
     * @return Boolean: true = vorhanden, false = nicht vorhanden
     */
    public boolean studentExistsByMatr(String labName, String term, String matr) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_STUDENT +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + labName + "' AND " +
                DbHelper.COLUMN_TERM + " = '" + term + "' AND " +
                DbHelper.COLUMN_MATR + " = '" + matr + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        if (exists)
            Log.d(LOG_TAG, "Student existiert " + matr);
        else
            Log.d(LOG_TAG, "Student existiert nicht " + matr);

        return exists;
    }

    /**
     * Ermöglicht das Bearbeiten von Kommentaren in der App
     *
     * @param id
     * @param newComment
     */
    public void updateCommentStudent(long id, String newComment) {
        ContentValues commentValue = new ContentValues();
        commentValue.put(DbHelper.COLUMN_COMMENT, newComment);

        database.update(DbHelper.TABLE_STUDENT,
                commentValue,
                DbHelper.COLUMN_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Neuer Kommentar des Studenten mit der ID " + String.valueOf(id) + " ist jetzt " + newComment);
    }

    /**
     * ***************************************************************
     *                             Anforderungen
     * ***************************************************************
     */

    /**
     * Funktion erstellt einen Eintrag in die Anforderungstabelle
     *
     * @param labName
     * @param group
     * @param term
     * @param type
     * @param count
     */
    public void createRequirement(String labName, String group, String term, String type,
                                  String count) {

        ContentValues valuesReq = new ContentValues();

        valuesReq.put(DbHelper.COLUMN_LAB_NAME, labName);
        valuesReq.put(DbHelper.COLUMN_GROUP, group);
        valuesReq.put(DbHelper.COLUMN_TERM, term);
        valuesReq.put(DbHelper.COLUMN_TYPE, type);
        valuesReq.put(DbHelper.COLUMN_COUNT, count);
        valuesReq.put(DbHelper.COLUMN_TS, getTimestamp());

        database.insert(DbHelper.TABLE_REQ, null, valuesReq);

        Log.d(LOG_TAG, "Neue Anforderung erstellt (" + labName + "|" + group + "|" + term + "|" + type + "|" + count + ")");
    }

    /**
     * @param labName
     * @param group
     * @param term
     * @return gibt eine ArrayList mit den Anforderungen der aktuellen Gruppe zurück
     */
    public ArrayList<Requirement> getGroupRequirements(String labName, String group, String term) {
        ArrayList<Requirement> reqList = new ArrayList<>();
        Requirement req;

        String query = "SELECT * FROM " + DbHelper.TABLE_REQ +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + labName + "' AND " +
                DbHelper.COLUMN_GROUP + " = '" + group + "' AND " +
                DbHelper.COLUMN_TERM + " = '" + term + "'";
        Cursor cursor = database.rawQuery(query, null);

        Log.d(LOG_TAG, "Erstelle Anforderungsliste (" + labName + "|" + group + "|" + term + ") -------------------------");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            req = cursorToRequirement(cursor);
            reqList.add(req);
            Log.d(LOG_TAG, "Füge Anforderung hinzu (" + req.getType() + ": " + req.getCount() + ")");
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(LOG_TAG, "Anforderungsliste erstellt (" + labName + "|" + group + "|" + term + ") -------------------------");

        return reqList;
    }

    /**
     * @return ArrayList mit allen Anforderungen
     */
    public ArrayList<Requirement> getAllRequirements() {
        ArrayList<Requirement> reqList = new ArrayList<>();
        Requirement req;

        String query = "SELECT * FROM " + DbHelper.TABLE_REQ;
        Cursor cursor = database.rawQuery(query, null);

        Log.d(LOG_TAG, "Alle Requirements geholt!");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            req = cursorToRequirement(cursor);
            reqList.add(req);
            cursor.moveToNext();
        }
        cursor.close();

        return reqList;
    }

    public void updateReq(Requirement req, String lab, String grp, String term){
        //TODO Definition
    }

    /**
     * @param cursor
     * @return Requirement
     */
    private Requirement cursorToRequirement(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idLabName = cursor.getColumnIndex(DbHelper.COLUMN_LAB_NAME);
        int idGroup = cursor.getColumnIndex(DbHelper.COLUMN_GROUP);
        int idTerm = cursor.getColumnIndex(DbHelper.COLUMN_TERM);
        int idType = cursor.getColumnIndex(DbHelper.COLUMN_TYPE);
        int idCount = cursor.getColumnIndex(DbHelper.COLUMN_COUNT);

        long id = cursor.getLong(idIndex);
        String lab_name = cursor.getString(idLabName);
        String group = cursor.getString(idGroup);
        String term = cursor.getString(idTerm);
        String type = cursor.getString(idType);
        String count = cursor.getString(idCount);

        return new Requirement(id, lab_name, group, term, type, count);
    }


    /**
     * ***************************************************************
     *                             Gruppen
     * ***************************************************************
     */

    /**
     *
     * @param lab_name
     * @param group
     * @param term
     * @param supervisor
     */
    public void createGroup(String lab_name, String group, String term,
                            String supervisor) {
        ContentValues valuesGroup = new ContentValues();

        valuesGroup.put(DbHelper.COLUMN_LAB_NAME, lab_name);
        valuesGroup.put(DbHelper.COLUMN_GROUP, group);
        valuesGroup.put(DbHelper.COLUMN_TERM, term);
        valuesGroup.put(DbHelper.COLUMN_SUPERVISOR, supervisor);
        valuesGroup.put(DbHelper.COLUMN_TS, getTimestamp());
        database.insert(DbHelper.TABLE_GROUP, null, valuesGroup);

        Log.d(LOG_TAG, "Gruppe erstellt (" + lab_name + "|" + group + "|" + term + "|" + supervisor + ")");
    }

    /**
     *
     * @param lab_name
     * @param group
     * @param term
     * @return boolean if Group exists
     */
    public boolean groupExists(String lab_name, String group, String term) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_GROUP +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + lab_name + "' AND "
                + DbHelper.COLUMN_GROUP + " = '" + group +"' AND "
                + DbHelper.COLUMN_TERM + " = '" + term +"'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        if (exists)
            Log.d(LOG_TAG, "Gruppe existiert (" + lab_name + "|" + group + "|" + term + ")");
        else
            Log.d(LOG_TAG, "Gruppe existiert nicht (" + lab_name + "|" + group + "|" + term + ")");

        return exists;
    }

    /**
     *
     * @param lab_id
     * @param group
     * @param term
     * @return boolean if Group exists
     */
    public boolean groupExistsByLabId(String lab_id, String group, String term) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DbHelper.TABLE_GROUP +
                " WHERE " + DbHelper.COLUMN_LAB_NAME + " = '" + lab_id + "' AND "
                + DbHelper.COLUMN_GROUP + " = '" + group +"' AND "
                + DbHelper.COLUMN_TERM + " = '" + term +"'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        if (exists)
            Log.d(LOG_TAG, "Gruppe existiert (" + lab_id + "|" + group + "|" + term + ")");
        else
            Log.d(LOG_TAG, "Gruppe existiert nicht (" + lab_id + "|" + group + "|" + term + ")");

        return exists;
    }

    /**
     *
     * @return
     */
    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groupList = new ArrayList<>();
        Group group;
        String query = "SELECT * FROM " + DbHelper.TABLE_GROUP;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Log.d(LOG_TAG, "Erstelle Liste mit allen Gruppen ----------------------------------------");

        while (!cursor.isAfterLast()) {
            group = cursorToGroup(cursor);
            groupList.add(group);
            Log.d(LOG_TAG, "Füge Gruppe hinzu (" + group.getLab_id() + "|" + group.getLab_name() + "|" +
                    group.getGroup() + "|" + group.getTerm() + "|" + group.getSupervisor() + ")");
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(LOG_TAG, "Liste mit allen Gruppen erstellt ----------------------------------------");

        return groupList;
    }

    /**
     *
     * @return Arraylist mit allen Gruppennamen
     */
    public ArrayList<String> getAllGroupNames() {
        ArrayList<String> groupList = new ArrayList<>();
        String groupName;
        String query = "SELECT * FROM " + DbHelper.TABLE_GROUP;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Log.d(LOG_TAG, "Erstelle Liste mit allen Gruppennamen -----------------------------------");

        while (!cursor.isAfterLast()) {
            groupName = cursorToGroupName(cursor);
            groupList.add(groupName);
            Log.d(LOG_TAG, "Füge Gruppe " + groupName + " hinzu");
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(LOG_TAG, "Liste mit allen Gruppennamen erstellt -----------------------------------");

        return groupList;
    }

    /**
     * @param cursor
     * @return neu erstellte Gruppe aus Datenbankeintrag(cursor)
     */
    private Group cursorToGroup(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idLabName = cursor.getColumnIndex(DbHelper.COLUMN_LAB_NAME);
        int idGroup = cursor.getColumnIndex(DbHelper.COLUMN_GROUP);
        int idTerm = cursor.getColumnIndex(DbHelper.COLUMN_TERM);
        int idSupervisor = cursor.getColumnIndex(DbHelper.COLUMN_SUPERVISOR);

        long id = cursor.getLong(idIndex);

        String lab_name = cursor.getString(idLabName);
        String group = cursor.getString(idGroup);
        String term = cursor.getString(idTerm);
        String supervisor = cursor.getString(idSupervisor);

        return new Group(id, lab_name, group, term, supervisor, getStudentsFromGrp(lab_name, group));
    }

    /**
     * @param cursor
     * @return String mit Gruppenname formatiert nach "[Nummer] _ Gruppenname"
     */
    private String cursorToGroupName(Cursor cursor) {
        int idLabName = cursor.getColumnIndex(DbHelper.COLUMN_LAB_NAME);
        int idGroup = cursor.getColumnIndex(DbHelper.COLUMN_GROUP);

        String labName = cursor.getString(idLabName);
        String group = cursor.getString(idGroup);

        return (labName + " " + group);
    }

    /**
     * ***************************************************************
     *                         Synchronisation
     * ***************************************************************
     */

    /*
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
    */

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
     */
    /*
    public void insertAttd(HashMap<String, String> queryValues) {
        openW();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_MATR, queryValues.get("matr"));
        values.put(DbHelper.COLUMN_TS, queryValues.get("ts"));
        values.put(DbHelper.COLUMN_EDITOR, queryValues.get("editor"));
        values.put(DbHelper.COLUMN_DATE, queryValues.get("a_date"));
        values.put(DbHelper.COLUMN_COMMENT, queryValues.get("comment"));
        values.put(DbHelper.COLUMN_LAB, queryValues.get("status"));
        values.put(DbHelper.COLUMN_NEW_ENTRY, queryValues.get("new_entry"));
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE + ";", null);
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }
*/
    /**
     * Anwesenheit einfügen
     *
     * @param queryValues
     */
    /*
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
    */

    /**
     * Anwesenheit einfügen
     *
     * @param queryValues
     */
    /*
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
            values.put(DbHelper.COLUMN_LAB,
                    String.valueOf(queryValues.get(i).get("LAB_ID")));
            values.put(DbHelper.COLUMN_NEW_ENTRY, newEntry);
            database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        }
        close();
    }
    */

    /**
     * Anwesenheit einfügen
     *
     * @param matr
     * @param editor
     */
    /*
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
        values.put(DbHelper.COLUMN_LAB, "no");
        values.put(DbHelper.COLUMN_NEW_ENTRY, "yes");
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }
*/

    /**
     * Anwesenheit einfügen
     *
     * @param matr
     * @param editor
     */
    /*
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
        values.put(DbHelper.COLUMN_LAB, "no");
        values.put(DbHelper.COLUMN_NEW_ENTRY, "yes");
        database.insert(DbHelper.TABLE_ATTENDANCE, null, values);
        close();
    }
*/

    /**
     * Ermittelt die Anzahl wahrgenommener Termine
     *
     * @param student
     * @return
     */
    /*
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
*/

    /**
     * Synchronisiert neue Einträge in lokaler Datenbank mit Oracle-Datenbank
     * <p/>
     * Bedingung:
     * Spalte NEW_ENTRY = 1 und Kombination Matr+Datum in Oracle-DB nicht vorhanden
     */
    /*
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
*/
    /**
     * Überschreibt lokale Attendance-Einträge mit den Einträgen der Oracle-Datenbank
     *
     * @return
     */
    public void syncAttdToRemote() throws
            InterruptedException, ExecutionException, TimeoutException {
        Log.d(LOG_TAG, "Aufruf syncAttdWithRemote");
        //deleteAttdRecords();
        GetRemoteAttdTask getRemoteAttdTask = new GetRemoteAttdTask(context);
        getRemoteAttdTask.delegate = this;
        getRemoteAttdTask.execute();
    }

    /**
     * Löscht die Anwesenheitsdaten zu einer Matrikelnummer
     *
     * @param student
     */
    /*
    public void deleteAttdRecords(Student student) {
        openW();
        database.delete(DbHelper.TABLE_ATTENDANCE,
                DbHelper.COLUMN_MATR + "= '" + student.getBib() + "'",
                null);
        /*
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_MATR + " = '" +  student.getBib() + "';", null);
                *//*
        close();
        Log.d(LOG_TAG, "Einträge gelöscht! Matrikel: " + student.getBib());
    }
*/

    /**
     * Löscht ALLE Anwesenheitsdaten
     */
    /*
    public void deleteAttdRecords() {
        openW();
        database.delete(DbHelper.TABLE_ATTENDANCE, null, null);
        /*
        database.rawQuery("DELETE FROM " + DbHelper.TABLE_ATTENDANCE +
                " WHERE " + DbHelper.COLUMN_MATR + " = '" +  student.getBib() + "';", null);
                *//*
        close();
        Log.d(LOG_TAG, "Alle Einträge gelöscht!");
    }
    */

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
                //TODO Parameter anpassen
                // Vergleich: labname, semester, matrikel
                if (!studentExistsByMatr(str[0],str[2], str[6]))
                    createStudent(str[0], str[1], str[2], str[3], str[4], str[5], str[6],
                            str[7], str[8]);
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
    /*
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
    */


    /**
     * JSON erstellen
     */
    /*
    public String composeJSONfromSQLite() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(getAttendance());
    }*/

    /**
     * @return Anzahl der SQLite-Datensätze , die noch nicht synchronisiert wurden
     */
    /*
    public int dbSyncCount() {
        int count = 0;
        String selectQuery = "SELECT  * FROM " + DbHelper.TABLE_ATTENDANCE + " WHERE " +
                DbHelper.COLUMN_LAB + " = 'no'";
        openW();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        close();
        //return count;
        return 1;
    }*/

    /**
     * Sync Status Update (auf 'yes')
     */
    /*
    public void updateSyncStatus(String id, String status) {
        openW();
        String updateQuery = "Update " + DbHelper.TABLE_ATTENDANCE +
                " set " + DbHelper.COLUMN_LAB + " = '" + status
                + "' WHERE " + DbHelper.COLUMN_ID + " = '" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
*/
    /**
     * Tabelle Attendence: NEW_ENTRY auf parameter 'status' setzen
     * Achtung: Die Datenbank muss bereits geöffnet sein
     */
    /*
    private void updateAttdEntryStatus(String id, String status) {
        String updateQuery = "Update " + DbHelper.TABLE_ATTENDANCE +
                " set " + DbHelper.COLUMN_NEW_ENTRY + " = '" + status
                + "' WHERE " + DbHelper.COLUMN_ID + " = '" + id + "'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
    }
*/

    @Override
    public void onTaskComplete(ArrayList<HashMap<String, String>> result) {
        //TODO Was tun wenn Sync fertig
        //insertAttd(result, NO);
    }

    public String getTimestamp(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
