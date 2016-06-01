package de.th_nuernberg.harwedu.labcert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.th_nuernberg.harwedu.labcert.csv.CsvParser;

/**
 * Created by Edu on 17.05.2016.
 */

/* TODO

- CreateStudent Fach-ID übergeben -> Switch-Case-Anweisung mit entsprechenden Einträgen
- cursorToStudent + getStudent anpassen
- joins

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
            StudentDbHelper.COLUMN_ATTD,
            StudentDbHelper.COLUMN_TASKS,
    };

    public StudentDataSource(Context context){
        Log.d(LOG_TAG, "StudentDataSource erzeugt jetzt dbHelper.");
        dbHelper = new StudentDbHelper(context);
    }

    public Student createStudent(String surname, String firstname, String comment,
                                 String group, String team, String matr,
                                 String bib, int attd, int tasks) {

        ContentValues valuesStudent = new ContentValues();
        ContentValues valuesAttendance = new ContentValues();
        ContentValues valuesTasks = new ContentValues();

        valuesStudent.put(StudentDbHelper.COLUMN_SURNAME, surname);
        valuesStudent.put(StudentDbHelper.COLUMN_FIRSTNAME, firstname);
        valuesStudent.put(StudentDbHelper.COLUMN_COMMENT, comment);
        valuesStudent.put(StudentDbHelper.COLUMN_LABGROUP, group);
        valuesStudent.put(StudentDbHelper.COLUMN_LABTEAM, team);
        valuesStudent.put(StudentDbHelper.COLUMN_MATR, matr);
        valuesStudent.put(StudentDbHelper.COLUMN_BIB, bib);
        valuesStudent.put(StudentDbHelper.COLUMN_ATTD, attd);
        valuesStudent.put(StudentDbHelper.COLUMN_TASKS, tasks);

        valuesAttendance.put(StudentDbHelper.COLUMN_MATR, matr);
        valuesAttendance.put(StudentDbHelper.COLUMN_ATTD1, 1);
        valuesAttendance.put(StudentDbHelper.COLUMN_ATTD2, 1);
        valuesAttendance.put(StudentDbHelper.COLUMN_ATTD3, 1);
        valuesAttendance.put(StudentDbHelper.COLUMN_ATTD4, 1);
        valuesAttendance.put(StudentDbHelper.COLUMN_ATTD5, 1);

        valuesTasks.put(StudentDbHelper.COLUMN_MATR, matr);
        valuesTasks.put(StudentDbHelper.COLUMN_TASK1, 1);
        valuesTasks.put(StudentDbHelper.COLUMN_TASK2, 1);
        valuesTasks.put(StudentDbHelper.COLUMN_TASK3, 1);
        valuesTasks.put(StudentDbHelper.COLUMN_TASK4, 1);
        valuesTasks.put(StudentDbHelper.COLUMN_TASK5, 1);

        long insertIdStudent = database.insert(StudentDbHelper.TABLE_STUDENT, null, valuesStudent);
        long insertIdAttd = database.insert(StudentDbHelper.TABLE_ATTENDANCE, null, valuesAttendance);

        /*
        Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
                columns, StudentDbHelper.COLUMN_ID + "=" + insertIdStudent,
                null, null, null, null);

        cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        cursor.close();
*/
        return getStudent(bib);
    }

    private Student cursorToStudent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(StudentDbHelper.COLUMN_ID);
        int idSurname = cursor.getColumnIndex(StudentDbHelper.COLUMN_SURNAME);
        int idFirstname = cursor.getColumnIndex(StudentDbHelper.COLUMN_FIRSTNAME);
        int idComment = cursor.getColumnIndex(StudentDbHelper.COLUMN_COMMENT);
        int idGroup = cursor.getColumnIndex(StudentDbHelper.COLUMN_LABGROUP);
        int idTeam = cursor.getColumnIndex(StudentDbHelper.COLUMN_LABTEAM);
        int idMatr = cursor.getColumnIndex(StudentDbHelper.COLUMN_MATR);
        int idBib = cursor.getColumnIndex(StudentDbHelper.COLUMN_BIB);
        int idAttd = cursor.getColumnIndex(StudentDbHelper.COLUMN_ATTD);
        int idTasks = cursor.getColumnIndex(StudentDbHelper.COLUMN_TASKS);

        String surname = cursor.getString(idSurname);
        String firstname = cursor.getString(idFirstname);
        String comment = cursor.getString(idComment);
        String group = cursor.getString(idGroup);
        String team = cursor.getString(idTeam);
        String matr = cursor.getString(idMatr);
        String bib = cursor.getString(idBib);
        int attd = cursor.getInt(idAttd);
        int task = cursor.getInt(idTasks);
        long id = cursor.getLong(idIndex);

        int[] attendance = new int[20];
        attendance[0] = attd;
        int[] tasks = new int[20];
        attendance[0] = task;


        Student student = new Student(surname, firstname, comment,
                group, team, matr, bib, attendance, tasks, id);

        return student;
    }

    private Student cursorToStudent_b(Cursor cursor) {
        int idSurname = cursor.getColumnIndex(StudentDbHelper.COLUMN_SURNAME);
        int idFirstname = cursor.getColumnIndex(StudentDbHelper.COLUMN_FIRSTNAME);
        int idAttd = cursor.getColumnIndex(StudentDbHelper.COLUMN_ATTD1);

        String surname = cursor.getString(idSurname);
        String firstname = cursor.getString(idFirstname);
        int attd1 = cursor.getInt(idAttd);

        int[] attendance = new int[1];
        attendance[0] = attd1;

        Student student = new Student(surname, firstname, attendance);

        return student;
    }

    public Student getStudent(String bibNo) {
        //Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
          //      columns, null, null, null, null, null);
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + "student_list" + " WHERE bib = '" + bibNo + "'", null);
        if (cursor != null)
            cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        cursor.close();
        close();
        return student;
    }

    /*
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();

        Cursor cursor = database.query(StudentDbHelper.TABLE_STUDENT,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Student student;

        while(!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            studentList.add(student);
            Log.d(LOG_TAG, "ID: " + student.getId() + ", Inhalt: " + student.getSurname()
                    + " " + student.getFirstname() + " " + student.getGroup());
            cursor.moveToNext();
        }

        cursor.close();

        return studentList;
    }*/


public List<Student> getAllStudents() {
    List<Student> studentList = new ArrayList<>();

    String query = "SELECT * FROM "
       /*     + StudentDbHelper.COLUMN_SURNAME + ", "
            + StudentDbHelper.COLUMN_FIRSTNAME + ", "
            + StudentDbHelper.COLUMN_ATTD1 + ", "
            + " FROM "*/
            + StudentDbHelper.TABLE_STUDENT + ", "
            + StudentDbHelper.TABLE_ATTENDANCE
            + " WHERE " + StudentDbHelper.TABLE_STUDENT + "."
            + StudentDbHelper.COLUMN_MATR + " = "
            + StudentDbHelper.TABLE_ATTENDANCE + "."
            + StudentDbHelper.COLUMN_MATR;

    Cursor cursor = database.rawQuery(query, null);
    cursor.moveToFirst();
    Student student;

    while(!cursor.isAfterLast()) {
        student = cursorToStudent_b(cursor);
        studentList.add(student);
        Log.d(LOG_TAG, "ID: " + student.getId() + ", Inhalt: " + student.getSurname()
                + " " + student.getFirstname() + " " + student.getGroup());
        cursor.moveToNext();
    }

    cursor.close();

    return studentList;
}

    public boolean studentExists(String bibNo) {
        openR();
        Cursor cursor = database.rawQuery("SELECT * FROM " + "student_list" + " WHERE bib = '" + bibNo + "'", null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        close();
        return exist;
    }

    public void updateComment(long id, String newComment) {
        openW();
        ContentValues values = new ContentValues();
        values.put(StudentDbHelper.COLUMN_COMMENT, newComment);

        database.update(StudentDbHelper.TABLE_STUDENT,
                values,
                StudentDbHelper.COLUMN_ID + "=" + id,
                null);

        close();
    }

    public void importCSV(Context context, String file_name) {
        InputStream instream = null;
        try {
            instream = context.getAssets().open(file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader instreamreader = new InputStreamReader(instream);
        BufferedReader buffer = new BufferedReader(instreamreader);
        String line;

        openW();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] str = line.split(",");

                ContentValues values = new ContentValues();
                values.put(StudentDbHelper.COLUMN_SURNAME, str[0]);
                values.put(StudentDbHelper.COLUMN_FIRSTNAME, str[1]);
                values.put(StudentDbHelper.COLUMN_COMMENT, str[2]);
                values.put(StudentDbHelper.COLUMN_LABGROUP, str[3]);
                values.put(StudentDbHelper.COLUMN_LABTEAM, str[4]);
                values.put(StudentDbHelper.COLUMN_MATR, str[5]);
                values.put(StudentDbHelper.COLUMN_BIB,str[6]);
                values.put(StudentDbHelper.COLUMN_ATTD, str[7]);
                values.put(StudentDbHelper.COLUMN_TASKS, str[8]);

                database.insert(StudentDbHelper.TABLE_STUDENT, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public void openW() {
        Log.d(LOG_TAG, "Referenz auf die Datenbank wird angefragt...");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void openR() {
        Log.d(LOG_TAG, "Referenz auf die Datenbank wird angefragt...");
        database = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank geschlossen.");
    }
}
