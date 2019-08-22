package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.tabascoweb.cerocorrupciontabasco.Classes.Media.UploadHelper;
import com.tabascoweb.cerocorrupciontabasco.Classes.Utilidades;
import com.tabascoweb.cerocorrupciontabasco.Connections.ComboConn;
import com.tabascoweb.cerocorrupciontabasco.Connections.SendFormDenunciaWithVolley;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.AsyncTaskListener;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.VolleyTaskListener;
import com.tabascoweb.cerocorrupciontabasco.MainActivity;
import com.tabascoweb.cerocorrupciontabasco.R;
import com.tabascoweb.cerocorrupciontabasco.Classes.AppConfig;
import com.tabascoweb.cerocorrupciontabasco.Classes.BottomSheetDialog;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.PhotoUtils;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MANAGE_DOCUMENTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DenunciaActivity extends AppCompatActivity implements AsyncTaskListener, VolleyTaskListener, BottomSheetDialog.BottomSheetListener {
    private static final String TAG = "RESPUESTA";
    private Context context;
    private Activity activity;
    private String uuid, device;
    private TextInputLayout selTG, selMun, selDep, selAreas;
    private Spinner spTG, spMun, spDep, spAreas;
    private ViewGroup.LayoutParams prMun, prDep, prAreas;
    private Integer hMun, hDep, hAreas;
    private int ano, mes, dia, hora, min;
    public TextView tvFecha, tvHora;
    private EditText app, apm, nombre, celular, email, lugarhechos, hechos;
    private ImageView imgImagen;
    private VideoView vidVideo;
    private TextView filenameArchivoSel;
    private ProgressDialog dialog;
    MediaController MC;
    private LinearLayout LLAnonimous;

    private Uri mImageUri;

    private static final int
                            PERMISSION_CODE = 1000,
                            PERMISSION_CODE_2 = 2000,
                            IMAGE_CAPTURE_CODE = 1001,
                            PERMISSION_CODE_GALLERY = 1002,
                            GALLERY_REQUEST_CODE=1003,
                            CAMERA_REQUEST_CODE=1004,
                            PERMISSION_CODE_AUDIO = 1005,
                            AUDIO_REQUEST_CODE = 1006,
                            PERMISSION_CODE_VIDEO = 1007,
                            VIDEO_REQUEST_CODE = 1008,
                            PERMISSION_CODE_GALLERY_VIDEO = 1009,
                            GALLERY_VIDEO_REQUEST_CODE = 1010,
                            PERMISSION_CODE_DOC = 1011,
                            DOC_REQUEST_CODE = 1012;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ActionBar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            }
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(8);

            final Drawable leftArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            leftArrow.setColorFilter(getResources().getColor(R.color.colorBlanco), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);

        }

        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        if (Singleton.getIdTipoDenuncia()==0) txtTitulo.setText("Denuncia Personal");
        else txtTitulo.setText("Denuncia Anónima");

        tvFecha = (TextView) findViewById(R.id.fecha);
        tvHora  = (TextView) findViewById(R.id.hora);

        app = (EditText) findViewById(R.id.apellido_paterno);
        apm = (EditText) findViewById(R.id.apellido_materno);
        nombre = (EditText) findViewById(R.id.nombre);

        celular = (EditText) findViewById(R.id.celular);
        email = (EditText) findViewById(R.id.email);
        lugarhechos = (EditText) findViewById(R.id.lugarHechos);
        hechos = (EditText) findViewById(R.id.hechos);

        uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Singleton.setUUID(uuid);

        device = Utilidades.getDeviceName();
        Singleton.setDevice(device);

        Singleton.setIdTipoGobierno(0);
        Singleton.setIdMunicipio(0);
        Singleton.setIdDependencia(0);
        Singleton.setIdArea(0);

        activity = this;
        context = this;

        checkPermissions();

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            Utilidades.GetGPS(this, lm, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LLAnonimous = (LinearLayout) findViewById(R.id.ll_denuncia_personal);
        if (Singleton.getIdTipoDenuncia()==1){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) LLAnonimous.getLayoutParams();
            params.height = 1;
            LLAnonimous.setLayoutParams(params);        }

        inicializaCombos();
        inicializaFechaYHora();
        inizializamosSubirArchivo();

    }

    private void inicializaFechaYHora(){
        Calendar cal =Calendar.getInstance();
        ano  = cal.get(cal.YEAR);
        mes  = cal.get(cal.MONTH) + 1;
        dia  = cal.get(cal.DAY_OF_MONTH);
        hora = cal.get(cal.HOUR_OF_DAY);
        min  = cal.get(cal.MINUTE);

        tvFecha.setText(setDate(ano,mes,dia));
        tvHora.setText(setHora(hora,min));

        Button btnFecha = (Button) findViewById(R.id.btnFecha);
        Button btnHora  = (Button) findViewById(R.id.btnHora);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpD  = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tvFecha.setText(setDate(i,i1+1,i2));
                    }
                },ano,mes-1,dia);
                dpD.show();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpD = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvHora.setText(setHora(i,i1));
                    }
                },hora,min,true);
                tpD.show();
            }
        });


    }


    private String setDate(int Ano, int Mes, int Dia){
        return Integer.toString(Ano) + '-' +  Mes + '-' +  Dia;
    }

    private String setHora(int Hora, int Min){
        return Integer.toString(Hora) + ':' +  Min;
    }

    public void updateTipoDeGobierno() {
    }

    public void updateMunicipios() {

    }

    public void updateDependencias() {
    }

    public void updateAreas() {
//        final ViewGroup.LayoutParams prAreas = selAreas.getLayoutParams();
        prAreas = selAreas.getLayoutParams();
        prAreas.height = 0;
        selAreas.setLayoutParams(prAreas);

    }


    private void inicializaCombos(){


        selTG    = (TextInputLayout) findViewById(R.id.selTipoGobierno);
        selMun   = (TextInputLayout) findViewById(R.id.selMunicipios);
        selDep   = (TextInputLayout) findViewById(R.id.selDependencias);
        selAreas = (TextInputLayout) findViewById(R.id.selAreas);

        spTG = (Spinner) findViewById(R.id.tipp_gobierno);
        spMun = (Spinner) findViewById(R.id.municipios);
        spDep = (Spinner) findViewById(R.id.dependencias);
        spAreas = (Spinner) findViewById(R.id.areas);

//        final ViewGroup.LayoutParams prMun = selMun.getLayoutParams();
        prMun = selMun.getLayoutParams();
        hMun = prMun.height;
        prMun.height = 0;

//        final ViewGroup.LayoutParams prDep = selDep.getLayoutParams();
        prDep = selDep.getLayoutParams();
        hDep = prDep.height;
        prDep.height = 0;

//        final ViewGroup.LayoutParams prAreas = selAreas.getLayoutParams();
        prAreas = selAreas.getLayoutParams();
        hAreas = prAreas.height;
        prAreas.height = 0;

        spTG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Opciones opt = (Opciones) adapterView.getAdapter().getItem(i);
                Singleton.setIdTipoGobierno(opt.getKey());

                spMun.setAdapter(null);
                spDep.setAdapter(null);
                spAreas.setAdapter(null);

                prMun.height = 0;
                prDep.height = 0;
                prAreas.height = 0;

                selMun.setLayoutParams(prMun);
                selDep.setLayoutParams(prDep);
                selAreas.setLayoutParams(prAreas);

                if (Singleton.getIdTipoGobierno()  == 1){
                    prMun.height = 0;
                    selMun.setLayoutParams(prMun);
//                    spMun.setVisibility(View.INVISIBLE);

                    prAreas.height = 0;
                    selAreas.setLayoutParams(prAreas);

                    prDep.height = hDep;
                    selDep.setLayoutParams(prDep);

                    Singleton.setUrlPasoConsulta(AppConfig.getURLDependencias());
                    Singleton.setIdCombo(2);
                    new ComboConn(context, spDep, activity).execute();
                    Log.w("Entro spDep ","OK");
                }else if (Singleton.getIdTipoGobierno()  == 2){
                    prMun.height = hMun;
                    selMun.setLayoutParams(prMun);
//                    spMun.setVisibility(View.VISIBLE);
                    Singleton.setUrlPasoConsulta(AppConfig.URL_MUNICIPIOS);
                    Singleton.setIdCombo(1);
                    new ComboConn(context, spMun, activity).execute();

                }
                //Log.w("Entro spDep ","NO");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Opciones opt = (Opciones) adapterView.getAdapter().getItem(i);
                Singleton.setIdMunicipio(opt.getKey());
                spAreas.setAdapter(null);
                spDep.setAdapter(null);
                if (Singleton.getIdTipoGobierno() > 0 && Singleton.getIdMunicipio() > 0){
                    prAreas.height = 0;
                    selAreas.setLayoutParams(prAreas);
                    Singleton.setUrlPasoConsulta(AppConfig.getURLDependencias());
                    Singleton.setIdCombo(2);
                    prDep.height = hDep;
                    selDep.setLayoutParams(prDep);
                    new ComboConn(context, spDep, activity).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Opciones opt = (Opciones) adapterView.getAdapter().getItem(i);
                Singleton.setIdDependencia(opt.getKey());
                if (Singleton.getIdDependencia() > 0){
                    spAreas.setAdapter(null);
                    Singleton.setUrlPasoConsulta(AppConfig.getURLAreas());
                    Singleton.setIdCombo(3);
                    prAreas.height = hAreas;
                    selAreas.setLayoutParams(prAreas);
                    new ComboConn(context, spAreas, activity).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Opciones opt = (Opciones) adapterView.getAdapter().getItem(i);
                Singleton.setIdArea(opt.getKey());
                Log.e("Id Area", String.valueOf(Singleton.getIdArea()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Singleton.setIdCombo(0);
        Singleton.setUrlPasoConsulta(AppConfig.URL_TIPO_GOBIERNO);
        Singleton.setMethod("GET");

        new ComboConn(this, spTG, this).execute();

    }

    // ******************************************************************************************
    // METODOS DE SUBIR ARCHIVOS INICIAN AQUI
    // ******************************************************************************************

    private void inizializamosSubirArchivo(){
        imgImagen = (ImageView) findViewById(R.id.imgArchivo);
        vidVideo = (VideoView) findViewById(R.id.vidArchivo);
        filenameArchivoSel = (TextView) findViewById(R.id.nombreArchivoSeleccionado);
        Button btnImagen = (Button) findViewById(R.id.btnImagen);
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bsD = new BottomSheetDialog();
                bsD.show(getSupportFragmentManager(),"seleccionarArchivo");
            }
        });

        Button btnSendData = (Button) findViewById(R.id.btnSendData);
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataToSave();
                SendFormDenunciaWithVolley sdv = new SendFormDenunciaWithVolley(activity, context);
                switch (Singleton.getIdTarget()){
                    case 1:
                    case 2:
                        sdv.bitmap = ((BitmapDrawable) Singleton.getImagen().getDrawable()).getBitmap();
                        sdv.uploadImage(sdv.bitmap, null, null);
                        break;
                    case 3:
                    case 4:
                        Uri VideoURI = Uri.parse(String.valueOf(Singleton.getUriData()));
                        byte[] VideoBytes = UploadHelper.getFileDataFromDrawable(activity,VideoURI);
                        sdv.uploadImage(null, VideoBytes, null);
                        break;
                    case 5:
                        Uri AudioURI = Uri.parse(String.valueOf(Singleton.getUriData()));
                        byte[] AudioBytes = UploadHelper.getFileDataFromDrawable(activity,AudioURI);
                        sdv.uploadImage(null, AudioBytes, null);
                        break;
                    case 6:
                        Uri Archivo = Uri.parse(String.valueOf(Singleton.getUriData()));
                        byte[] ArchivoBytes = UploadHelper.getFileDataFromDrawable(activity,Archivo);
                        String extension = Utilidades.getfileExtensionFromUri(activity,Archivo);
                        sdv.uploadImage(null, ArchivoBytes,extension);
                        break;
                    default:
                        sdv.uploadImage(null, null,null);
                        break;
                }

            }
        });
    }

    @Override
    public void onButtonBSDClicked(int Tipo) {
        filenameArchivoSel.setText("");
        Singleton.setIdTarget(Tipo);
        switch (Tipo){
            case 1:
                PrepararCámara();
                break;
            case 2:
                PrepararGalería();
                break;
            case 3:
                PrepararCámaraParaVideo();
                break;
            case 4:
                PrepararGaleríaDeVideos();
                break;
            case 5:
                PrepararGaleríaAudio();
                break;
            case 6:
                PrepararGaleríaArchivos();
                break;
        }
    }

    private void PrepararCámara() {
        if (PhotoUtils.chechPermission1(this)){
            tomarFoto();
        }
    }

    public void tomarFoto() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void PrepararGalería() {
        if (PhotoUtils.chechPermission1(this)){
            seleccionarImagen();
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/jpg","image/png"};
        intent.putExtra(intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    private void PrepararCámaraParaVideo(){
        if (PhotoUtils.chechPermission2(this)){
            capturarVideo();
        }
    }

    public void capturarVideo() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Desde la Cámara");
        mImageUri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, VIDEO_REQUEST_CODE);
        }

    }

    public void PrepararGaleríaDeVideos(){
        if (PhotoUtils.chechPermission2(this)){
            seleccionarVideo();
        }
    }

    public void seleccionarVideo(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_VIDEO_REQUEST_CODE);
        }
    }

    private void PrepararGaleríaAudio() {
        if (PhotoUtils.chechPermission3(this)){
            reproducirAudio();
        }
    }

    public void reproducirAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("audio/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        }
    }

    private void PrepararGaleríaArchivos() {
        if (PhotoUtils.chechPermission4(this)){
            verArchivo();
        }
    }

    public void verArchivo() {

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "applications/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"Escoge un archivo"), DOC_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img;
        VideoView vid;
        Bitmap bmFrame = null;

        if (resultCode == RESULT_OK) {
            // Se muestta la imagen
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    Log.e("DATA: ", "Entro");
                    img = imgImagen;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(photo);
                    Singleton.setImagen(img);
                    Singleton.setUriData(PhotoUtils.getImageUri(context, photo));
                    setFileName();

                    break;
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    Singleton.setUriData(data.getData());
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    setFileName();

                    cursor.close();
                    img = imgImagen;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Singleton.setPathFile(data.getData().getPath());
                    Log.e("PATH FILE IN : ", data.getData().getPath());
                    img.setImageBitmap(bitmap);
                    Singleton.setImagen(img);
                    imgImagen = Singleton.getImagen();
                    Singleton.setPathFile(imgDecodableString);
                    break;
                case VIDEO_REQUEST_CODE:

                    Uri uri_video = null;
                    vid = vidVideo;
                    uri_video = data.getData();
                    vid.setVideoURI(uri_video);
                    Singleton.setUriData(data.getData());
                    setFileName();

                    Singleton.setVideo(vid);
                    vidVideo = Singleton.getVideo();
                    MediaController mediaController = new MediaController(this);
                    vidVideo.setMediaController(mediaController);

                    imgImagen.setImageResource(R.drawable.ic_video_library_tizne_202dp);
                    vidVideo.start();

                    break;
                case GALLERY_VIDEO_REQUEST_CODE:
                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Cargando video...");
                    dialog.setCancelable(false);
                    dialog.show();

                    Uri uri_video2 = data.getData();
                    vidVideo.setVideoURI(uri_video2);

                    vidVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                        }
                    });

                    vidVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {

                            MC = new MediaController(context);
                            Singleton.setVideo(vidVideo);
                            Singleton.setUriData(data.getData());
                            setFileName();

                            MC.setMediaPlayer(vidVideo);
                            vidVideo.setMediaController(MC);

                            vidVideo.requestFocus();
                            dialog.dismiss();
                            vidVideo.start();

                        }
                    });

                    vidVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            vidVideo.stopPlayback();
                            vidVideo.setVideoURI(null);
                            dialog.dismiss();
                            return false;
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        vidVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                                dialog.dismiss();
                                return false;
                            }
                        });
                    }

                    break;

                case AUDIO_REQUEST_CODE:
                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Cargando audio...");
                    dialog.setCancelable(false);
                    dialog.show();

                    Uri uri_video3 = data.getData();
                    vidVideo.setVideoURI(uri_video3);

                    vidVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                        }
                    });

                    vidVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            imgImagen.setImageResource(R.drawable.ic_audiotrack_black_24dp);
                            MC = new MediaController(context);
                            Singleton.setVideo(vidVideo);
                            Singleton.setUriData(data.getData());
                            setFileName();

                            MC.setMediaPlayer(vidVideo);
                            vidVideo.setMediaController(MC);

                            vidVideo.requestFocus();
                            dialog.dismiss();
                            vidVideo.start();

                        }
                    });

                    vidVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            vidVideo.stopPlayback();
                            vidVideo.setVideoURI(null);
                            dialog.dismiss();

                            return false;
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        vidVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                                dialog.dismiss();
                                return false;
                            }
                        });
                    }

                    break;

                case DOC_REQUEST_CODE:
                    MC = null;
                    Singleton.setUriData(data.getData());
                    imgImagen.setImageResource(R.drawable.ic_file_black_24dp);
                    setFileName();
                    break;
            }
        }
    }


    private void setFileName(){
        Uri Archivo = Uri.parse(String.valueOf(Singleton.getUriData()));
        filenameArchivoSel.setText(Utilidades.getFilenameFromUri(activity,Archivo));
    }


/*


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("PERMISO DENUNCIA: ", String.valueOf(requestCode));
        switch (requestCode){
            case PERMISSION_CODE:
            case PERMISSION_CODE_2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //tomarFoto();
                }else{
                    Toast.makeText(this, "No tiene permiso para usar la cámara", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission1(this);
                }
                break;
            case PERMISSION_CODE_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //seleccionarImagen();
                }else{
                    Toast.makeText(this, "No tiene permiso para ver las imágenes", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission1(this);
                }
                break;
            case PERMISSION_CODE_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //reproducirAudio();
                }else{
                    Toast.makeText(this, "No tiene permiso para usar el audio", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission3(this);
                }
                break;
            case PERMISSION_CODE_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //capturarVideo();
                }else{
                    Toast.makeText(this, "No tiene permiso para grabar videos", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission2(this);
                }
                break;
            case PERMISSION_CODE_GALLERY_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //seleccionarVideo();
                }else{
                    Toast.makeText(this, "No tiene permiso para ver los videos de esta cámara", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission2(this);
                }
                break;
            case PERMISSION_CODE_DOC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    seleccionarVideo();
                }else{
                    Toast.makeText(this, "No tiene permiso para ver los videos de esta cámara", Toast.LENGTH_SHORT).show();
                    PhotoUtils.chechPermission4(this);
                }
                break;
        }
    }

*/

// *******************************************************************************************
// *******************************************************************************************
// *******************************************************************************************



    public void getDataToSave(){
//        private TextView tvFecha, tvHora;
//        private EditText  lugarhechos, hechos;

        Singleton.setFecha(tvFecha.getText().toString());
        Singleton.setHora(tvHora.getText().toString());

        Singleton.setApellido_Paterno(app.getText().toString());
        Singleton.setApellido_Materno(apm.getText().toString());
        Singleton.setNombre(nombre.getText().toString());

        Singleton.setCelular(celular.getText().toString());
        Singleton.setCorreo_Electronico(email.getText().toString());

        Singleton.setLugar_Denuncia(lugarhechos.getText().toString());
        Singleton.setDenuncia(hechos.getText().toString());



    }

    @Override
    public void goBackActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void checkPermissions(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_DOCUMENTS)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED
        ){
            //Do_SOme_Operation();
        }else{
            requestStoragePermission();
        }
    }

    public void requestStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this
                    ,new String[]{INTERNET, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, ACCESS_NETWORK_STATE, MANAGE_DOCUMENTS, RECORD_AUDIO},1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1234:if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                // Do_SOme_Operation();
            }
            default:super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }






}
