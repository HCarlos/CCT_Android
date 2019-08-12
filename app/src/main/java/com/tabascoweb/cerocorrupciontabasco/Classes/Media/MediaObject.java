package com.tabascoweb.cerocorrupciontabasco.Classes.Media;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.tabascoweb.cerocorrupciontabasco.BuildConfig;
import com.tabascoweb.cerocorrupciontabasco.Classes.PhotoUtils;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;

import java.io.IOException;

public class MediaObject extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000,
            IMAGE_CAPTURE_CODE = 1001,
            PERMISSION_CODE_GALLERY = 1002,
            GALLERY_REQUEST_CODE=1003,
            CAMERA_REQUEST_CODE=1004,
            PERMISSION_CODE_AUDIO = 1005,
            VIDEO_REQUEST_CODE=1006;

    private static Activity activity;
    private static Context context;
    private static Activity act2;
    private PhotoUtils pu;
    private Uri mImageUri;
    private ImageView imgArchivo;

    private MediaObjectListener mListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MediaObject(Activity activity, Context context, ImageView _imgImagen) {
        this.activity = activity;
        this.context = context;
        this.imgArchivo = _imgImagen;
        pu = new PhotoUtils(context,activity);
        act2 = this;

    }


    public void PrepararGalería() {
        if (pu.chechPermission1(activity)){
            seleccionarImagen();
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/jpg","image/png"};
        intent.putExtra(intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    public void PrepararCámara() {
        if (pu.chechPermission1(activity)){
            tomarFoto();
        }
    }


    public void tomarFoto(){
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
            values.put(MediaStore.Images.Media.DESCRIPTION,"Desde la Cámara");
            mImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".classes.GenericFileProvider", pu.createImageFile("devch_", ".jpg", activity.getBaseContext())));
            intent.setFlags(0);
            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    tomarFoto();
                }else{
                    Toast.makeText(this, "No tiene permiso para usar la cámara", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case PERMISSION_CODE_GALLERY:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    seleccionarImagen();
                }else{
                    Toast.makeText(this, "No tiene permiso para ver las imágenes", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case PERMISSION_CODE_AUDIO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    seleccionarImagen();
                }else{
                    Toast.makeText(this, "No tiene permiso para ver las imágenes", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("Llego la imagen => none", " none");
        if (resultCode == RESULT_OK){
            // Se muestta la imagen
            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    //imgImagen.setImageURI(mImageUri);
                    imgArchivo.setImageURI(Uri.parse(pu.getCameraFilePath()));
                    Log.e("Llego la imagen ", String.valueOf(imgArchivo));
                    Singleton.setImagen(imgArchivo);
                    mListener.updateImages(imgArchivo);
                    break;
                case GALLERY_REQUEST_CODE:

//                    mImageUri = data.getData();
//                    imgImagen.setImageURI(mImageUri);

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    imgArchivo.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    Singleton.setImagen(imgArchivo);
                    mListener.updateImages(imgArchivo);
                    break;
            }
        }
    }


    public interface MediaObjectListener{
        void updateImages(ImageView imgV);
    }



}
