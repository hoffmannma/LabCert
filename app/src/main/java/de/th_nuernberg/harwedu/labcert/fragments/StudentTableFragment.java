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

import java.util.ArrayList;
import java.util.Objects;

import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.StudentTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Dieses Fragment öffnet die Datenbank und stellt alle
 * Studenten der gewählten Gruppe tabellarisch dar:
 *
 * Nachname | Vorname | Fortschritt in %
 */

public class StudentTableFragment extends Fragment {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DataSource dataSource;
    ArrayList<Student> studentList;

    private static String lab;
    private static String group;


    public StudentTableFragment() {
        // Required empty public constructor
    }

    public static StudentTableFragment newInstance(String param1, String param2) {
        StudentTableFragment fragment = new StudentTableFragment();
        lab = param1;
        group = param2;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_student_table, container, false);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new DataSource(getActivity());
        return rootView;
    }

    /**
     *
     * @param rootView rootView
     */
    private void showAllListEntries(View rootView) {
        // Liefert alle Datensätze
        if (!Objects.equals(group, MainActivity.ALL_STUDENTS)) {
            // Liste der Studenten einer Gruppe
            // TODO Parameter anpassen
            studentList = dataSource.getStudentsFromGrp(lab, group);
        } else {
            // Liste aller Studenten
            studentList = dataSource.getAllStudents();
        }

        ListView studentListView = (ListView) rootView.findViewById(R.id.listview_student_table);
        StudentTableAdapter adapter = new StudentTableAdapter(getActivity(), studentList);

        studentListView.setAdapter(adapter);

        // Auf Auswahl eines Studenten reagieren
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Student student = (Student) adapter.getItemAtPosition(position);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                StudentFragment.newInstance(student);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    /**
     * Fragment tritt in den Vordergrund: Datenbank neu aufrufen
     */
    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "+++ Resume +++");
        dataSource.openR();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries(getView());
    }

    /**
     * Fragment pausiert: Datenbankzugriff schließen
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "+++ Pause +++");
        dataSource.close();
    }
}
