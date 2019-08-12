package com.tabascoweb.cerocorrupciontabasco.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tabascoweb.cerocorrupciontabasco.Adapters.MisArchivosAdapter;
import com.tabascoweb.cerocorrupciontabasco.Classes.BottomSheetDialog;
import com.tabascoweb.cerocorrupciontabasco.Classes.Media.UploadHelper;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.PhotoUtils;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.Classes.Utilidades;
import com.tabascoweb.cerocorrupciontabasco.Connections.MisArchivosConn;
import com.tabascoweb.cerocorrupciontabasco.Connections.SendArchivoWithVolley;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.MisArchivosTaskListener;
import com.tabascoweb.cerocorrupciontabasco.Interfaces.VolleyTaskListener;
import com.tabascoweb.cerocorrupciontabasco.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MisArchivosActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener, VolleyTaskListener, MisArchivosTaskListener {
    private Activity activity;
    private Context context;
    private ImageView btnRefreshFiles;
    private ImageView btnAddFiles;
    private int IdDenuncia;
    private static final String TAG = "RESPUESTA";
    private ImageView imgImagen;
    private VideoView vidVideo;
    private TextView filenameArchivoSel;
    MediaController MC;
    private RecyclerView rvMisArchivos;
    private String fileName;

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
        setContentView(R.layout.activity_mis_archivos);

        activity = this;
        context = this;

        fileName = "";

        rvMisArchivos = (RecyclerView) activity.findViewById(R.id.rvMisArchivos);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(VERTICAL);
        rvMisArchivos.setLayoutManager(llm);

        btnRefreshFiles = (ImageView) findViewById(R.id.btnRefreshFiles);
        btnAddFiles = (ImageView) findViewById(R.id.btnAddFiles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ActionBar4);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(8);

            final Drawable leftArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            leftArrow.setColorFilter(getResources().getColor(R.color.colorBlanco), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);

            Log.e("Si ","Entro");

        }

        IdDenuncia = Singleton.getIdDenuncia();
        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText("Folio: UIPE-"+IdDenuncia+"-TAB");

        String uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Singleton.setUUID(uuid);

        String device = Utilidades.getDeviceName();
        Singleton.setDevice(device);

        PhotoUtils.chechPermission1(this);
        PhotoUtils.chechPermission2(this);
        PhotoUtils.chechPermission3(this);
        PhotoUtils.chechPermission4(this);

        new MisArchivosConn(context,activity).execute();

        btnRefreshFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MisArchivosConn(context,activity).execute();
            }
        });

        inizializamosSubirArchivo();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // ******************************************************************************************
    // METODOS DE SUBIR ARCHIVOS INICIAN AQUI
    // ******************************************************************************************



    private void inizializamosSubirArchivo() {
        imgImagen = (ImageView) findViewById(R.id.imgArchivo);
        vidVideo = (VideoView) findViewById(R.id.vidArchivo);
        filenameArchivoSel = (TextView) findViewById(R.id.nombreArchivoSeleccionado);
        btnAddFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bsD = new BottomSheetDialog();
                bsD.show(getSupportFragmentManager(), "seleccionarArchivo");
            }
        });
    }

    private void uploadFileDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("En este momento se dispone a enviar este archivo: "+fileName+"\n\nEsta seguro de hacerlo?")
                .setTitle(R.string.app_name);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                uploadFile();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void uploadFile(){
        if (fileName != "") {
            SendArchivoWithVolley sdv = new SendArchivoWithVolley(activity, context);
            switch (Singleton.getIdTarget()) {
                case 1:
                case 2:
                    sdv.bitmap = ((BitmapDrawable) Singleton.getImagen().getDrawable()).getBitmap();
                    sdv.uploadImage(sdv.bitmap, null, null);
                    break;
                case 3:
                case 4:
                    Uri VideoURI = Uri.parse(String.valueOf(Singleton.getUriData()));
                    byte[] VideoBytes = UploadHelper.getFileDataFromDrawable(activity, VideoURI);
                    sdv.uploadImage(null, VideoBytes, null);
                    break;
                case 5:
                    Uri AudioURI = Uri.parse(String.valueOf(Singleton.getUriData()));
                    byte[] AudioBytes = UploadHelper.getFileDataFromDrawable(activity, AudioURI);
                    sdv.uploadImage(null, AudioBytes, null);
                    break;
                case 6:
                    Uri Archivo = Uri.parse(String.valueOf(Singleton.getUriData()));
                    byte[] ArchivoBytes = UploadHelper.getFileDataFromDrawable(activity, Archivo);
                    String extension = Utilidades.getfileExtensionFromUri(activity, Archivo);
                    sdv.uploadImage(null, ArchivoBytes, extension);
                    break;
                default:
                    sdv.uploadImage(null, null, null);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Seleccione un archivo válido.")
                    .setTitle(R.string.app_name);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onButtonBSDClicked(int Tipo) {
        fileName = "";
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
//        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_VIDEO_REQUEST_CODE);
//        }

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
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

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
        ImageView img;
        VideoView vid;
        Bitmap bmFrame = null;

        if (resultCode == RESULT_OK){
            // Se muestta la imagen
            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    Log.e("DATA: ","Entro");
                    img = imgImagen;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(photo);
                    Singleton.setImagen(img);
                    Singleton.setUriData(PhotoUtils.getImageUri(context,photo));
                    setFileName();
                    uploadFileDialog();
                    break;
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    Singleton.setUriData(data.getData());
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    setFileName();

                    cursor.close();
                    img = imgImagen;
                    Bitmap bitmap = null;
                    try {
                        bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Singleton.setPathFile(data.getData().getPath());
                    Log.e("PATH FILE IN : ",data.getData().getPath()  );
                    img.setImageBitmap(bitmap);
                    Singleton.setImagen(img);
                    imgImagen = Singleton.getImagen();
                    Singleton.setPathFile(imgDecodableString);

                    uploadFileDialog();
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

                    uploadFileDialog();
                    break;
                case GALLERY_VIDEO_REQUEST_CODE:

                    Uri uri_video2 = data.getData();
                    vidVideo.setVideoURI(uri_video2);

                    vidVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                        }
                    });
                    Log.e("Paso","Aquí");
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
                            uploadFileDialog();

                        }
                    });

                    vidVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            vidVideo.stopPlayback();
                            vidVideo.setVideoURI(null);
                            return false;
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        vidVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                                return false;
                            }
                        });
                    }

                    break;

                case AUDIO_REQUEST_CODE:

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
                            uploadFileDialog();

                        }
                    });

                    vidVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            vidVideo.stopPlayback();
                            vidVideo.setVideoURI(null);
                            return false;
                        }
                    });

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        vidVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                                return false;
                            }
                        });
                    }

                    break;

                case DOC_REQUEST_CODE:
                    Singleton.setUriData(data.getData());
                    imgImagen.setImageResource(R.drawable.ic_file_black_24dp);
                    setFileName();
                    uploadFileDialog();
                    break;
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("PERMISO: ", String.valueOf(requestCode));
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


    @Override
    public void goBackActivity() {

    }

    @Override
    public void preparaMisArchivosAdapter(ArrayList<Opciones> items) {
        MisArchivosAdapter adapter = new MisArchivosAdapter(activity,context,items);
        rvMisArchivos.setAdapter(adapter);
    }

    private void setFileName(){
        Uri Archivo = Uri.parse(String.valueOf(Singleton.getUriData()));
        fileName = Utilidades.getFilenameFromUri(activity,Archivo);
    }


// *******************************************************************************************
// *******************************************************************************************
// *******************************************************************************************







}
