package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.GroupTableAdapter;
import de.th_nuernberg.harwedu.labcert.adapter.RequirementTableAdapter;
import de.th_nuernberg.harwedu.labcert.adapter.SimpleStudentTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.objects.Group;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private DataSource dataSource;
    private static String mParam;


    public GroupTableFragment() {
        // Required empty public constructor
    }


    public static GroupTableFragment newInstance(String param) {
        GroupTableFragment fragment = new GroupTableFragment();
        mParam = param;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_table, container, false);
        ListView groupListView = (ListView) rootView.findViewById(R.id.listview_group_table);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_group_list, groupListView,
                false);
        groupListView.addHeaderView(header, null, false);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new DataSource(getActivity());
        dataSource.openR();
        Log.d(LOG_TAG, "GroupTableFragment - Datenbank-Einträge:");
        showAllListEntries(rootView, groupListView);
        dataSource.close();
        return rootView;
    }

    private void showAllListEntries(final View rootView, ListView groupListView) {
        // Liefert alle Datensätze
        ArrayList<Group> groupList = dataSource.getAllGroups();

        GroupTableAdapter adapter = new GroupTableAdapter(getActivity(), groupList);
        groupListView.setAdapter(adapter);

        // Auf Auswahl eines Studenten reagieren
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Group group = (Group) adapter.getItemAtPosition(position);
                Toast.makeText(getActivity(), group.getGroup_id(), Toast.LENGTH_SHORT).show();
                /*
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                GroupFragment fragment = new GroupFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                */
            }
        });
    }

}
