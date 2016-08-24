package de.th_nuernberg.harwedu.labcert.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;


public class CreateRequirementFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private String mParam;

    public CreateRequirementFragment() {
    }

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

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_req_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.req_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //TODO Auf Spinnerauswahl reagieren
        final EditText editTextLab = (EditText) rootView.findViewById(R.id.editText_reqLab);
        final EditText editTextGroup = (EditText) rootView.findViewById(R.id.editText_reqGroup);
        //TODO Öffnen von EditTextGroup öffnet Spinner oben; was drinsteht abhängig von Spinner oben
        editTextGroup.setText(mParam);

        Button createReqButton = (Button) rootView.findViewById(R.id.button_create_req);
        createReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupString = editTextGroup.getText().toString();
                String labString = editTextLab.getText().toString();

                if (TextUtils.isEmpty(groupString)) {
                    editTextGroup.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(labString)) {
                    editTextLab.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                editTextGroup.setText("");
                editTextLab.setText("");

                final DataSource dataSource = new DataSource(getActivity());
                dataSource.openW();
                // TODO Gruppen-ID holen (über Objekt Gruppe)
                // TODO Übergabeparameter anpassen
                // lab, group, term, type, count
                dataSource.createRequirement("Dies", "ist", "nur", "ein", "Test");
                toastMsg("Anforderung erstellt");
                dataSource.close();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RequirementTableFragment fragment = new RequirementTableFragment();
                RequirementTableFragment.newInstance(groupString);
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
