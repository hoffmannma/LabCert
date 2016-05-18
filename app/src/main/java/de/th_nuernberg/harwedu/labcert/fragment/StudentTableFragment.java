package de.th_nuernberg.harwedu.labcert.fragment;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import de.th_nuernberg.harwedu.labcert.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.Student;
import de.th_nuernberg.harwedu.labcert.database.StudentDataSource;


public class StudentTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private StudentDataSource dataSource;


    public StudentTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_student_table, container, false);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new StudentDataSource(getActivity());


        /*
        *   Testroutine: Anlegen von Studenten direkt beim Fragmentaufruf
        *   --> Datenbank wird bei jedem Aufruf des Fragments um
        *       einen Eintrag erweitert
        *   --> Logs zum Debuggen
        *       Filter: MainActivity|StudentTableFragment|Student
        *
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.openW();
        Student student = dataSource.createStudent("Harwart","Eduard",
                "Kommentar","3/2","4",2,1,0,0);
        Log.d(LOG_TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG, "ID: " + student.getId() + ", Inhalt: " + student.getSurname());
        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries(rootView);
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
        */

        return rootView;
    }

    private void showAllListEntries (View rootView) {
        //Liefert alle Datensätze
        List<Student> studentList = dataSource.getAllStudents();

        //Einfügen in Adapter
        ArrayAdapter<Student> studentArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                studentList);
        //Array-Adapter an ListView des MainActivity-Layouts binden
        //-> Tabelle wird auf Gerät angezeigt
        ListView studentListView = (ListView) rootView.findViewById(R.id.listview_student_table);
        studentListView.setAdapter(studentArrayAdapter);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Student student = (Student)adapter.getItemAtPosition(position);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                fragment.newInstance(student);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "+++ Resume +++");
        dataSource.openW();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries(getView());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "+++ Pause +++");
        dataSource.close();
    }
}
