package de.th_nuernberg.harwedu.labcert.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.th_nuernberg.harwedu.labcert.csv.CsvParser;
import de.th_nuernberg.harwedu.labcert.R;


public class CsvSampleFragment extends ListFragment {

    ArrayList<HashMap<String, String>> csvData;

    public CsvSampleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Mytask().execute();
    }
    class Mytask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            CsvParser csvreader = new CsvParser(getActivity(),
                    "test_grp.csv");
            try {
                csvData = csvreader.ReadCSV();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Keys der Hashmap
            String[] from = { "row[0]", "row[1]", "row[2]", "row[3]" };
            // Elemente, in die die Daten eingefügt werden
            int[] to = { R.id.surname, R.id.first_name, R.id.mat_no, R.id.cert };
            // Adapter erstellen
            ListAdapter adapter = new SimpleAdapter(getActivity(), csvData,
                    R.layout.list_items, from, to);
            // Adapter anbinden
            setListAdapter(adapter);

        }
    }
}
