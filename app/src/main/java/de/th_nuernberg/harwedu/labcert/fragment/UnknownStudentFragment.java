package de.th_nuernberg.harwedu.labcert.fragment;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.th_nuernberg.harwedu.labcert.R;

public class UnknownStudentFragment extends Fragment {


    public UnknownStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_unknown_student, container, false);

        //TextView unknownTxt = (TextView) rootView.findViewById(R.id.textview_unknow_student);
        Button addMemberButton = (Button) rootView.findViewById(R.id.button_add_unknown);
        Button dropDataButton = (Button) rootView.findViewById(R.id.button_drop_data);

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                AddStudentFragment fragment = new AddStudentFragment();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        dropDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}
