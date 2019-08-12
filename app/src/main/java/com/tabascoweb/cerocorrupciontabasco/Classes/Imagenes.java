package com.tabascoweb.cerocorrupciontabasco.Classes;

import java.net.URI;

/**
 * Created by devch on 3/08/16.
 */
public class Imagenes {

    private  int ID;
    private  String imagen;
    private  String imagen_s;
    private  String imagen_mini;
    private  int so_mobile;
    private  double latitud;
    private  double longitud;
    private  double altitud;
    private  String creado_el;
    private  String cfecha;
    private URI uri;

    public Imagenes() {
    }

    public Imagenes(int ID) {
        this.ID = ID;
    }

    public Imagenes(int ID, String imagen) {
        this.ID = ID;
        this.imagen = imagen;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagen_s() {
        return imagen_s;
    }

    public void setImagen_s(String imagen_s) {
        this.imagen_s = imagen_s;
    }

    public String getImagen_mini() {
        return imagen_mini;
    }

    public void setImagen_mini(String imagen_mini) {
        this.imagen_mini = imagen_mini;
    }

    public int getSo_mobile() {
        return so_mobile;
    }

    public void setSo_mobile(int so_mobile) {
        this.so_mobile = so_mobile;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public String getCreado_el() {
        return creado_el;
    }

    public void setCreado_el(String creado_el) {
        this.creado_el = creado_el;
    }

    public String getCfecha() {
        return cfecha;
    }

    public void setCfecha(String cfecha) {
        this.cfecha = cfecha;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
