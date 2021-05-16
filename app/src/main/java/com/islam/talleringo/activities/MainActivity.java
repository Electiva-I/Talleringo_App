package com.islam.talleringo.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MenuItem prevMenuItem ;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        Fragment fragment = new HomeFragment();

        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        final Observer<Vehicle> namObserver = vehicle -> navigationView.getMenu().getItem(2).setEnabled(true);
        dataViewModel.getNewVehicle().observe(this, namObserver);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            }else {
                navigationView.getMenu().getItem(0).setChecked(false);

            }

            prevMenuItem = item;
            boolean fragment_transaction = false;
            Fragment fragment1 = null;
            switch (item.getItemId()){
                case R.id.menu_signOut:
                    utils.signOut();
                    LoginManager.getInstance().logOut();
                    writeSharedPreference();
                    startActivity(utils.updateUI(null,getApplicationContext()));
                    finish();
                    break;
                case R.id.menu_about:
                    fragment1 = new AboutFragment();
                    fragment_transaction = true;
                    break;
                case R.id.menu_home:
                    fragment1 = new HomeFragment();
                    fragment_transaction = true;
                    break;
                case R.id.menu_vehicles:
                    fragment1 = new VehicleFragment();
                    fragment_transaction = true;
                    break;
                case R.id.menu_maintenance:
                    fragment1 = new MaintenanceFragment();
                    fragment_transaction = true;
                    break;
                case R.id.menu_settings:
                    fragment1 = new SettingsFragment();
                    fragment_transaction = true;
                    break;
                case R.id.menu_record:
                    fragment1 = new RecordFragment();
                    fragment_transaction = true;
                    break;
            }
            if (fragment_transaction)   {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment1)
                        .commit();
                item.setChecked(true);
                Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
                drawerLayout.closeDrawers();
            }
            return false;
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

    private void writeSharedPreference() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("type", -1);
        editor.apply();
    }
    private void  setToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_burger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}