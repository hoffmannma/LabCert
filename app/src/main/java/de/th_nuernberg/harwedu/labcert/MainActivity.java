package de.th_nuernberg.harwedu.labcert;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import de.th_nuernberg.harwedu.labcert.database.Student;
import de.th_nuernberg.harwedu.labcert.database.StudentDataSource;
import de.th_nuernberg.harwedu.labcert.fragment.AddStudentFragment;
import de.th_nuernberg.harwedu.labcert.fragment.CreateGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragment.StudentFragment;
import de.th_nuernberg.harwedu.labcert.fragment.StudentTableFragment;
import de.th_nuernberg.harwedu.labcert.fragment.SwitchGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragment.SyncFragment;
import de.th_nuernberg.harwedu.labcert.fragment.UnknownStudentFragment;

/**
 *  TODO
 * - Datenbank:
 *      Zeitstempel und Sync
 *      email im Hintergrund versenden
 * - Anwesenheit:
 *      Im Objekt Student: Daten mit Terminen (1,2,3..) mappen
 *      Nach Scan automatisch anhand Datum eintragen
 *      Kein passendes Datum: manueller / automatischer Eintrag wählen
 *      -> manuell: Termin wählen
 *      -> automatisch: Erster Null-Eintrag wird inkrementiert
 *
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    // Initialize
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //permissions
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"};
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

        View header=navigationView.getHeaderView(0);
        userNameTxt = (TextView)header.findViewById(R.id.textview_user_name);
        userMailTxt = (TextView)header.findViewById(R.id.textview_user_mail);
        currentLabTxt = (TextView)header.findViewById(R.id.textview_current_lab);
        currentGroupTxt = (TextView)header.findViewById(R.id.textview_current_group);
        userName = "Eduard Harwart";
        userMail = "harwartedu58020@th-nuernberg.de";
        currentLab = "INF2/1";
        currentGroup = "Gruppe 2";
        userNameTxt.setText(userName);
        userMailTxt.setText(userMail);
        currentLabTxt.setText(currentLab);
        currentGroupTxt.setText(currentGroup);

        if (savedInstanceState == null){
            navigationView.getMenu().getItem(0).setChecked(true);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            StudentTableFragment fragment = new StudentTableFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    // Verwenden, um auf vorhandene Berechtigungen zu prüfen
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions,
                                           int[] grantResults){
        switch(permsRequestCode){
            case 200:
                readExtAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                writeExtAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    public void fabClicked(View v){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    // show format and content
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanFormat = scanningResult.getFormatName();
            String scanContent = scanningResult.getContents();

            if((scanContent != null) && (scanFormat != null)) {
                StudentDataSource dataSource = new StudentDataSource(this);

                if (dataSource.studentExists(scanContent)) {
                    dataSource.insertAttd(scanContent, getEditor());
                    Student student = dataSource.getStudent(scanContent);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    StudentFragment fragment = new StudentFragment();
                    fragment.newInstance(student);
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    UnknownStudentFragment fragment = new UnknownStudentFragment();
                    fragment.newInstance(scanFormat, scanContent);
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            else
            {
                backToHome();
                toastMsg("Scan abgebrochen");
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Verhalten von Back-Button am Smartphone
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Drawer schließen falls geöffnet
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        // App schließen falls nur ein Fragment geöffnet ist
        else if (count == 1) {
            super.onBackPressed();
        }
        // ...sonst zurück zu vorheriger Ansicht
        else {
            backToHome();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_group) {
            backToHome();
        }
        else if (id == R.id.nav_add_member) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AddStudentFragment fragment = new AddStudentFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.nav_switch_group) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SwitchGroupFragment fragment = new SwitchGroupFragment();
            fragment.newInstance(currentGroup);
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.nav_new_group) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CreateGroupFragment fragment = new CreateGroupFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.nav_sync_db) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SyncFragment fragment = new SyncFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.nav_import_db) {

            StudentDataSource dataSource = new StudentDataSource(this);
            dataSource.importCSV(this, "students.csv");
            toastMsg("students.csv importiert");
        }
        else if (id == R.id.nav_cert) {
            toastMsg("Aktuell keine Funktion");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void backToHome(){
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StudentTableFragment fragment = new StudentTableFragment();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void toastMsg(String msg)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void setGroup(String grp)
    {
        this.currentGroup = grp;
        currentGroupTxt.setText(grp);
    }

    // Hardcoding zu Testzwecken
    private String getEditor()
    {
        return "11";
    }

}
