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
import de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener;
import de.th_nuernberg.harwedu.labcert.main.MainActivity;


public class CreateRequirementFragment extends Fragment {

    private static String mLab;
    private static String mGroup;
    private static String mTerm;

    public CreateRequirementFragment() {
    }

    public static CreateRequirementFragment newInstance(String lab, String grp, String term) {
        CreateRequirementFragment fragment = new CreateRequirementFragment();
        mLab = lab;
        mGroup = grp;
        mTerm = term;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_create_requirement, container, false);

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_req_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.req_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final EditText editTextCount = (EditText) rootView.findViewById(R.id.editText_reqCount);
        final EditText editTextLab = (EditText) rootView.findViewById(R.id.editText_reqLab);
        final EditText editTextGroup = (EditText) rootView.findViewById(R.id.editText_reqGroup);
        final EditText editTextTerm = (EditText) rootView.findViewById(R.id.editText_reqTerm);
        Button createReqButton = (Button) rootView.findViewById(R.id.button_create_req);
        editTextLab.setText(mLab);
        editTextGroup.setText(mGroup);
        editTextTerm.setText(mTerm);

        MainActivity.addGroupChangeListener(new GroupChangeListener() {
            @Override
            public void onGroupChanged(String lab, String group, String term) {
                editTextLab.setText(lab);
                editTextGroup.setText(group);
                editTextTerm.setText(term);
            }
        });

        createReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typeString = spinner.getSelectedItem().toString();
                String countString = editTextCount.getText().toString();
                String labString = editTextLab.getText().toString();
                String groupString = editTextGroup.getText().toString();
                String termString = editTextTerm.getText().toString();

                if (TextUtils.isEmpty(countString)) {
                    editTextCount.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(groupString)) {
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
                dataSource.createRequirement(labString, groupString, termString, typeString,
                        countString);
                toastMsg("Anforderung erstellt");
                dataSource.close();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RequirementTableFragment fragment = new RequirementTableFragment();
                RequirementTableFragment.newInstance(labString, groupString, termString);
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
