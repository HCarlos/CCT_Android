package com.tabascoweb.cerocorrupciontabasco.Classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devch on 1/08/16.
 */
public class PhotoUtils {
    private static Context mContext;
    private static Activity activity;
    private BitmapFactory.Options generalOptions;
    private static String cameraFilePath;

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




    public PhotoUtils(Context context, Activity _activity) {
        mContext = context;
        activity = _activity;
    }

    public static File createTemporaryFile(String part, String ext,
                                           Context myContext) throws IOException {
        cameraFilePath = myContext.getExternalCacheDir().getAbsolutePath();
        File tempDir = new File(cameraFilePath);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        File image = File.createTempFile(part, ext, tempDir);
//        cameraFilePath = "file://" + image.getAbsolutePath();
        cameraFilePath = image.getAbsolutePath();
        Log.e("PathFileName: ",cameraFilePath);
        return image;
    }

    public static File createImageFile(String part, String ext,
                                       Context myContext) throws IOException {
        cameraFilePath = "content";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "devch_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(part, ext, storageDir);
        cameraFilePath = "content:/" + image.getAbsolutePath();
        // cameraFilePath = image.getAbsolutePath();
        Log.e("PathFileName: ",cameraFilePath);
        return image;
    }

    public static File createVideoFile(String part, String ext,
                                       Context myContext) throws IOException {
        cameraFilePath = "content";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "devch_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(part, ext, storageDir);
        cameraFilePath = image.getAbsolutePath();
        return image;
    }



    public static String getCameraFilePath(){
        return cameraFilePath;
    }

    public Bitmap getImage(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.generalOptions = options;
        return scaleImage(options, uri, 300);
    }

    public static int nearest2pow(int value) {
        return value == 0 ? 0
                : (32 - Integer.numberOfLeadingZeros(value - 1)) / 2;
    }

    public Bitmap scaleImage(BitmapFactory.Options options, Uri uri,
                             int targetWidth) {
        if (options == null)
            options = generalOptions;
        Bitmap bitmap = null;
        double ratioWidth = ((float) targetWidth) / (float) options.outWidth;
        double ratioHeight = ((float) targetWidth) / (float) options.outHeight;
        double ratio = Math.min(ratioWidth, ratioHeight);
        int dstWidth = (int) Math.round(ratio * options.outWidth);
        int dstHeight = (int) Math.round(ratio * options.outHeight);
        ratio = Math.floor(1.0 / ratio);
        int sample = nearest2pow((int) ratio);

        options.inJustDecodeBounds = false;
        if (sample <= 0) {
            sample = 1;
        }
        options.inSampleSize = (int) sample;
        options.inPurgeable = true;
        try {
            InputStream is;
            is = mContext.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (sample > 1)
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight,
                        true);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static Boolean chechPermission1(Activity _activity) {
        Boolean retorno = true;
        Activity activity = _activity;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                retorno = false;
                Toast.makeText(activity, "No ha autorizado utilizar la cámara ó guardar imaganes en su teléfono.", Toast.LENGTH_SHORT).show();
                String[] permission = {Manifest.permission.CAMERA};
                activity.requestPermissions(permission, PERMISSION_CODE);
            }
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                retorno = false;
                Toast.makeText(activity, "No ha autorizado utilizar la cámara ó guardar imaganes en su teléfono.", Toast.LENGTH_SHORT).show();
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                activity.requestPermissions(permission, PERMISSION_CODE_2);
            }
        }
        return  retorno;
    }

    public static Boolean chechPermission2(Activity _activity) {
        Boolean retorno = true;
        Activity activity = _activity;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                retorno = false;
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                activity.requestPermissions(permission, PERMISSION_CODE_GALLERY);
            }
        }
        return  retorno;
    }




    public static Boolean chechPermission3(Activity _activity) {
        Boolean retorno = true;
        Activity activity = _activity;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ||
                    activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                retorno = false;
                String[] permission = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                activity.requestPermissions(permission, PERMISSION_CODE_AUDIO);
            }
        }
        return  retorno;
    }





    public static Boolean chechPermission4(Activity _activity) {
        Boolean retorno = true;
        Activity activity = _activity;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (activity.checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
                    activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                retorno = false;
                Toast.makeText(activity, "No ha autorizado utilizar la cámara ó guardar imaganes en su teléfono.", Toast.LENGTH_SHORT).show();
                String[] permission = {Manifest.permission.INTERNET,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                activity.requestPermissions(permission, PERMISSION_CODE_DOC);
            }
        }
        // OJO: en el 'activity' de donde viene, debe estar sobre escrito el método ' onRequestPermissionsResult'

        return retorno;
    }





}