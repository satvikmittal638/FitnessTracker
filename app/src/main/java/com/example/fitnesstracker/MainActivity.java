package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;


public class MainActivity extends AppCompatActivity {



//FOR NAVIGATION DRAWER
private NavigationView navigationView;
private ActionBarDrawerToggle toggle;
private Toolbar toolbar;
private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeVariablesOnStart();
        setSupportActionBar(toolbar);

        drawerLayout.addDrawerListener(toggle);//listens to the toggle
        toggle.syncState();//syncs its current state in accordance with the opening and closing of drawer layout



        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new OverviewFrag()).commit();
        Log.d("Stay Fit","Loaded OverviewFrag");

        navigationView.setCheckedItem(R.id.overview);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragToDisplay;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);//sends the drawer to the start

                switch (item.getItemId()){

                    case R.id.overview:
                        fragToDisplay=new OverviewFrag();
                        break;

                    case R.id.settings:
                        fragToDisplay=new SettingsFrag();
                        break;

                    case R.id.music:
                        fragToDisplay=new MusicFrag();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"An error has occurred",Toast.LENGTH_SHORT).show();



                }
            getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,fragToDisplay).commit();
            drawerLayout.closeDrawer(GravityCompat.START);//close the drawer
                return true;
            }
        });
    }

//USER DEFINED METHODS->
    private void initializeVariablesOnStart(){
        navigationView=findViewById(R.id.navView);
        toolbar=findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OPEN,R.string.CLOSE);//only takes a string resource
    }





}