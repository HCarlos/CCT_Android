package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Connections.MiDenunciaConn;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.MiDenunciaTaskListener;
import com.tabascoweb.cerocorrupciontabasco.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MiDenunciaActivity extends AppCompatActivity implements MiDenunciaTaskListener {
    private Activity activity;
    private Context context;
    private ImageView btnViewFiles;
    private int IdDenuncia;

    private TextView tvTipoGobierno;
    private TextView tvMunicipio;
    private TextView tvDependencia;
    private TextView tvArea;

    private EditText fecha_registro;
    private EditText fecha_denuncia;
    private EditText estatus;
    private EditText tipo_denuncia;
    private EditText nombre_completo;
    private EditText correo_electronico;
    private EditText celular;
    private EditText lugar_denuncia;
    private EditText denuncia;

    private LinearLayout llTipoDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_denuncia);

        activity = this;
        context = this;

        btnViewFiles = (ImageView) findViewById(R.id.btnViewFiles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ActionBar3);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(8);

            final Drawable leftArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            leftArrow.setColorFilter(getResources().getColor(R.color.colorBlanco), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);

        }
        IdDenuncia = Singleton.getIdDenuncia();
        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText("Folio: UIPE-"+IdDenuncia+"-TAB");

        tvTipoGobierno = (TextView) findViewById(R.id.tvTipoGobierno);
        tvMunicipio = (TextView) findViewById(R.id.tvMunicipio);
        tvDependencia = (TextView) findViewById(R.id.tvDependencia);
        tvArea = (TextView) findViewById(R.id.tvArea);

        fecha_registro = (EditText) findViewById(R.id.fecha_registro);
        fecha_denuncia = (EditText) findViewById(R.id.fecha_denuncia);
        estatus = (EditText) findViewById(R.id.estatus);
        tipo_denuncia = (EditText) findViewById(R.id.tipo_denuncia);
        nombre_completo = (EditText) findViewById(R.id.nombre_completo);
        correo_electronico = (EditText) findViewById(R.id.correo_electronico);
        celular = (EditText) findViewById(R.id.celular);
        lugar_denuncia = (EditText) findViewById(R.id.lugar_denuncia);
        denuncia = (EditText) findViewById(R.id.denuncia);

        fecha_registro.setEnabled(false);
        fecha_denuncia.setEnabled(false);
        estatus.setEnabled(false);
        tipo_denuncia.setEnabled(false);
        nombre_completo.setEnabled(false);
        correo_electronico.setEnabled(false);
        celular.setEnabled(false);
        lugar_denuncia.setEnabled(false);
        denuncia.setEnabled(false);

        new MiDenunciaConn(context,activity,tvTipoGobierno).execute();

        btnViewFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MisArchivosActivity.class);
                intent.putExtra("IdDenuncia", IdDenuncia );
                activity.startActivity(intent);
                activity.finish();
            }
        });


    }

    @Override
    public void updateMiDenuncia(JSONObject item) {
        try {



            tvTipoGobierno.setText( item.getString("tipo_gobierno") );
            tvMunicipio.setText( item.getString("municipio") );
            tvDependencia.setText( item.getString("dependencia") );
            tvArea.setText( item.getString("area") );

            fecha_registro.setText( item.getString("creado_el") );

            String FechaDenuncia = item.getString("fecha") + " " + item.getString("hora");
            fecha_denuncia.setText( FechaDenuncia );

            estatus.setText( item.getString("estatus") );
            tipo_denuncia.setText( item.getString("mi_tipo_denuncia") );

            String NombreCompleto = item.getString("apellido_paterno") + " " + item.getString("apellido_materno") + " " + item.getString("nombre");
            nombre_completo.setText(NombreCompleto);

            correo_electronico.setText( item.getString("correo_electronico") );
            celular.setText( item.getString("celular") );
            lugar_denuncia.setText( item.getString("lugar_denuncia") );
            denuncia.setText( item.getString("denuncia") );

            int IdTipoDenuncia = item.getInt("idtipodenuncia");
            int IdMunicipio = item.getInt("idmunicipio");
            int IdArea = item.getInt("idarea");

            llTipoDenuncia = (LinearLayout) findViewById(R.id.llTipoDenuncia);
            if (IdTipoDenuncia==1){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTipoDenuncia.getLayoutParams();
                params.height = 1;
                llTipoDenuncia.setLayoutParams(params);
            }
            if (IdMunicipio <= 0){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvMunicipio.getLayoutParams();
                params.height = 0;
                tvMunicipio.setLayoutParams(params);
            }
            if (IdArea <= 0){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvArea.getLayoutParams();
                params.height = 0;
                tvArea.setLayoutParams(params);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
