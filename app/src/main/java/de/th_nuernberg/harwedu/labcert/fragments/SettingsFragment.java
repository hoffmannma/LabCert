package de.th_nuernberg.harwedu.labcert.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.DbHelper;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Created by bestj on 06.09.2016.
 */
public class SettingsFragment extends Fragment {
    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final TextView usernameTxt = (TextView) rootView.findViewById(R.id.textview_username);
        final TextView passwordTxt = (TextView) rootView.findViewById(R.id.textview_password);
        final EditText commUsernameTxt = (EditText) rootView.findViewById(R.id.edittext_username);
        final EditText commPasswordTxt = (EditText) rootView.findViewById(R.id.edittext_password);
        Button saveDataButton = (Button) rootView.findViewById(R.id.button_save_data);

        usernameTxt.setText("Benutzername");
        passwordTxt.setText("Passwort");
        DataSource datasource = new DataSource(getActivity());

        datasource.openR();
        commUsernameTxt.setText(datasource.getSetting(DbHelper.SETTING_MAIL_USERNAME));
        Log.d(LOG_TAG, "Benutzername: " + datasource.getSetting(DbHelper.SETTING_MAIL_USERNAME));
        commPasswordTxt.setText(datasource.getSetting(DbHelper.SETTING_MAIL_PASSWORD));
        Log.d(LOG_TAG, "Passwort: " + datasource.getSetting(DbHelper.SETTING_MAIL_PASSWORD));
        datasource.close();

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameString = commUsernameTxt.getText().toString();
                String passwordString = commPasswordTxt.getText().toString();

                DataSource datasource = new DataSource(getActivity());
                datasource.openW();
                datasource.updateSetting(DbHelper.SETTING_MAIL_USERNAME, usernameString);
                Log.d(LOG_TAG, "Benutzername aktualisiert: " + datasource.getSetting(DbHelper.SETTING_MAIL_USERNAME));
                datasource.updateSetting(DbHelper.SETTING_MAIL_PASSWORD, passwordString);
                Log.d(LOG_TAG, "Passwort aktualisiert: " + datasource.getSetting(DbHelper.SETTING_MAIL_PASSWORD));
                datasource.close();

                getActivity().getFragmentManager().popBackStack();

                toastMsg("Daten gespeichert...");
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
