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


public class CreateRequirementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private String mParam;

    public CreateRequirementFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CreateRequirementFragment newInstance(String param) {
        CreateRequirementFragment fragment = new CreateRequirementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_requirement, container, false);

        final DataSource dataSource = new DataSource(getActivity());
        dataSource.openW();

        final EditText editTextType = (EditText) rootView.findViewById(R.id.editText_reqType);
        final EditText editTextName = (EditText) rootView.findViewById(R.id.editText_reqName);
        final EditText editTextGroup = (EditText) rootView.findViewById(R.id.editText_reqGroup);
        final EditText editTextLab = (EditText) rootView.findViewById(R.id.editText_reqLab);
        final EditText editTextTerm = (EditText) rootView.findViewById(R.id.editText_reqTerm);

        Button createReqButton = (Button) rootView.findViewById(R.id.button_create_req);

        //if(mParam != null)
        editTextGroup.setText(mParam);

        createReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typeString = editTextType.getText().toString();
                String nameString = editTextName.getText().toString();
                String groupString = editTextGroup.getText().toString();
                String labString = editTextLab.getText().toString();
                String termString = editTextTerm.getText().toString();

                if (TextUtils.isEmpty(typeString)) {
                    editTextType.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(nameString)) {
                    editTextName.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(groupString)) {
                    editTextGroup.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(labString)) {
                    editTextLab.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(termString)) {
                    editTextTerm.setError(getString(R.string.editText_errorMessage));
                    return;
                }


                editTextType.setText("");
                editTextName.setText("");
                editTextGroup.setText("");
                editTextLab.setText("");
                editTextTerm.setText("");

                dataSource.createRequirement(typeString, nameString,
                        groupString, labString, termString);
                toastMsg("Anforderung erstellt");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RequirementTableFragment fragment = new RequirementTableFragment();
                fragment.newInstance(groupString);
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
