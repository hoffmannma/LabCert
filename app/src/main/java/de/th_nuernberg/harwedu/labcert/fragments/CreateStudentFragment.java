package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Dieses Fragment ermöglicht das Hinzufügen von Studenten.
 * <p/>
 * Der Aufruf erfolgt über den entsprechenden Menüeintrag oder der Auswahl "Student hinzufügen"
 * nach dem Scannen einer unbekannten Bib.-Nr..
 */

public class CreateStudentFragment extends Fragment {


    private static String mBib;
    private static String mGroup;

    public CreateStudentFragment() {
    }

    public static CreateStudentFragment newInstance(String pBib, String pGroup) {
        CreateStudentFragment fragment = new CreateStudentFragment();
        mBib = pBib;
        mGroup = pGroup;
        return fragment;
    }

    public static CreateStudentFragment newInstance(String pGroup) {
        CreateStudentFragment fragment = new CreateStudentFragment();
        mGroup = pGroup;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_student, container, false);

        final DataSource dataSource = new DataSource(getActivity());
        dataSource.openW();

        final EditText editTextSurname = (EditText) rootView.findViewById(R.id.editText_surname);
        final EditText editTextFirstname = (EditText) rootView.findViewById(R.id.editText_firstname);
        final EditText editTextMatr = (EditText) rootView.findViewById(R.id.editText_matr);
        final EditText editTextBib = (EditText) rootView.findViewById(R.id.editText_bib);
        final EditText editTextLabteam = (EditText) rootView.findViewById(R.id.editText_labteam);
        final EditText editTextComment = (EditText) rootView.findViewById(R.id.editText_comment);

        Button addStudentButton = (Button) rootView.findViewById(R.id.button_create_student);

        if(mBib != null)
            editTextBib.setText(mBib);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String surnameString = editTextSurname.getText().toString();
                String firstnameString = editTextFirstname.getText().toString();
                String matrString = editTextMatr.getText().toString();
                String bibString = editTextBib.getText().toString();
                String commentString = editTextComment.getText().toString();

                if (TextUtils.isEmpty(surnameString)) {
                    editTextSurname.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(firstnameString)) {
                    editTextFirstname.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(matrString)) {
                    editTextMatr.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(bibString)) {
                    editTextBib.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                editTextSurname.setText("");
                editTextFirstname.setText("");
                editTextMatr.setText("");
                editTextBib.setText("");
                editTextLabteam.setText("");
                // TODO Variablen implementieren, Parameter an Fragment übergeben
                dataSource.createStudent("LABNAME", mGroup, "SS16", "HERR", surnameString,
                        firstnameString, matrString, "E@MAIL.COM", commentString);
                /*
                (String lab_name, String group, String term,
                        String title, String surname, String firstname,
                        String matr, String email, String comment, String bib)
                        */
                //TODO Parameter labname, term, bib
                Student student = dataSource.getStudentByBib("LABNAME", "SS16", bibString);
                toastMsg("Student hinzugefügt");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                StudentFragment.newInstance(student);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return rootView;
    }

    private void toastMsg(String msg) {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
