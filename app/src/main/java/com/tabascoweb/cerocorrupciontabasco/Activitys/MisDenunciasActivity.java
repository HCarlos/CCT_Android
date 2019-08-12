package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabascoweb.cerocorrupciontabasco.Adapters.MisDenunciasAdapter;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Connections.MisDenunciasConn;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.MisDenunciasTaskListener;
import com.tabascoweb.cerocorrupciontabasco.R;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MisDenunciasActivity extends AppCompatActivity implements MisDenunciasTaskListener {
    private Activity activity;
    private Context context;
    private ImageView btnRefresh;
    private RecyclerView rvDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_denuncias);

        activity = this;
        context = this;

        rvDenuncia = (RecyclerView) activity.findViewById(R.id.rvMisDenuncias);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(VERTICAL);
        rvDenuncia.setLayoutManager(llm);


        btnRefresh = (ImageView) findViewById(R.id.btnRefresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ActionBar2);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(8);

            final Drawable leftArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            leftArrow.setColorFilter(getResources().getColor(R.color.colorBlanco), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);

        }

        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText("Mis Denuncias");

        String uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Singleton.setUUID(uuid);

        new MisDenunciasConn(context,activity).execute();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MisDenunciasConn(context,activity).execute();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void prepareMisDenunciasAdapter(ArrayList<Opciones> items) {
        MisDenunciasAdapter adapter = new MisDenunciasAdapter(activity,context,Singleton.getArrayOpciones());
        rvDenuncia.setAdapter(adapter);
    }
}
