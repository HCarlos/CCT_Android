package com.tabascoweb.cerocorrupciontabasco.Connections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tabascoweb.cerocorrupciontabasco.Classes.AppConfig;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.MisArchivosTaskListener;

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

public class MisArchivosConn extends AsyncTask<Void, Void, String> {
    private ProgressDialog dialog;
    private Context context;
    private Activity activity;
    private ArrayList<Opciones> items;
    private MisArchivosTaskListener listener;

    public MisArchivosConn(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        listener = (MisArchivosTaskListener) context;
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
            Log.e("URL de Consulta: ", AppConfig.getURLMisArchivos());
            URL url = new URL(AppConfig.getURLMisArchivos());
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

            for (int j = 0; j < jObj.length(); j++) {
                item = jObj.getJSONObject(j);
                items.add(new Opciones(item.getInt("idarchivo"),item.getString("archivo"),item.getString("creado_el"),item.getInt("iddenuncia")));
            }

            if (items.size() > 0) {
                Singleton.setArrayOpciones(items);
                dialog.dismiss();
                listener.preparaMisArchivosAdapter(Singleton.getArrayOpciones());
            } else {
                Toast.makeText(context.getApplicationContext(), "No Data to display", Toast.LENGTH_SHORT).show();
                errorListener();
                dialog.dismiss();

            }

        } catch (JSONException e) {
            Log.d("ERROR DE DATOS : ", e.getMessage());
            errorListener();
            dialog.dismiss();

        }
    }

    protected void errorListener(){
        Singleton.setArrayOpciones(null);

    }


}
