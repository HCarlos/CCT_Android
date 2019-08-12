package com.tabascoweb.cerocorrupciontabasco.Classes;

public class AppConfig {

    public static String TEXT_HTML = "text/html";
    public static String UTF_8 = "UTF-8";

    public static String URL_HOME = "https://uipeapp01.uipe.tabascoweb.com/";

    public static String URL_TIPO_GOBIERNO = URL_HOME + "getTipoGobierno/";

    public static String URL_MEDIA_DATA = URL_HOME + "up_control_images/";

    public static String URL_MUNICIPIOS = URL_HOME + "getMunicipios/1/";

    public static String URL_GUARDAR_DENUNCIA = URL_HOME + "setDenunciaAndroid/";

    public static String URL_AGREGAR_ARCHIVO = URL_HOME + "setAddArchivo/";

    public static String URL_QUITAR_ARCHIVO = URL_HOME + "setRemoveArchivo/";

    public static String URL_MIS_DENUNCIAS = URL_HOME + "getMisDenunciasAndroid/";

    public static String URL_MI_DENUNCIA = URL_HOME + "getDenuncia/";

    public static String URL_ARCHIVOS_DENUNCIA = URL_HOME + "getDenunciaArchivos/";

    public static String URL_DEPENDENCIAS = URL_HOME + "getDependencias/";

    public static String URL_AREAS = URL_HOME + "getAreas/";

    public static String URL_ACERCADE = URL_HOME + "acercade/";

    public static String URL_AVISODEPRIVACIDAD = URL_HOME + "avisodeprivacidad/";

    public static String URL_MEDIA = URL_HOME + "up_control_images/";

    public static String getURLDependencias(){
        return URL_DEPENDENCIAS + Singleton.getIdTipoGobierno() + "/";
    };

    public static String getURLAreas(){
        return URL_AREAS + Singleton.getIdDependencia() + "/";
    };

    public static String getURLMisDenuncias(){
        return URL_MIS_DENUNCIAS + Singleton.getUUID() + "/";
    };

    public static String getURLMiDenuncia(){
        return URL_MI_DENUNCIA + Singleton.getIdDenuncia() + "/";
    };

    public static String getURLMisArchivos(){
        return URL_ARCHIVOS_DENUNCIA + Singleton.getIdDenuncia() + "/";
    };

    public static String getURLImagenArchivo(){
        return URL_MEDIA + Singleton.getArchivo();
    };


}
