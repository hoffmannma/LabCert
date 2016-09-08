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
import de.th_nuernberg.harwedu.labcert.adapter.RequirementTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;


public class RequirementTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static String mLab;
    private static String mGroup;
    private static String mTerm;

    ListView reqListView;

    private DataSource dataSource;


    public RequirementTableFragment() {
        // Required empty public constructor
    }



    public static RequirementTableFragment newInstance(String lab, String grp, String term) {
        RequirementTableFragment fragment = new RequirementTableFragment();
        mLab = lab;
        mGroup = grp;
        mTerm = term;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_requirement_table, container, false);
        dataSource = new DataSource(getActivity());
        reqListView = (ListView) rootView.findViewById(R.id.listview_req_table);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_req_list,
                reqListView, false);
        reqListView.addHeaderView(header, null, false);

        Button newReqButton = (Button) rootView.findViewById(R.id.button_new_requirement);
        Button importReqButton = (Button) rootView.findViewById(R.id.button_import_requirement);

        showAllListEntries();
        MainActivity.addGroupChangeListener(new GroupChangeListener() {
            @Override
            public void onGroupChanged(String lab, String group, String term) {
                mLab = lab;
                mGroup = group;
                mTerm = term;
                showAllListEntries();
            }
        });

        newReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                CreateRequirementFragment fragment = new CreateRequirementFragment();
                CreateRequirementFragment.newInstance(mLab, mGroup, mTerm);
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
        dataSource.openR();
        ArrayList<Requirement> reqList = dataSource.getGroupRequirements(mLab, mGroup, mTerm);
        dataSource.close();
        //ListView reqListView = (ListView) rootView.findViewById(R.id.listview_req_table);
        RequirementTableAdapter adapter = new RequirementTableAdapter(getActivity(), reqList);

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

}
