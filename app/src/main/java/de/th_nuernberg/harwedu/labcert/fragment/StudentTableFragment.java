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

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.harwedu.labcert.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.StudentTableAdapter;
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

        return rootView;
    }

    private void showAllListEntries (View rootView) {

        // Liefert alle Datensätze
        ArrayList<Student> studentList = dataSource.getAllStudents();

        ListView studentListView = (ListView) rootView.findViewById(R.id.listview_student_table);
        StudentTableAdapter adapter = new StudentTableAdapter(getActivity(), studentList);

        studentListView.setAdapter(adapter);

        // Auf Klicks auf Items reagieren
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

    // Fragment tritt in den Vordergrund: Datenbank neu aufrufen
    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "+++ Resume +++");
        dataSource.openW();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries(getView());
    }

    // Fragment pausiert: Datenbankzugriff schließen
    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "+++ Pause +++");
        dataSource.close();
    }
}
