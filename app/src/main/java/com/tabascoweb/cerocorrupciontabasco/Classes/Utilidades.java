package com.tabascoweb.cerocorrupciontabasco.Classes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.provider.OpenableColumns;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utilidades {

    private static final String TAG = "RESPUESTA";
    private ProgressDialog pDialog;
    private static Activity activity;
    private static LocationManager lm;
    private static int tipo;
    private static double current_lattitude = 0.0;
    private static double current_longitude = 0.0;
    private static double current_altitud = 0.0;
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    public Utilidades(ProgressDialog pDialog) {
        this.pDialog = pDialog;
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            this.pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            this.pDialog.dismiss();
    }

    public static void GetGPS(Activity _activity, LocationManager _lm, int _tipo) throws IOException {

        activity = _activity;
        lm = _lm;
        tipo = _tipo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        } else {
            if (tipo == 0) {
                getLatLon();
            }
        }
    }

    public static void GetSMSData(Activity _activity, LocationManager _lm, int _tipo) {

        activity = _activity;
        lm = _lm;
        tipo = _tipo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS},
                    123);
        } else {
            if (tipo == 1) {
                getSMSData();
            }
        }
    }

    public static void GetCamera(Activity _activity, int _tipo) {

        activity = _activity;
        Context context = activity.getApplicationContext();
        tipo = _tipo;
        Singleton.setIsCameraPresent(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    123);
        } else {
            if (tipo == 0) {
                Singleton.setIsCameraPresent(true);
            }
        }
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) throws IOException {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                if (tipo == 0) {
                    getLatLon();
                }
                if (tipo == 1) {
                    getSMSData();
                }
                if (tipo == 2) {
                    // setFoto();
                    Singleton.setIsCameraPresent(true);
                }

            } else {
                Toast.makeText(activity, "No tiene los permisos necesarios, para utilizar esta App.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void getLatLon() throws IOException {
        GPSTracker gps = new GPSTracker(activity);
        int status = 0;
        if (!gps.canGetLocation()) {
            return;
        }
        status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);

        if (status == ConnectionResult.SUCCESS) {
            current_lattitude = gps.getLatitude();
            current_longitude = gps.getLongitude();
            if (gps.getLocation() != null){
                current_altitud = gps.getLocation().getAltitude();
            }

            Singleton.setLatitude(current_lattitude);
            Singleton.setLongitude(current_longitude);
            Singleton.setAltitud(current_altitud);

            Singleton.setIsGPS(true);

            Log.d("LAT LON ALT", "" + current_lattitude + " | "
                    + current_longitude + " | "
                    + current_altitud);

            if (current_lattitude == 0.0 && current_longitude == 0.0) {
                current_lattitude = 22.22;
                current_longitude = 22.22;
                current_altitud = 0.0;

                Singleton.setLatitude(current_lattitude);
                Singleton.setLongitude(current_longitude);
                Singleton.setAltitud(current_altitud);

                Singleton.setIsGPS(false);

            }

        } else {
            current_lattitude = 22.22;
            current_longitude = 22.22;

            Singleton.setLatitude(current_lattitude);
            Singleton.setLongitude(current_longitude);

            Singleton.setIsGPS(false);

        }


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        addresses = geocoder.getFromLocation(current_lattitude, current_longitude, 1);

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        Singleton.setDireccionGoogle(address + " " + city + " " + state + " " + country + " " + postalCode);

    }

    @SuppressLint("MissingPermission")
    public static void getSMSData() {
//        TelephonyManager tManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyManager tManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        Singleton.setNombre(tManager.getSimOperatorName());
        Singleton.setCelular(tManager.getLine1Number());
    }

    public static void setFoto(Activity activity) {
        try {
            Log.e("ENTRO", "SI");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            int code = TAKE_PICTURE;
            activity.startActivityForResult(intent, code);
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNetworkConnected(Activity _activity) {
        activity = _activity;
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) return false;
        else return true;
    }


    public static String getFilenameFromUri(Activity activity, Uri uri) {

        Log.e("URI: ",uri.getScheme());

        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public static String getfileExtensionFromUri(Activity activity, Uri uri) {
        String extension;
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }





}