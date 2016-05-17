package de.th_nuernberg.harwedu.labcert.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.th_nuernberg.harwedu.labcert.MainActivity;
import de.th_nuernberg.harwedu.labcert.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {

    public StudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student, container, false);
    }


}
