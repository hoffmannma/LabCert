package de.th_nuernberg.harwedu.labcert.fragments;


import android.app.Activity;
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
import de.th_nuernberg.harwedu.labcert.main.MainActivity;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 *
 */
public class CreateGroupFragment extends Fragment {


    Context context;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        final DataSource dataSource = new DataSource(getActivity());
        dataSource.openW();

        final EditText editTextLab = (EditText) rootView.findViewById(R.id.editText_lab);
        final EditText editTextLabId = (EditText) rootView.findViewById(R.id.editText_lab_id);
        final EditText editTextGroupNo = (EditText) rootView.findViewById(R.id.editText_group_no);
        final EditText editTextSupervisor = (EditText) rootView.findViewById(R.id.editText_supervisor);

        Button createGroupButton = (Button) rootView.findViewById(R.id.button_create_group);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String labString = editTextLab.getText().toString();
                String labIdString = editTextLabId.getText().toString();
                String groupNoString = editTextGroupNo.getText().toString();
                String supervisorString = editTextSupervisor.getText().toString();

                if (TextUtils.isEmpty(labString)) {
                    editTextLab.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(labIdString)) {
                    editTextLabId.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(groupNoString)) {
                    editTextGroupNo.setError(getString(R.string.editText_errorMessage));
                    return;
                } else if (TextUtils.isEmpty(supervisorString)) {
                    editTextSupervisor.setError(getString(R.string.editText_errorMessage));
                    return;
                }


                editTextLab.setText("");
                editTextLabId.setText("");
                editTextGroupNo.setText("");
                editTextSupervisor.setText("");

                dataSource.createGroup(labString, labIdString, groupNoString, supervisorString);
                toastMsg("Gruppe erstellt");

                try{
                    ((OnGroupCreatedListener) context).onGroupCreated(true);
                } catch (ClassCastException cce){

                }

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                GroupTableFragment fragment = new GroupTableFragment();
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

    public interface OnGroupCreatedListener{
        public void onGroupCreated(boolean newGrp);
    }

}
