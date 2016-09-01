package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.th_nuernberg.harwedu.labcert.R;

public class UnknownStudentFragment extends Fragment {

    private static final String ARG_FORMAT = "format";
    private static final String ARG_CONTENT = "content";

    private static String mFormat;
    private static String mContent;
    private static String mLab;
    private static String mGroup;
    private static String mTerm;

    public UnknownStudentFragment() {
        // Required empty public constructor
    }

    public static UnknownStudentFragment newInstance(String format, String content, String lab,
                                                     String grp, String term) {
        UnknownStudentFragment fragment = new UnknownStudentFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_FORMAT, format);
        //args.putString(ARG_CONTENT, content);
        //fragment.setArguments(args);
        mFormat = format;
        mContent = content;
        mLab = lab;
        mGroup = grp;
        mTerm = term;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mFormat = getArguments().getString(ARG_FORMAT);
            mContent = getArguments().getString(ARG_CONTENT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_unknown_student, container, false);

        //TextView unknownTxt = (TextView) rootView.findViewById(R.id.textview_unknown_student);
        TextView formatTxt = (TextView) rootView.findViewById(R.id.textview_unknown_format);
        TextView contentTxt = (TextView) rootView.findViewById(R.id.textview_unknown_content);
        Button addMemberButton = (Button) rootView.findViewById(R.id.button_add_unknown);
        Button addToExistingButton = (Button) rootView.findViewById(R.id.button_add_to_existing);
        Button dropDataButton = (Button) rootView.findViewById(R.id.button_drop_data);

        String formatString = "Format: " + mFormat;
        String contentString = "Inhalt: " + mContent;

        formatTxt.setText(formatString);
        contentTxt.setText(contentString);

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                CreateStudentFragment fragment = new CreateStudentFragment();
                fragment.newInstance(mContent, mGroup);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        addToExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SimpleStudentTableFragment fragment = new SimpleStudentTableFragment();
                fragment.newInstance(mLab, mGroup, mTerm, mContent);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        dropDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                toastMsg("Scan verworfen");
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
