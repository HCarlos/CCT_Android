package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.R;

public class VerVideoActivity extends AppCompatActivity {
    private Activity activity;
    private Context context;
    private MediaController MC;
    private Uri uri;
    private VideoView videoView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_video);

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

        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText("Video "+Singleton.getIdArchivo());

        activity = this;
        context = this;

        videoView = (VideoView) findViewById(R.id.videoView);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Cargando video...");
        dialog.setCancelable(false);
        dialog.show();

        videoView.setVideoURI( Uri.parse( Singleton.getPathFile() ) );
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                dialog.dismiss();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(VerVideoActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                        dialog.dismiss();
                    }
                });
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                dialog.dismiss();
                return false;
            }
        });


/*

        MC = new MediaController(context);
        MC.setMediaPlayer(videoView);
        videoView.setMediaController(MC);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //videoView.start();
                dialog.dismiss();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                MC = new MediaController(context);

                MC.setMediaPlayer(videoView);
                videoView.setMediaController(MC);

                videoView.requestFocus();
                //videoView.start();

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                videoView.stopPlayback();
                //videoView.setVideoURI(null);
                dialog.dismiss();
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });
        }

*/



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // por defecto si escribes aquí el super el botón hará lo que debía hacer si lo quitas ya no hará lo que debía de hacer y puedes programar otros comportamientos.
        //Quita el super y has un finish() a la actividad o bien replanteate bien lo que quieres hacer cuando se presione hacia atrás.
    }

}
