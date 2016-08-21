package de.th_nuernberg.harwedu.labcert.main;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.fragments.CreateStudentFragment;
import de.th_nuernberg.harwedu.labcert.fragments.CreateGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragments.CreateRequirementFragment;
import de.th_nuernberg.harwedu.labcert.fragments.RequirementFragment;
import de.th_nuernberg.harwedu.labcert.fragments.StudentFragment;
import de.th_nuernberg.harwedu.labcert.fragments.StudentTableFragment;
import de.th_nuernberg.harwedu.labcert.fragments.SwitchGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragments.UnknownStudentFragment;

/**
 * TODO
 * - Datenbank:
 * email im Hintergrund versenden
 * - Anwesenheit:
 * Im Objekt Student: Daten mit Terminen (1,2,3..) mappen
 * Kein passendes Datum: manueller / automatischer Eintrag wählen
 * -> manuell: Termin wählen
 * -> automatisch: Erster Null-Eintrag wird inkrementiert
 * <p/>
 * - Meldung, falls Verbindung zu externer Datenbank fehlgeschlagen
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String csv_name = "students.csv";

    private static boolean readExtAccepted;
    private static boolean writeExtAccepted;

    private static String userName;
    private static String userMail;
    private static String currentLab;
    private static String currentGroup;

    private static TextView userNameTxt;
    private static TextView userMailTxt;
    private static TextView currentLabTxt;
    private static TextView currentGroupTxt;

    private static NavigationView navigationView;

    private ProgressDialog prgDialog;

    // Initialisieren
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Berechtigungen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            String[] perms = {getString(R.string.perm_read_ext_sto),
                    getString(R.string.perm_wr_ext_sto)};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userNameTxt = (TextView) header.findViewById(R.id.textview_user_name);
        userMailTxt = (TextView) header.findViewById(R.id.textview_user_mail);
        currentLabTxt = (TextView) header.findViewById(R.id.textview_current_lab);
        currentGroupTxt = (TextView) header.findViewById(R.id.textview_current_group);

        // TODO: Datenbank / Shared Memory Abfrage von Userdaten
        userName = "Eduard Harwart";
        userMail = "harwartedu58020@th-nuernberg.de";
        currentLab = "INF2/1";
        currentGroup = "Gruppe 2";
        userNameTxt.setText(userName);
        userMailTxt.setText(userMail);
        currentLabTxt.setText(currentLab);
        currentGroupTxt.setText(currentGroup);

        if (savedInstanceState == null) {
            //navigationView.getMenu().getItem(0).setChecked(true);
            jumpToStudentTable();
            /*
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            StudentTableFragment fragment = new StudentTableFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            */
        }
    }


    /**
     * Auf vorhandene Berechtigungen prüfen
     */

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions,
                                           int[] grantResults) {
        switch (permsRequestCode) {
            case 200:
                readExtAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                writeExtAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    /**
     * Auswahl: Floating Action Button
     *
     * @param v
     */
    public void fabClicked(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    /**
     * Verarbeiten der Scan-Ergebnisse
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanFormat = scanningResult.getFormatName();
            String scanContent = scanningResult.getContents();

            if ((scanContent != null) && (scanFormat != null)) {
                DataSource dataSource = new DataSource(this);

                if (dataSource.studentExists(scanContent)) {
                    dataSource.insertAttd(scanContent, getEditor());
                    toastMsg(getString(R.string.attd_updated));
                    Student student = dataSource.getStudent(scanContent);
                    student.setAttd(dataSource.getAttdCount(student));
                    jumpToStudent(student);
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    UnknownStudentFragment fragment = new UnknownStudentFragment();
                    UnknownStudentFragment.newInstance(scanFormat, scanContent);
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else {
                jumpToStudentTable();
                toastMsg(getString(R.string.scan_aborted));
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_data, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Verhalten bei Betätigung des Back-Buttons
     */
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Drawer schließen falls geöffnet
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        // App schließen falls nur ein Fragment geöffnet ist
        else if (count == 0) {
            //super.onBackPressed();
            this.moveTaskToBack(true);
        }
        // ...sonst zurück zu vorheriger Ansicht
        else {
            jumpToStudentTable();
            // Nur einen Schritt zurück
            //getFragmentManager().popBackStack();
        }
    }

    // Create Options Menu (upper action bar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Auswahl: Einstellungen
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Button: Aktualisieren
        if (id == R.id.action_refresh) {
            jumpToStudentTable();
        }
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    /**
     * Auswahl: Menüeinträge
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_group) {
            jumpToStudentTable();
        } else if (id == R.id.nav_add_member) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateStudentFragment fragment = new CreateStudentFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_requirements) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RequirementFragment fragment = new RequirementFragment();
            RequirementFragment.newInstance(currentGroup);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_import_requirement) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RequirementFragment fragment = new RequirementFragment();
            RequirementFragment.newInstance(currentGroup);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_create_requirement) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateRequirementFragment fragment = new CreateRequirementFragment();
            SwitchGroupFragment.newInstance(currentGroup);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_new_group) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateGroupFragment fragment = new CreateGroupFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } /*else if (id == R.id.nav_sync_web) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            WebSyncFragment fragment = new WebSyncFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } */else if (id == R.id.nav_sync_db) {
            DataSource dataSource = new DataSource(this);
            dataSource.uploadNewAttdRecords();
            try {
                dataSource.syncAttdToRemote();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_import_csv) {

            DataSource dataSource = new DataSource(this);
            dataSource.importCSV(this, csv_name);
            toastMsg(getString(R.string.file_imported));
            jumpToStudentTable();
        } else if (id == R.id.nav_cert) {
            toastMsg("Aktuell keine Funktion");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Zum Homescreen springen (Gruppentabelle)
     */
    private void jumpToStudentTable() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StudentTableFragment fragment = new StudentTableFragment();
        transaction.replace(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void jumpToStudent(Student student) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StudentFragment fragment = new StudentFragment();
        StudentFragment.newInstance(student);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Kurze Meldung (Toast) ausgeben
     *
     * @param msg
     */
    private void toastMsg(String msg) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    private void openPrgDialog(String msg) {
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(msg);
        prgDialog.setCancelable(false);
        prgDialog.show();
    }


    /**
     * Gruppe wählen
     *
     * @param grp
     */
    public void setGroup(String grp) {
        currentGroup = grp;
        currentGroupTxt.setText(grp);
    }


    // Zu Testzwecken
    private String getEditor() {
        return "11";
    }

}
