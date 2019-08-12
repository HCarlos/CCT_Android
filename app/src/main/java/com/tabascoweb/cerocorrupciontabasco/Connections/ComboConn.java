package com.tabascoweb.cerocorrupciontabasco.Connections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.tabascoweb.cerocorrupciontabasco.Adapters.ComboAdapter;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.AsyncTaskListener;
import com.tabascoweb.cerocorrupciontabasco.R;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;



public class ComboConn extends AsyncTask<Void, Void, String> {
    private ProgressDialog dialog;
    private Context context;
    private Activity activity;
    private ArrayList<Opciones> items;
    private Spinner spinner;
    private AsyncTaskListener listener;

    public ComboConn(Context context, Spinner spinner, Activity activity) {
        this.context = context;
        this.spinner = spinner;
        this.activity = activity;
        listener= (AsyncTaskListener)context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        items = new ArrayList();
        dialog = new ProgressDialog(this.context);
        dialog.setMessage("Cargando datos...");
        dialog.setCancelable(false);
        dialog.show();
    }



    @Override
    protected String doInBackground(Void... params) {
        BufferedReader reader;
        StringBuffer buffer;
        String res = null;

        try {
            Log.e("URL de Consulta: ",Singleton.getUrlPasoConsulta());
            URL url = new URL(Singleton.getUrlPasoConsulta());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(40000);
            con.setConnectTimeout(40000);
            con.setRequestMethod(Singleton.getMethod());
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            InputStream inputStream;
            if (status == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
            } else {
                inputStream = con.getErrorStream();
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            res = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("JSON RESP:" + s);
        String response = s;
        try {

            JSONArray jObj = new JSONArray(response);
            JSONObject item = jObj.getJSONObject(0);
            Singleton.setArrayOpciones(null);
            String svar = "un elemento";
            switch (Singleton.getIdCombo()){
                case 0:
                        svar = "un Tipo de Gobierno";
                        break;
                case 1:
                        svar = "un Municipio";
                        break;
                case 2:
                        svar = "una Dependencia";
                        break;
                case 3:
                        svar = "una Área";
                        break;
            }
            items.add(new Opciones(0,"Seleccione "+svar));
            for (int j = 0; j < jObj.length(); j++) {
                item = jObj.getJSONObject(j);
                Log.w("Valor => ",item.getString("label"));
                items.add(new Opciones(item.getInt("data"),item.getString("label")));
            }

            if (items.size() > 0) {
                Singleton.setArrayOpciones(items);
                spinner.setVisibility(View.VISIBLE);
                final ComboAdapter adapter = new ComboAdapter(context, R.layout.spinner_item_devch, Singleton.getArrayOpciones());
                spinner.setAdapter(adapter);
                //spinner.setSelection(-1, false);
                //spinner.setOnItemSelectedListener(null);
                dialog.dismiss();

            } else {
                Toast.makeText(context.getApplicationContext(), "No Data to display", Toast.LENGTH_SHORT).show();
                errorListener();
            }

            dialog.dismiss();

        } catch (JSONException e) {
           Log.d("ERROR DE DATOS : ", e.getMessage());
           errorListener();

        }
    }

    protected void errorListener(){
        Singleton.setArrayOpciones(null);
        spinner.setVisibility(View.INVISIBLE);
        spinner.setAdapter(null);

        switch (Singleton.getIdCombo()){
            case 0:
                listener.updateTipoDeGobierno();
                break;
            case 1:
                listener.updateMunicipios();
                break;
            case 2:
                listener.updateDependencias();
                break;
            case 3:
                listener.updateAreas();
                break;
        }
        dialog.dismiss();
    }


}