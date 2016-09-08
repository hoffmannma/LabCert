package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.adapter.StudentTableAdapter;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Dieses Fragment öffnet die Datenbank und stellt alle
 * Studenten der gewählten Gruppe tabellarisch dar:
 *
 * Nachname | Vorname | Fortschritt in %
 */

public class StudentTableFragment extends Fragment {

    //TODO Interface einfügen
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DataSource dataSource;
    ArrayList<Student> studentList;
    ListView studentListView;

    private static String mLab;
    private static String mGroup;
    private static String mTerm;

    private View rootView;


    public StudentTableFragment() {
        // Required empty public constructor
    }

    public static StudentTableFragment newInstance(String lab, String grp, String term) {
        StudentTableFragment fragment = new StudentTableFragment();
        mLab = lab;
        mGroup = grp;
        mTerm = term;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_student_table, container, false);
        studentListView = (ListView) rootView.findViewById(R.id.listview_student_table);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_student_list,
                studentListView, false);
        studentListView.addHeaderView(header, null, false);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new DataSource(getActivity());
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
        return rootView;
    }

    /**
     *
     */
    private void showAllListEntries() {
        // Liefert alle Datensätze
        dataSource.openR();
        if (!Objects.equals(mGroup, MainActivity.CHOOSE_GROUP)) {
            // Liste der Studenten einer Gruppe
            // TODO Parameter anpassen
            studentList = dataSource.getStudentsFromGrp(mLab, mGroup);
        } else {
            // Liste aller Studenten
            studentList = dataSource.getAllStudents();
        }
        dataSource.close();

        StudentTableAdapter adapter = new StudentTableAdapter(getActivity(), studentList);
        studentListView.setAdapter(adapter);

        // Auf Auswahl eines Studenten reagieren
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                try{
                    ((OnStudentSelected) getContext()).onStudentSelected(true);
                } catch (ClassCastException ignored) {
                }
                Student student = (Student) adapter.getItemAtPosition(position);
                dataSource.openR();
                ArrayList<Requirement> reqList = dataSource.getGroupRequirements(mLab, mGroup, mTerm);
                dataSource.close();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                StudentFragment.newInstance(student, reqList);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    /**
     * Fragment tritt in den Vordergrund: Datenbank neu aufrufen
     */
    /*
    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "+++ Resume +++");
        dataSource.openR();
        Log.d(LOG_TAG, "Datenbank-Einträge:");
        showAllListEntries(getView());
    }*/

    /**
     * Fragment pausiert: Datenbankzugriff schließen
     */
    /*
    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "+++ Pause +++");
        dataSource.close();
    }*/

    public interface OnStudentSelected{
        void onStudentSelected(boolean selected);
    }

}
