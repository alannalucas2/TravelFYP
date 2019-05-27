package com.example.alannalucas.travelfyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class NavigationBarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, new ProfileFragment()).commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, new ProfilePage()).commit();

                Intent intent2 = new Intent(NavigationBarActivity.this, ProfilePage.class);
                startActivity(intent2);

                break;
            case R.id.nav_maps:

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NearbyLocationsFragment()).commit();
                /*Intent intent = new Intent(NavigationBarActivity.this, NearbyLocationsFragment.class);
                startActivity(intent);
                finish();*/
                break;
            case R.id.nav_users:
                Intent intent3 = new Intent(NavigationBarActivity.this, AllUsers.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_sendshare:
                Toast.makeText(this, "Send", Toast.LENGTH_LONG).show();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


}
