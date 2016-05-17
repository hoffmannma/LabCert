package de.th_nuernberg.harwedu.labcert;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.th_nuernberg.harwedu.labcert.database.StudentDataSource;
import de.th_nuernberg.harwedu.labcert.fragment.AddStudentFragment;
import de.th_nuernberg.harwedu.labcert.fragment.CreateGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragment.CsvSampleFragment;
import de.th_nuernberg.harwedu.labcert.fragment.StudentTableFragment;
import de.th_nuernberg.harwedu.labcert.fragment.StudentFragment;
import de.th_nuernberg.harwedu.labcert.fragment.SwitchGroupFragment;
import de.th_nuernberg.harwedu.labcert.fragment.UnknownStudentFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView scanFormatTxt;
    private TextView scanContentTxt;
    // Initialize
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //scanFormatTxt = (TextView)findViewById(R.id.scan_format_TV);
        //scanContentTxt = (TextView)findViewById(R.id.scan_content_TV);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "<Call Scanner>", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                IntentIntegrator scanIntegrator = new IntentIntegrator();
                scanIntegrator.initiateScan();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CsvSampleFragment fragment = new CsvSampleFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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

            StudentDataSource dataSource = new StudentDataSource(this);
            dataSource.openR();

            if(dataSource.studentExists(scanContent))
            {
                dataSource.close();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else
            {
                dataSource.close();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                UnknownStudentFragment fragment = new UnknownStudentFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
            getFragmentManager().popBackStack();
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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            StudentTableFragment fragment = new StudentTableFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
        else if (id == R.id.nav_csv_sample) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CsvSampleFragment fragment = new CsvSampleFragment();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.nav_export_db) {

        }
        else if (id == R.id.nav_cert) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
