package de.th_nuernberg.harwedu.labcert.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.th_nuernberg.harwedu.labcert.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.Student;
import de.th_nuernberg.harwedu.labcert.database.StudentDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {

    private static Student student;


    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance(Student param) {
        StudentFragment fragment = new StudentFragment();
        student = param;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        TextView studentTxt = (TextView) rootView.findViewById(R.id.textview_student);
        final EditText commEditTxt = (EditText) rootView.findViewById(R.id.edittext_comm);
        Button saveDataButton = (Button) rootView.findViewById(R.id.button_save_data);

        studentTxt.setText(student.getStudentData());
        commEditTxt.setText(student.getComment());

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentString = commEditTxt.getText().toString();

                StudentDataSource dataSource = new StudentDataSource(getActivity());
                dataSource.updateComment(student.getId(), commentString);

                getActivity().getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }


}
