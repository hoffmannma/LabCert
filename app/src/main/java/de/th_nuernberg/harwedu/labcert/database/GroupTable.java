package de.th_nuernberg.harwedu.labcert.database;

/**
 * Created by Edu on 16.05.2016.
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GroupTable {

    // Database table
    public static final String TABLE_GROUP = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SURNAME = "Nachname";
    public static final String COLUMN_FIRSTNAME = "Vorname";
    public static final String COLUMN_MATNO = "Matrikelnummer";
    public static final String COLUMN_BIBNO = "Bibbliotheksnummer";
    public static final String COLUMN_ATTENDANCE = "Anwesenheit";
    public static final String COLUMN_CERT = "Testate";
    public static final String COLUMN_COMMENT = "Kommentar";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_GROUP
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SURNAME + " text not null, "
            + COLUMN_FIRSTNAME + " text not null,"
            + COLUMN_MATNO + " text not null"
            + COLUMN_BIBNO + " text not null"
            + COLUMN_ATTENDANCE + " text not null"
            + COLUMN_CERT + " text not null"
            + COLUMN_COMMENT + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(GroupTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        onCreate(database);
    }
}
