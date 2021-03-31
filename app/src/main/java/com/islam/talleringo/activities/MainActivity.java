package com.islam.talleringo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.islam.talleringo.R;
import com.islam.talleringo.fragments.AboutFragment;
import com.islam.talleringo.fragments.HomeFragment;
import com.islam.talleringo.utils.utils;

public class MainActivity extends AppCompatActivity {
    private Button logout ;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean fragment_transaction = false;
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menu_signOut:
                        utils.signOut();
                        startActivity(utils.updateUI(null,getApplicationContext()));
                        break;
                    case R.id.menu_about:
                        fragment = new AboutFragment();
                        fragment_transaction = true;
                        break;

                    case R.id.menu_home:
                        fragment = new HomeFragment();
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