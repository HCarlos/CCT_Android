package com.tabascoweb.cerocorrupciontabasco.Connections;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.tabascoweb.cerocorrupciontabasco.Classes.AppConfig;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.VolleyTaskListener;
import com.tabascoweb.cerocorrupciontabasco.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendArchivoWithVolley {
    private Activity activity;
    private Context context;
    private RequestQueue rQueue;
    private String url = AppConfig.URL_AGREGAR_ARCHIVO;
    private ArrayList<HashMap<String, String>> arraylist;
    public Bitmap bitmap;
    private static final String TAG = "RESPUESTA";
    private ProgressDialog pDialog;
    private VolleyTaskListener listener;

    public SendArchivoWithVolley(Activity _activity, Context _context) {
        this.activity = _activity;
        this.context = _context;
        listener = (VolleyTaskListener) context;

    }

    public void uploadImage(final Bitmap bitmap, final byte[] FileBytes, final String ExtensionArchivo){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Enviando datos...");
        pDialog.setCancelable(false);
        showDialog();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d(TAG,new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Log.e(TAG,jsonObject.getString("status"));
                            String msg = jsonObject.getString("message");
                            if (jsonObject.getString("status").equals("OK")) {
                                showDialog(activity, msg,true);
                            }else{
                                showDialog(activity, msg,false);
                            }
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            hideDialog();
                        } catch (JSONException e) {
                            hideDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IdDenuncia", String.valueOf(Singleton.getIdDenuncia()));
                params.put("UIDD", Singleton.getUUID());
                params.put("Device", Singleton.getDevice());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                String filename = "foto";
                Map<String, DataPart> params = new HashMap<>();
                switch (Singleton.getIdTarget()){
                    case 1:
                    case 2:
                        if (bitmap != null){
                            byte[] byte_arr = getFileDataFromDrawable(bitmap);
                            params.put(filename, new DataPart(  "image.jpg", byte_arr));
                        }
                        break;
                    case 3:
                    case 4:
                        if (FileBytes != null){
                            params.put(filename, new DataPart("video.mp4", FileBytes));
                        }
                        break;
                    case 5:
                        if (FileBytes != null){
                            params.put(filename, new DataPart("audio.mp3", FileBytes));
                        }
                        break;
                    case 6:
                        if (FileBytes != null){
                            params.put(filename, new DataPart("archivo."+ExtensionArchivo, FileBytes));
                        }
                        break;
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("api-key", "55e76dc4bbae25b066cb");
                params.put("Accept", "application/json");
                return params;
            }

        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(context);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void showDialog(Activity activity, String msg, final boolean isClosed){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (!isClosed) dialog.setContentView(R.layout.dialog_error);
        else dialog.setContentView(R.layout.dialog_succefully);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isClosed) listener.goBackActivity();
            }
        });

        dialog.show();

    }

}
