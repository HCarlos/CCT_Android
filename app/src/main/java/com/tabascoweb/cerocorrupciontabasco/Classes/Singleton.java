package com.tabascoweb.cerocorrupciontabasco.Classes;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.ArrayList;


public class Singleton {

    private static int IdTarget;
    private static int IdDenuncia = 0;
    private static int IdTipoDenuncia = 0;
    private static int IdTipoGobierno = 0;
    private static int IdMunicipio = 0;
    private static int IdDependencia = 0;
    private static int IdArea = 0;
    private static int IdArchivo = 0;
    private static String Archivo = "";
    private static String Apellido_Paterno = "";
    private static String Apellido_Materno = "";
    private static String Nombre = "";
    private static String Correo_Electronico = "";
    private static String Celular = "";
    private static String Fecha = "";
    private static String Hora = "";
    private static String Lugar_Denuncia = "";
    private static String Denuncia = "";
    private static String UUID = "";
    private static String Device = "";

    private static int IdCombo = 0;
    private static String Method = "POST";
    private static String urlPasoConsulta = "";
    private static ArrayList<Opciones> arrayOpciones = null;

    private static String pathPList;
    private static boolean IsDelete;

    private static int ID;
    private static double Longitude;
    private static double Latitude;
    private static double Altitud;

    private static ArrayList<Imagenes> arrImagenes;

    private static boolean isCameraPresent;
    private static boolean isGPS;

    private static String DireccionGoogle;

    private static ImageView Imagen;
    private static VideoView Video;
    private static MediaStore.Audio Audio;

    private static String pathFile;
    private static Uri UriData;
    private static Intent IntentData;

    private static boolean isInit = true;



    private static Singleton ourInstance = new Singleton();

    static Singleton getInstance() {
        return ourInstance;
    }

    public Singleton() { }

    public static String getPathPList() {

        return pathPList;
    }
    public static void setPathPList(String pathPList) {

        Singleton.pathPList = pathPList;
    }

    public static boolean isIsDelete() {
        return IsDelete;
    }
    public static void setIsDelete(boolean isDelete) {

        IsDelete = isDelete;
    }

    public static int getIdTarget() {
        return IdTarget;
    }

    public static void setIdTarget(int idTarget) {
        IdTarget = idTarget;
    }

    public static int getIdDenuncia() {
        return IdDenuncia;
    }

    public static void setIdDenuncia(int idDenuncia) {
        IdDenuncia = idDenuncia;
    }

    public static int getIdTipoDenuncia() {
        return IdTipoDenuncia;
    }

    public static void setIdTipoDenuncia(int idTipoDenuncia) {
        IdTipoDenuncia = idTipoDenuncia;
    }

    public static int getIdTipoGobierno() {
        return IdTipoGobierno;
    }

    public static void setIdTipoGobierno(int idTipoGobierno) {
        IdTipoGobierno = idTipoGobierno;
    }

    public static int getIdMunicipio() {
        return IdMunicipio;
    }

    public static void setIdMunicipio(int idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public static int getIdDependencia() {
        return IdDependencia;
    }

    public static void setIdDependencia(int idDependencia) {
        IdDependencia = idDependencia;
    }

    public static int getIdArea() {
        return IdArea;
    }

    public static void setIdArea(int idArea) {
        IdArea = idArea;
    }

    public static int getIdArchivo() {
        return IdArchivo;
    }

    public static void setIdArchivo(int idArchivo) {
        IdArchivo = idArchivo;
    }

    public static String getArchivo() {
        return Archivo;
    }

    public static void setArchivo(String archivo) {
        Archivo = archivo;
    }

    public static String getApellido_Paterno() {
        return Apellido_Paterno;
    }

    public static void setApellido_Paterno(String apellido_Paterno) {
        Apellido_Paterno = apellido_Paterno;
    }

    public static String getApellido_Materno() {
        return Apellido_Materno;
    }

    public static void setApellido_Materno(String apellido_Materno) {
        Apellido_Materno = apellido_Materno;
    }

    public static String getNombre() {
        return Nombre;
    }

    public static void setNombre(String nombre) {
        Nombre = nombre;
    }

    public static String getCorreo_Electronico() {
        return Correo_Electronico;
    }

    public static void setCorreo_Electronico(String correo_Electronico) {
        Correo_Electronico = correo_Electronico;
    }

    public static String getCelular() {
        return Celular;
    }

    public static void setCelular(String celular) {
        Celular = celular;
    }

    public static String getFecha() {
        return Fecha;
    }

    public static void setFecha(String fecha) {
        Fecha = fecha;
    }

    public static String getHora() {
        return Hora;
    }

    public static void setHora(String hora) {
        Hora = hora;
    }

    public static String getLugar_Denuncia() {
        return Lugar_Denuncia;
    }

    public static void setLugar_Denuncia(String lugar_Denuncia) {
        Lugar_Denuncia = lugar_Denuncia;
    }

    public static String getDenuncia() {
        return Denuncia;
    }

    public static void setDenuncia(String denuncia) {
        Denuncia = denuncia;
    }

    public static String getUUID() {
        return UUID;
    }

    public static void setUUID(String UUID) {
        Singleton.UUID = UUID;
    }

    public static String getDevice() {
        return Device;
    }

    public static void setDevice(String device) {
        Device = device;
    }

    public static String getMethod() {
        return Method;
    }

    public static void setMethod(String method) {
        Method = method;
    }

    public static int getIdCombo() {
        return IdCombo;
    }

    public static void setIdCombo(int idCombo) {
        IdCombo = idCombo;
    }

    public static String getUrlPasoConsulta() {
        return urlPasoConsulta;
    }

    public static void setUrlPasoConsulta(String urlPasoConsulta) {
        Singleton.urlPasoConsulta = urlPasoConsulta;
    }

    public static ArrayList<Opciones> getArrayOpciones() {
        return arrayOpciones;
    }

    public static void setArrayOpciones(ArrayList<Opciones> arrayOpciones) {
        Singleton.arrayOpciones = arrayOpciones;
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        Singleton.ID = ID;
    }

    public static double getLongitude() {
        return Longitude;
    }

    public static void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public static double getLatitude() {
        return Latitude;
    }

    public static void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public static double getAltitud() {
        return Altitud;
    }

    public static void setAltitud(double altitud) {
        Altitud = altitud;
    }

    public static ArrayList<Imagenes> getArrImagenes() {
        return arrImagenes;
    }

    public static void setArrImagenes(ArrayList<Imagenes> arrImagenes) {
        Singleton.arrImagenes = arrImagenes;
    }

    public static boolean isIsCameraPresent() {
        return isCameraPresent;
    }

    public static void setIsCameraPresent(boolean isCameraPresent) {
        Singleton.isCameraPresent = isCameraPresent;
    }

    public static boolean isIsGPS() {
        return isGPS;
    }

    public static void setIsGPS(boolean isGPS) {
        Singleton.isGPS = isGPS;
    }

    public static String getDireccionGoogle() {
        return DireccionGoogle;
    }

    public static void setDireccionGoogle(String direccionGoogle) {
        DireccionGoogle = direccionGoogle;
    }

    public static ImageView getImagen() {
        return Imagen;
    }

    public static void setImagen(ImageView imagen) {
        Imagen = imagen;
    }

    public static VideoView getVideo() {
        return Video;
    }

    public static void setVideo(VideoView video) {
        Video = video;
    }

    public static MediaStore.Audio getAudio() {
        return Audio;
    }

    public static void setAudio(MediaStore.Audio audio) {
        Audio = audio;
    }


    public static String getPathFile() {
        return pathFile;
    }

    public static void setPathFile(String pathFile) {
        Singleton.pathFile = pathFile;
    }


    public static Uri getUriData() {
        return UriData;
    }

    public static void setUriData(Uri uriData) {
        UriData = uriData;
    }

    public static Intent getIntentData() {
        return IntentData;
    }

    public static void setIntentData(Intent intentData) {
        IntentData = intentData;
    }


    public static boolean IsInit() {
        return isInit;
    }

    public static void setInit(boolean isInit) {
        Singleton.isInit = isInit;
    }

    public static void reset() {
//        setRsHijos(null);
//        setRsElementos(null);
        ourInstance = new Singleton();
    }


}

