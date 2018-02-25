package com.blowout.blowout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.blowout.blowout.Fragment.CartFragment;
import com.blowout.blowout.Fragment.EstablishmentFragment;
import com.blowout.blowout.helper.SQLiteHandler;
import com.blowout.blowout.helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteHandler db;

    RecyclerView rvItem;
    CardView cvItem;

    private TextView nav_header_name;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nav_header_name = findViewById(R.id.nav_header_username);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        //Fetching user details from SQLite
        HashMap<String, String> dbData = db.getUserDetails();

        String id= dbData.get("u_id");
        Log.d("MainActivity", "Check Data main activity  user id       : " + id);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

        //layout->menu->activity_main_drawer.xml
        if (id == R.id.nav_establishment) {

            EstablishmentFragment ef= new EstablishmentFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main_relativelayout_for_fragment, ef).commit();

            Toast.makeText(getApplicationContext(), "Establishments", Toast.LENGTH_LONG).show();

        }else if (id == R.id.nav_cart) {
            String user_id = getIntent().getStringExtra("user_id"); //fetch data from loginActivty
            Log.d("Navigation","user id:"      +user_id);

            //Cart
            CartFragment cart= new CartFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main_relativelayout_for_fragment, cart).commit();

//            Toast.makeText(getApplicationContext(), "My Cart coming soon", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUserData();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
