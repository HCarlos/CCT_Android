package com.tabascoweb.cerocorrupciontabasco;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.tabascoweb.cerocorrupciontabasco.Activitys.AcercaDeActivity;
import com.tabascoweb.cerocorrupciontabasco.Activitys.AvisoDePrivacidadActivity;


import com.tabascoweb.cerocorrupciontabasco.Activitys.DenunciaActivity;
import com.tabascoweb.cerocorrupciontabasco.Activitys.MisDenunciasActivity;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Classes.Utilidades;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private Singleton Singleton;
    private Activity activity;
    private Context context;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Singleton = new Singleton();
        
        activity = this;
        context = this;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ImageView btnDP = (ImageView) findViewById(R.id.btnDenunciaPersonal);
        btnDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DenunciaActivity.class);
                Singleton.setIdTipoDenuncia(0);
                startActivity(intent);
            }
        });

        ImageView btnDA = (ImageView) findViewById(R.id.btnDenunciaAnonima);
        btnDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DenunciaActivity.class);
                Singleton.setIdTipoDenuncia(1);
                startActivity(intent);
            }
        });

        Utilidades.GetCamera(this,0);
        if ( Singleton.isIsCameraPresent() ){
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            try {
                Utilidades.GetGPS(this, lm, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Singleton.IsInit() ){
            showDialog(activity);
            Singleton.setInit(false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_denuncia) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_mis_denuncias) {
            Intent intent = new Intent(MainActivity.this, MisDenunciasActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(MainActivity.this, AvisoDePrivacidadActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_welcome_init);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnIntro);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }





}



