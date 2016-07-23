package de.th_nuernberg.harwedu.labcert.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * TODO
 *
 */


public class StudentDbHelper extends SQLiteOpenHelper {

    /**
     * Konstanten
     */
    private static final String LOG_TAG = StudentDbHelper.class.getSimpleName();
    public static final String DB_NAME = "labcert_group.db";
    public static final int DB_VERSION = 1;

    /**
     * Konstanten: Tabellen
     *
     * - Studentendaten
     * - Anwesenheit
     * - Aufgaben
     * - Praktika (Fächer)ww
     */
    public static final String TABLE_STUDENT = "student_list";
    public static final String TABLE_ATTENDANCE = "attendance_list";
    public static final String TABLE_TASKS = "task_list";
    public static final String TABLE_LABS = "lab_list";

    /**
     * Konstanten: Gemeinsame verwendete Spaltennamen
     */
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MATR = "matr";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_EDITOR = "editor";
    public static final String COLUMN_TS = "timestamp";
    public static final String COLUMN_UPDATE_STATUS = "update_status";
    public static final String COLUMN_NEW_ENTRY = "new_entry";

    /**
     * Konstanten: Spalten Tabelle Student
     *
     * 1. ID
     * 2. Nachname
     * 3. Vorname
     * 4. Kommentar
     * 5. Praktikumsgruppe
     * 6. Laborteam
     * 7. Matrikelnummer
     * 8. Bibliotheksnummer
     */
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LABGROUP = "labgroup";
    public static final String COLUMN_LABTEAM = "labteam";
    public static final String COLUMN_BIB = "bib";

    /**
     * Konstanten: Spalten Tabelle Anwesenheit
     *
     * 1. ID
     * 2. Matrikelnummer
     * 3. Zeitstempel
     * 4. Verfasser
     * 5. Datum (Anwesenheit an Termin)
     * 6. Kommentar
     * 7. Aktualisiert (ja/nein)*
     */
    public static final String COLUMN_DATE = "a_date";

    /**
     * Konstanten: Spalten Tabelle Aufgaben
     *
     * 1. ID
     * 2. Matrikelnummer
     * 3. Zeitstempel
     * 4. Verfasser
     * 5. Aufgabe
     * 6. Status (erledigt / nicht erledigt / in Korrektur / Nachbesserung erforderderlich)
     * 7. Kommentar
     * 8. Aktualisiert (ja/nein)
     */
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_STATUS = "status";

    /**
     * Konstanten: Spaltennamen Tabelle Fächer
     *
     * 1. ID
     * 2. Praktikum / Fach
     * 3. Betreuer
     * 4. Minimale Anwesenheit
     * 5. Mindestaufgabenzahl
     * 6. Termine
     * 7. Pflichttermine
     * 8. Pflichtaufgaben
     * 9. Mindestpunktzahl (Informatik)
     * 10. Zeitstempel
     * 11. Verfasser
     * 12. Kommentar
     * 13. Aktualisiert (ja/nein)
     */
    public static final String COLUMN_LAB = "lab";
    public static final String COLUMN_SUPERVISOR = "supervisor";
    public static final String COLUMN_MIN_ATTD = "min_attd";
    public static final String COLUMN_MIN_TASKS = "min_tasks";
    public static final String COLUMN_DATES = "dates";
    public static final String COLUMN_COMP_DATES = "comp_dates";
    public static final String COLUMN_COMP_TASKS = "comp_tasks";
    public static final String COLUMN_MIN_SCORE = "min_score";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Studenten'
     */
    public static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + TABLE_STUDENT +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SURNAME + " TEXT NOT NULL, " +
                    COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_LABGROUP + " TEXT NOT NULL, " +
                    COLUMN_LABTEAM + " TEXT NOT NULL, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_BIB + " TEXT NOT NULL);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Anwesenheit'
     */
    public static final String CREATE_ATTENDANCE_TABLE =
            "CREATE TABLE " + TABLE_ATTENDANCE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT NOT NULL, " +
                    COLUMN_EDITOR + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_UPDATE_STATUS + " TEXT NOT NULL, " +
                    COLUMN_NEW_ENTRY + " TEXT NOT NULL);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Aufgaben'
     */
    public static final String CREATE_TASK_TABLE =
            "CREATE TABLE " + TABLE_TASKS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT NOT NULL, " +
                    COLUMN_EDITOR + " TEXT NOT NULL, " +
                    COLUMN_TASK + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_UPDATE_STATUS + " TEXT NOT NULL, " +
                    COLUMN_NEW_ENTRY + " TEXT NOT NULL);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Praktika'
     */
    public static final String CREATE_LAB_TABLE =
            "CREATE TABLE " + TABLE_LABS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAB + " TEXT NOT NULL, " +
                    COLUMN_SUPERVISOR + " TEXT NOT NULL, " +
                    COLUMN_MIN_ATTD + " TEXT NOT NULL, " +
                    COLUMN_MIN_TASKS + " TEXT NOT NULL, " +
                    COLUMN_DATES + " TEXT NOT NULL, " +
                    COLUMN_COMP_DATES + " TEXT NOT NULL, " +
                    COLUMN_COMP_TASKS + " TEXT NOT NULL, " +
                    COLUMN_MIN_SCORE + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT NOT NULL, " +
                    COLUMN_EDITOR + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_UPDATE_STATUS + " TEXT NOT NULL);";

    public StudentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "StudentDbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + CREATE_STUDENT_TABLE + " angelegt.");
            db.execSQL(CREATE_STUDENT_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + CREATE_ATTENDANCE_TABLE + " angelegt.");
            db.execSQL(CREATE_ATTENDANCE_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + CREATE_TASK_TABLE + " angelegt.");
            db.execSQL(CREATE_TASK_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + CREATE_LAB_TABLE + " angelegt.");
            db.execSQL(CREATE_TASK_TABLE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
