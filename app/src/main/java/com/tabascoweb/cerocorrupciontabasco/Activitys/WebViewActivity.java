package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Classes.Utilidades;
import com.tabascoweb.cerocorrupciontabasco.R;

import java.io.IOException;

public class WebViewActivity extends AppCompatActivity {
    private Activity activity;
    private Context context;
    private ProgressDialog pDialog;
    private Utilidades Utl;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ActionBar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(8);

            final Drawable leftArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            leftArrow.setColorFilter(getResources().getColor(R.color.colorBlanco), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);

        }

        activity = this;
        context = this;

        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText("Archivo "+ Singleton.getIdArchivo());

        webView = (WebView) findViewById(R.id.webView);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Cargando...");
        Utl = new Utilidades(pDialog);
        Utl.showDialog();

        Bundle params = getIntent().getExtras();
        String Url = params.getString("Url");
        String Extension = params.getString("Extension");

        onCreateObject(Url, Extension);

    }





    public boolean onCreateObject(String URL, String Extension){
        Log.e("URL WebView: ",URL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.loadUrl("javascript:window.location.reload( true )");

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCachePath(this.getCacheDir().getPath());

        webView.setWebViewClient(new Callback());
        switch (Extension){
            case "jpg":
            case "mp4":
                webView.loadUrl(URL);
                break;
            case "mp3":
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
                break;
                default:
                    webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + URL);
                    break;
        }

        return true;

    }


    private class Callback extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("on","PageStarted External");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
            pDialog.setMessage("...");
            Utl = new Utilidades(pDialog);
            Utl.hideDialog();
            Log.e("on","PageFinished External");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.e("should","OverrideUrlLoading External");
            return true;

        }

    }


    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // por defecto si escribes aquí el super el botón hará lo que debía hacer si lo quitas ya no hará lo que debía de hacer y puedes programar otros comportamientos.
        //Quita el super y has un finish() a la actividad o bien replanteate bien lo que quieres hacer cuando se presione hacia atrás.
    }




}
