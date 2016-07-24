package de.th_nuernberg.harwedu.labcert.fragment;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.Student;

/**
 *
 */
public class AddStudentFragment extends Fragment {

    //private static final String ARG_PARAM = "param";

    private static String mParam;

    public AddStudentFragment() {
        // Required empty public constructor
    }

    public static AddStudentFragment newInstance(String param) {
        AddStudentFragment fragment = new AddStudentFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM, param);
        //fragment.setArguments(args);
        mParam = param;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_student, container, false);

        final DataSource dataSource = new DataSource(getActivity());
        dataSource.openW();

        final EditText editTextSurname = (EditText) rootView.findViewById(R.id.editText_surname);
        final EditText editTextFirstname = (EditText) rootView.findViewById(R.id.editText_firstname);
        final EditText editTextMatr = (EditText) rootView.findViewById(R.id.editText_matr);
        final EditText editTextBib = (EditText) rootView.findViewById(R.id.editText_bib);
        final EditText editTextLabteam = (EditText) rootView.findViewById(R.id.editText_labteam);
        final EditText editTextComment = (EditText) rootView.findViewById(R.id.editText_comment);

        Button addStudentButton = (Button) rootView.findViewById(R.id.button_add_student);

        //if(mParam != null)
            editTextBib.setText(mParam);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String surnameString = editTextSurname.getText().toString();
                String firstnameString = editTextFirstname.getText().toString();
                String matrString = editTextMatr.getText().toString();
                String bibString = editTextBib.getText().toString();
                String labteamString = editTextLabteam.getText().toString();
                String commentString = editTextComment.getText().toString();

                if(TextUtils.isEmpty(surnameString)) {
                    editTextSurname.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                else if(TextUtils.isEmpty(firstnameString)) {
                    editTextFirstname.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                else if(TextUtils.isEmpty(matrString)) {
                    editTextMatr.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                else if(TextUtils.isEmpty(bibString)) {
                    editTextBib.setError(getString(R.string.editText_errorMessage));
                    return;
                }


                editTextSurname.setText("");
                editTextFirstname.setText("");
                editTextMatr.setText("");
                editTextBib.setText("");
                editTextLabteam.setText("");

                dataSource.createStudent(surnameString, firstnameString,
                        commentString, "", labteamString, matrString, bibString);
                Student student = dataSource.getStudent(bibString);


                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                fragment.newInstance(student);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return rootView;
    }

}
