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

import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.SimpleStudentTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Student;


public class SimpleStudentTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DataSource dataSource;
    private static String mParam;


    public SimpleStudentTableFragment() {
        // Required empty public constructor
    }

    /**
     * Übergibt Bib-Nr. an Fragment
     */
    public static SimpleStudentTableFragment newInstance(String param) {
        SimpleStudentTableFragment fragment = new SimpleStudentTableFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM, param);
        //fragment.setArguments(args);
        mParam = param;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_student_table, container, false);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new DataSource(getActivity());
        dataSource.openR();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries(rootView);
        dataSource.close();
        return rootView;
    }

    /**
     * Zeigt Studentenliste nur mit Vor- und Nachnamen
     *
     * @param rootView
     */
    private void showAllListEntries(final View rootView) {
        // Liefert alle Datensätze
        ArrayList<Student> studentList = dataSource.getAllStudents();

        ListView studentListView = (ListView) rootView.findViewById(R.id.listview_student_table);
        SimpleStudentTableAdapter adapter =
                new SimpleStudentTableAdapter(getActivity(), studentList);

        studentListView.setAdapter(adapter);

        // Auf Auswahl eines Studenten reagieren
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Student student = (Student) adapter.getItemAtPosition(position);
                student.setBib(mParam);
                dataSource.insertBib(student);
                // Normale Tabelle wieder anzeigen und Toast für "Bib-Nr. aktualisiert" anzeigen
                String toastStr = "Bib.-Nr. eingetragen für " +
                        student.getSurname() + " " + student.getFirstname();
                Toast.makeText(getActivity(), toastStr, Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentTableFragment fragment = new StudentTableFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

}
