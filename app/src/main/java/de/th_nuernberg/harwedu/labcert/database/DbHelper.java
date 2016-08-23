package de.th_nuernberg.harwedu.labcert.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * TODO Tabellen überarbeiten
 */


public class DbHelper extends SQLiteOpenHelper {

    /**
     * Konstanten
     */
    private static final String LOG_TAG = DbHelper.class.getSimpleName();
    public static final String DB_NAME = "labcert_group.db";
    public static final int DB_VERSION = 1;

    /**
     * Tabellen
     * - Studentendaten
     * - Anwesenheit
     * - Aufgaben
     * - Praktika (Fächer)
     */
    public static final String TABLE_STUDENT = "student_table";
    //public static final String TABLE_ATTENDANCE = "attendance_table";
    //public static final String TABLE_TASKS = "task_table";
    public static final String TABLE_GROUP = "group_table";
    public static final String TABLE_REQ = "requirement_table";
    //public static final String TABLE_LABS = "lab_table";
    public static final String TABLE_PROGRESS = "progress_table";

    /**
     * Gemeinsame verwendete Spaltennamen
     */
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MATR = "matr";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_EDITOR = "editor";
    public static final String COLUMN_TS = "timestamp";
    public static final String COLUMN_LAB_NAME = "lab_name";
    public static final String COLUMN_LAB_ID = "lab_id";
    public static final String COLUMN_NEW_ENTRY = "new_entry";
    public static final String COLUMN_GROUP = "group_id";
    public static final String COLUMN_SUPERVISOR = "supervisor";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LABGROUP = "labgroup";
    public static final String COLUMN_BIB = "bib";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DATE = "a_date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TERM = "term";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_GROUP_ID = "group_id";

    /**
     * Tabelle Fortschritt
     */
    public static final String COLUMN_REQ_ID = "req_id";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Studenten'
     * ID | Gruppen-ID | Anrede | Nachname | Vorname | Matr | Email | Bib-Nr. | Kommentar
     */
    public static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + TABLE_STUDENT +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GROUP_ID + " TEXT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_SURNAME + " TEXT NOT NULL, " +
                    COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_BIB + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT NOT NULL);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Gruppen'
     * ID | Labor-ID | Laborname | Gruppe | Semester | Betreuer-Kennung
     */
    public static final String CREATE_GROUP_TABLE =
            "CREATE TABLE " + TABLE_GROUP +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAB_ID + " TEXT NOT NULL, " +
                    COLUMN_LAB_NAME + " TEXT NOT NULL, " +
                    COLUMN_GROUP + " TEXT, " +
                    COLUMN_TERM + " TEXT NOT NULL, " +
                    COLUMN_SUPERVISOR + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Anforderungen'
     * ID | Gruppen-ID | Typ | Anzahl | Zeitstempel
     */
    public static final String CREATE_REQ_TABLE =
            "CREATE TABLE " + TABLE_REQ +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GROUP_ID + " TEXT NOT NULL, " +
                    COLUMN_TYPE + " TEXT NOT NULL, " +
                    COLUMN_COUNT + " TEXT NOT NULL, " +
                    COLUMN_TS + " TEXT NOT NULL);";

    /**
     * SQL-Befehl (String) zum Erzeugen der Tabelle 'Fortschritt'
     * ID | Anforderungs-ID | Matr | Punktzahl | Zeitstempel
     */
    public static final String CREATE_PROGRESS_TABLE =
            "CREATE TABLE " + TABLE_PROGRESS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_REQ_ID + " TEXT NOT NULL, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " TEXT, " +
                    COLUMN_COMMENT + " TEXT, " +
                    COLUMN_TS + " TEXT NOT NULL);";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " +
                    CREATE_STUDENT_TABLE + " angelegt.");
            db.execSQL(CREATE_STUDENT_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " +
                    CREATE_GROUP_TABLE + " angelegt.");
            db.execSQL(CREATE_GROUP_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " +
                    CREATE_REQ_TABLE + " angelegt.");
            db.execSQL(CREATE_REQ_TABLE);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " +
                    CREATE_PROGRESS_TABLE + " angelegt.");
            db.execSQL(CREATE_PROGRESS_TABLE);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
