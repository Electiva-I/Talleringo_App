package com.islam.talleringo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.islam.talleringo.R;
import com.islam.talleringo.database.LiveData.DataViewModel;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.fragments.AboutFragment;
import com.islam.talleringo.fragments.HomeFragment;
import com.islam.talleringo.fragments.MaintenanceFragment;
import com.islam.talleringo.fragments.RecordFragment;
import com.islam.talleringo.fragments.SettingsFragment;
import com.islam.talleringo.fragments.VehicleFragment;
import com.islam.talleringo.utils.utils;

public class MainActivity extends AppCompatActivity {
    private MenuItem prevMenuItem ;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        Fragment fragment = new HomeFragment();

        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        final Observer<Vehicle> namObserver = new Observer<Vehicle>() {
            @Override
            public void onChanged(Vehicle vehicle) {
                navigationView.getMenu().getItem(2).setEnabled(true);
            }
        };
        dataViewModel.getNewVehicle().observe(this, namObserver);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }else {
                    navigationView.getMenu().getItem(0).setChecked(false);

                }

                prevMenuItem = item;
                boolean fragment_transaction = false;
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menu_signOut:
                        utils.signOut();
                        LoginManager.getInstance().logOut();
                        writeSharedPreference(-1);
                        startActivity(utils.updateUI(null,getApplicationContext()));
                        finish();
                        break;
                    case R.id.menu_about:
                        fragment = new AboutFragment();
                        fragment_transaction = true;
                        break;
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        fragment_transaction = true;
                        break;
                    case R.id.menu_vehicles:
                        fragment = new VehicleFragment();
                        fragment_transaction = true;
                        break;
                    case R.id.menu_maintenance:
                        fragment = new MaintenanceFragment();
                        fragment_transaction = true;
                        break;
                    case R.id.menu_settings:
                        fragment = new SettingsFragment();
                        fragment_transaction = true;
                        break;
                    case R.id.menu_record:
                        fragment = new RecordFragment();
                        fragment_transaction = true;
                        break;
                }
                if (fragment_transaction)   {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, fragment)
                            .commit();
                    item.setChecked(true);
                    getSupportActionBar().setTitle(item.getTitle());
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
        setToolbar();

        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(utils.updateUI(null, getApplicationContext()));
            }
        });*/


    }

    private void writeSharedPreference(int type) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("type", type);
        editor.commit();
    }
    private void  setToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}