package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;

/**
 * TODO
 * Synchronisation der Aufgaben
 */

public class WebSyncFragment extends Fragment {

/*
    private static final String LOG_TAG = "WebSyncFragment";
    private static final String SYNC_ATTD_URL =
            "http://josuaa.de/labcert/sync/insert_attendance.php";
    private final int DEFAULT_TIMEOUT = 30 * 1000;

    DataSource controller;
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;

    public WebSyncFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_sync, container, false);

        controller = new DataSource(getActivity());
        prgDialog = new ProgressDialog(getActivity());

        prgDialog.setMessage("Datenbank wird synchronisiert. Bitte warten...");
        prgDialog.setCancelable(false);

        syncSQLiteMySQLDB();

        //getFragmentManager().popBackStack();

        return rootView;
    }

    private void syncSQLiteMySQLDB() {
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList = controller.getAttendance();
        int a = 1;
        client.setTimeout(DEFAULT_TIMEOUT);

        if (a == 1) {
            if (controller.dbSyncCount() != 0) {

                prgDialog.show();
                String json = controller.composeJSONfromSQLite();

                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println(json);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                params.put("attendanceJSON", controller.composeJSONfromSQLite());

                client.post(SYNC_ATTD_URL, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        System.out.println(Arrays.toString(responseBody));
                        String str = new String(responseBody, StandardCharsets.UTF_8);
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        System.out.println(str);
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        prgDialog.hide();

                        try {
                            JSONArray jsonArray = new JSONArray(str);
                            System.out.println(jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                queryValues = new HashMap<>();
                                queryValues.put("id", obj.get("id").toString());
                                queryValues.put("matr", obj.get("matr").toString());
                                queryValues.put("ts", obj.get("ts").toString());
                                queryValues.put("editor", obj.get("editor").toString());
                                queryValues.put("a_date", obj.get("a_date").toString());
                                queryValues.put("comment", obj.get("comment").toString());
                                queryValues.put("status", obj.get("status").toString());
                                queryValues.put("new_entry", obj.get("new_entry").toString());

                                controller.insertAttd(queryValues);
                                //controller.updateSyncStatus(obj.get("id")
                                //       .toString(),obj.get("status").toString());
                            }
                            Log.d(LOG_TAG, "DB Sync completed!");
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            StudentTableFragment fragment = new StudentTableFragment();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } catch (JSONException e) {
                            Log.d(LOG_TAG, "Error Occured - Server's JSON response might be invalid");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                          Throwable error) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if (statusCode == 404) {
                            Log.d(LOG_TAG, "Requested resource not found");
                        } else if (statusCode == 500) {
                            Log.d(LOG_TAG, "Something went wrong at server end");
                        } else
                            Log.d(LOG_TAG, "Unexpected Error occcured! Status Code: " + statusCode);
                    }
                });
            } else
                Log.d(LOG_TAG, "SQLite and Remote MySQL DBs are in Sync!");
        } else
            Log.d(LOG_TAG, "No data in SQLite DB");

    }*/
}
