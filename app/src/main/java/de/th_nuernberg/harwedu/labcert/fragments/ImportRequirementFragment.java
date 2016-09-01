package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.ImportRequirementAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;

/**
 *
 */
public class ImportRequirementFragment extends Fragment {
//TODO Interface für Gruppenänderung

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARG_PARAM = "param";
    ListView reqListView;

    private DataSource dataSource;
    ArrayList<Requirement> reqList;
    private static String mLab;
    private static String mGroup;
    private static String mTerm;


    public ImportRequirementFragment() {
        // Required empty public constructor
    }



    public static ImportRequirementFragment newInstance(String lab, String grp, String term) {
        ImportRequirementFragment fragment = new ImportRequirementFragment();
        mLab = lab;
        mGroup = grp;
        mTerm = term;
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
        View rootView = inflater.inflate(R.layout.fragment_import_requirement, container, false);
        dataSource = new DataSource(getActivity());
        reqListView = (ListView) rootView.findViewById(R.id.listview_req_table);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_import_req,
                reqListView, false);
        reqListView.addHeaderView(header, null, false);
        Button importCheckedReqButton = (Button) rootView.findViewById(R.id.button_import_req);

        MainActivity.addGroupChangeListener(new GroupChangeListener() {
            @Override
            public void onGroupChanged(String lab, String group, String term) {
                mLab = lab;
                mGroup = group;
                mTerm = term;
            }
        });

        importCheckedReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = reqListView.getCheckedItemPositions();
                dataSource.openW();
                for (int i = 0; i < reqListView.getAdapter().getCount(); ++i) {
                    if (checked.get(i-1)) {
                        Requirement req = reqList.get(i-1);
                        dataSource.updateReq(req, mLab, mGroup, mTerm);
                    }
                }
                dataSource.close();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RequirementTableFragment fragment = new RequirementTableFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    private void showAllListEntries() {
        dataSource.openR();
        reqList = dataSource.getAllRequirements();
        dataSource.close();
        ImportRequirementAdapter adapter = new ImportRequirementAdapter(getActivity(), reqList);
        reqListView.setAdapter(adapter);

        /*
        reqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Requirement req = (Requirement) adapter.getItemAtPosition(position);
            }
        });
        */

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
