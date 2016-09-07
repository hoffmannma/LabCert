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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import de.th_nuernberg.harwedu.labcert.CONFIG;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.csv.CsvParser;
import de.th_nuernberg.harwedu.labcert.csv.XLSX;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.fragments.GroupTableFragment;
import de.th_nuernberg.harwedu.labcert.fragments.SettingsFragment;
import de.th_nuernberg.harwedu.labcert.fragments.ImportRequirementFragment;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.fragments.CreateStudentFragment;
import de.th_nuernberg.harwedu.labcert.fragments.CreateGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragments.CreateRequirementFragment;
import de.th_nuernberg.harwedu.labcert.fragments.RequirementTableFragment;
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
        implements NavigationView.OnNavigationItemSelectedListener,
        CreateGroupFragment.OnGroupCreatedListener, GroupTableFragment.OnGroupSelectedListener{

    private static final String ALL_STUDENTS = "Alle Studenten";

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STUDENT_TABLE_TAG = "STUDENT_TABLE";
    private static final String csv_name = "students.csv";
    private static final String xlsx_name = "Testatbogen.xlsx";

    public static final String CHOOSE_GROUP = "Gruppe wählen";

    private static boolean readExtAccepted;
    private static boolean writeExtAccepted;

    private static String userName;
    private static String userMail;
    public static String term;
    private static String currentLab;
    private static String currentGroup;

    private static String spinnerString;
    private static List<de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener> listeners
            = new ArrayList<de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener>();

    public String getCurrentGroup(){
        return currentGroup;
    }

    public void setCurrentGroup(String grp){
        currentGroup = grp;
        for (de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener g: listeners){
            g.onGroupChanged(currentLab, currentGroup, term);
        }
    }

    public static void addGroupChangeListener(de.th_nuernberg.harwedu.labcert.interfaces.GroupChangeListener g){
        listeners.add(g);
    }



    private static TextView userNameTxt;
    private static TextView userMailTxt;
    private static TextView currentLabTxt;
    private static TextView currentGroupTxt;

    private static Spinner spinner;
    private ArrayAdapter<String> adapter;

    private static NavigationView navigationView;

    private ProgressDialog prgDialog;

    // Initialisieren
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Klassen Mainactivity übergeben
        CONFIG.context = this;

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

        // TODO: Datenbank / Shared Memory Abfrage von Userdaten; TERM ANPASSEN
        userName = "Eduard Harwart";
        userMail = "harwartedu58020@th-nuernberg.de";
        userNameTxt.setText(userName);
        userMailTxt.setText(userMail);
        term = "SS16";

        /**
         *      Spinner mit Gruppenauswahl
         */
        spinner = (Spinner) findViewById(R.id.spinner_group);
        // Gruppenliste aus Datenbank holen
        // Auf Auswahl reagieren
        updateSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                spinnerString = spinner.getSelectedItem().toString();
                if (spinnerString != ALL_STUDENTS) {
                    String splitSpinnerString[] = spinnerString.split(getString(R.string.split_blank));
                    currentLab = splitSpinnerString[0];
                    setCurrentGroup(splitSpinnerString[1]);
                }
                else {
                    currentLab = "";
                    setCurrentGroup(CHOOSE_GROUP);
                }
                currentLabTxt.setText(currentLab);
                currentGroupTxt.setText(currentGroup);
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        /**
         *      Default-Fragment (Studententabelle) öffnen
         */
        if (savedInstanceState == null) {
            jumpToStudentTable();
            //navigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    /**
     * Auf vorhandene Berechtigungen prüfen
     */

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                // TODO Parameter labname, term, bib
                if (dataSource.studentExistsByBib(currentLab, term, scanContent)) {
                    //dataSource.insertAttd(scanContent, getEditor());
                    //toastMsg(getString(R.string.attd_updated));
                    // TODO Parameter labname, term, bib
                    Student student = dataSource.getStudentByBib(currentLab, term, scanContent);
                    jumpToStudent(student);
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    UnknownStudentFragment fragment = new UnknownStudentFragment();
                    UnknownStudentFragment.newInstance(scanFormat, scanContent, currentLab,
                            currentGroup, term);
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

        //Button: Settings
        if (id == R.id.action_settings) {
            try {
                Log.d(LOG_TAG, "Lege Setting-Einträge an.");
                DataSource datasource = new DataSource(this);
                datasource.openW();
                datasource.settingsCreation();
                datasource.close();
                Log.d(LOG_TAG, "Setting-Einträge angelegt.");
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Fehler beim Erstellen der Setting-Einträge: " + ex.getMessage());
            }

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SettingsFragment fragment = new SettingsFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }


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
        } else if (id == R.id.nav_all_groups) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            GroupTableFragment fragment = new GroupTableFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_add_member) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateStudentFragment fragment = new CreateStudentFragment();
            CreateStudentFragment.newInstance(currentLab, currentGroup, term);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_new_group) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateGroupFragment fragment = new CreateGroupFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_requirements) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RequirementTableFragment fragment = new RequirementTableFragment();
            RequirementTableFragment.newInstance(currentLab, currentGroup, term);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_import_requirement) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            ImportRequirementFragment fragment = new ImportRequirementFragment();
            ImportRequirementFragment.newInstance(currentLab, currentGroup, term);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_create_requirement) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateRequirementFragment fragment = new CreateRequirementFragment();
            CreateRequirementFragment.newInstance(currentLab, currentGroup, term);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_sync_db) {

            /* TODO Sync überarbeiten
            DataSource dataSource = new DataSource(this);
            dataSource.uploadNewAttdRecords();
            try {
                dataSource.syncAttdToRemote();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }*/
        } else if (id == R.id.nav_import_csv) {
            XLSX xlsx = new XLSX(this, xlsx_name);
            xlsx.execute();
            updateSpinner();
            //CsvParser csvparser = new CsvParser(this, xlsx_name);
            //csvparser.XLSXImport();
            //DataSource dataSource = new DataSource(this);
            //dataSource.importCSV(this, csv_name);
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
        StudentTableFragment.newInstance(currentLab, currentGroup, term);
        transaction.replace(R.id.fragment_container, fragment, STUDENT_TABLE_TAG);
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


    public void reload(){
        finish();
        startActivity(getIntent());
    }

    /**
     * Erstellt Gruppenliste in Spinner
     */
    private void updateSpinner(){
        ArrayList<String> groupList = new ArrayList<String>();
        DataSource datasource = new DataSource(this);
        datasource.openR();
        groupList = datasource.getAllGroupNames();
        datasource.close();
        groupList.add(ALL_STUDENTS);
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_main, groupList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onGroupCreated(boolean newGrp) {
        if (newGrp)
            updateSpinner();
    }

    @Override
    public void onGroupSelected(String lab, String grp){
        if (!grp.equals(null)) {
            String compareString = grp + " " + lab;
            int spinnerPos = adapter.getPosition(compareString);
            Log.d(LOG_TAG, "Abgleich des Spinners mit: " + compareString);
            spinner.setSelection(spinnerPos);
        }
        jumpToStudentTable();
    }
}
