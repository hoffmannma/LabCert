package de.th_nuernberg.harwedu.labcert.database;

/**
 * Created by Edu on 16.05.2016.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupDBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    public GroupDBController(Context applicationcontext) {
        super(applicationcontext, "LabCertDB.db", null, 1);  // creating DATABASE
        Log.d(LOGCAT, "Created");
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS group_table ( Id INTEGER PRIMARY KEY, Company TEXT,Name TEXT,Price TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS group_table";
        database.execSQL(query);
        onCreate(database);
    }
    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> proList;
        proList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM group_table";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", cursor.getString(0));
                map.put("Company", cursor.getString(1));
                map.put("Name", cursor.getString(2));
                map.put("Price", cursor.getString(3));
                proList.add(map);
            } while (cursor.moveToNext());
        }
        return proList;
    }
}
