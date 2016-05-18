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

    private static final String ARG_FORMAT = "format";
    private static final String ARG_CONTENT = "content";

    private String mFormat;
    private String mContent;

    public UnknownStudentFragment() {
        // Required empty public constructor
    }

    public static UnknownStudentFragment newInstance(String format, String content) {
        UnknownStudentFragment fragment = new UnknownStudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FORMAT, format);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFormat = getArguments().getString(ARG_FORMAT);
            mContent = getArguments().getString(ARG_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_unknown_student, container, false);

        //TextView unknownTxt = (TextView) rootView.findViewById(R.id.textview_unknown_student);
        TextView formatTxt = (TextView) rootView.findViewById(R.id.textview_unknown_format);
        TextView contentTxt = (TextView) rootView.findViewById(R.id.textview_unknown_content);
        Button addMemberButton = (Button) rootView.findViewById(R.id.button_add_unknown);
        Button dropDataButton = (Button) rootView.findViewById(R.id.button_drop_data);

        String formatString = "Format: " + mFormat;
        String contentString = "Inhalt: " + mContent;

        formatTxt.setText(formatString);
        contentTxt.setText(contentString);

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
