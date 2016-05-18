package de.th_nuernberg.harwedu.labcert.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Edu on 17.05.2016.
 */
public class StudentDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = StudentDbHelper.class.getSimpleName();
    public static final String DB_NAME = "labcert_group.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STUDENT = "student_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_LABGROUP = "labgroup";
    public static final String COLUMN_LABTEAM = "labteam";
    public static final String COLUMN_MATR = "matr";
    public static final String COLUMN_BIB = "bib";
    public static final String COLUMN_ATTD = "attd";
    public static final String COLUMN_TASKS = "tasks";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_STUDENT +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SURNAME + " TEXT NOT NULL, " +
                    COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                    COLUMN_COMMENT + " TEXT NOT NULL, " +
                    COLUMN_LABGROUP + " TEXT NOT NULL, " +
                    COLUMN_LABTEAM + " TEXT NOT NULL, " +
                    COLUMN_MATR + " TEXT NOT NULL, " +
                    COLUMN_BIB + " TEXT NOT NULL, " +
                    COLUMN_ATTD + " INTEGER NOT NULL, " +
                    COLUMN_TASKS + " INTEGER NOT NULL);";

    public StudentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "StudentDbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
