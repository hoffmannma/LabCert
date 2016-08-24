package de.th_nuernberg.harwedu.labcert.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.SimpleRequirementTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;


public class RequirementTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARG_PARAM = "param";

    ListView reqListView;

    private DataSource dataSource;

    // TODO: Rename and change types of parameters
    private String mGroup;


    public RequirementTableFragment() {
        // Required empty public constructor
    }



    public static RequirementTableFragment newInstance(String param) {
        RequirementTableFragment fragment = new RequirementTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroup = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_requirement_table, container, false);
        dataSource = new DataSource(getActivity());

        reqListView = (ListView) rootView.findViewById(R.id.listview_req_table);
        Button newReqButton = (Button) rootView.findViewById(R.id.button_new_requirement);
        Button importReqButton = (Button) rootView.findViewById(R.id.button_import_requirement);

        newReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                CreateRequirementFragment fragment = new CreateRequirementFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        importReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                ImportRequirementFragment fragment = new ImportRequirementFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    private void showAllListEntries() {

        // TODO Parameter labname, group, term
        // Liefert alle Datensätze
        ArrayList<Requirement> reqList = dataSource.getGroupRequirements("LABNAME",mGroup,"SS16");

        //ListView reqListView = (ListView) rootView.findViewById(R.id.listview_req_table);
        SimpleRequirementTableAdapter adapter = new SimpleRequirementTableAdapter(getActivity(), reqList);

        Log.d(LOG_TAG, "RequirementTable: Versuche Adapter zu setzen...");
        reqListView.setAdapter(adapter);
        Log.d(LOG_TAG, "RequirementTable: Versuche OnClickListener zu setzen...");
        // Auf Auswahl eines Studenten reagieren

        reqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Requirement req = (Requirement) adapter.getItemAtPosition(position);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RequirementFragment fragment = new RequirementFragment();
                RequirementFragment.newInstance(req);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    // Fragment tritt in den Vordergrund: Datenbank neu aufrufen
    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "+++ Resume: RequirementTable +++");
        dataSource.openR();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries();
    }

    // Fragment pausiert: Datenbankzugriff schließen
    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "+++ Pause: RequirementTable +++");
        dataSource.close();
    }

}
