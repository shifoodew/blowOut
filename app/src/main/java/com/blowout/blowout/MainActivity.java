package com.blowout.blowout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blowout.blowout.Fragment.EstablishmentFragment;
import com.blowout.blowout.MyAdapter.EstablishmentAdapter;
import com.blowout.blowout.helper.SessionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        String name = getIntent().getStringExtra("name"); //fetch data from another activity.
        String user = getIntent().getStringExtra("username"); //fetch data from another activity.

        Log.d("MainActivity","name:" +name);
        Log.d("MainActivity","username:" +user);

        nav_header_name = findViewById(R.id.nav_header_username);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //Displaying the user info in nav_header_main.xml
//        nav_header_name.setText("hahah");

//        rvItem= findViewById(R.id.rv_recycler_view_fragment_accounts);//fragment_establishment.xml-> rvItem
////        cvItem= findViewById(R.id.cvItem);//card_Item.xml
//        rvItem.setHasFixedSize(true);
//
//        LinearLayoutManager manager= new LinearLayoutManager(this);
//        rvItem.setLayoutManager(manager);
//
//        //Get the dummy data
//        ArrayList<EstablishmentData> establishment_list = generatedDummy();
//
//        EstablishmentAdapter adapter= new EstablishmentAdapter(this, establishment_list);
//
//        rvItem.setAdapter(adapter);

    }

//    //List Method
//    private ArrayList<EstablishmentData> generatedDummy(){
//        ArrayList<EstablishmentData> list= new ArrayList<>();
//        for(int i= 0; i < 10; i++){
//
//            EstablishmentData est_list= new EstablishmentData();
//            est_list.estabImage= "https://pinoyinvestigador.files.wordpress.com/2013/07/2-jollibee.jpeg";
//            est_list.estabName= "Lapriza Food Corps (Branch "+i+")";
//            est_list.estabAddress= "Sambag "+i;
//
//            list.add(est_list);
//        }
//
//        return list;
//
//    }

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
            //My Cart
            Toast.makeText(getApplicationContext(), "My Cart coming soon", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
