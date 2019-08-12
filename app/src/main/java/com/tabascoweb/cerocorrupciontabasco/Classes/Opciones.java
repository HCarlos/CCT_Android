package com.tabascoweb.cerocorrupciontabasco.Classes;

import android.media.Image;
import android.provider.MediaStore;

public class Opciones {

    private Integer Key;
    private String Value;
    private String VString1;
    private String VString2;
    private String VString3;
    private Integer VInteger1;
    private Integer VInteger2;
    private Integer VInteger3;
    private Image Imagen;
    private MediaStore.Video Video;

    public Opciones(Integer key, String value) {
        Key = key;
        Value = value;
    }

    public Opciones(Integer key, String value, String VString1) {
        Key = key;
        Value = value;
        this.VString1 = VString1;
    }

    public Opciones(Integer key, String value, String VString1, Integer VInteger1) {
        Key = key;
        Value = value;
        this.VString1 = VString1;
        this.VInteger1 = VInteger1;
    }

    public Integer getKey() {
        return Key;
    }

    public void setKey(Integer key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getVString1() {
        return VString1;
    }

    public void setVString1(String VString1) {
        this.VString1 = VString1;
    }

    public String getVString2() {
        return VString2;
    }

    public void setVString2(String VString2) {
        this.VString2 = VString2;
    }

    public String getVString3() {
        return VString3;
    }

    public void setVString3(String VString3) {
        this.VString3 = VString3;
    }

    public Image getImagen() {
        return Imagen;
    }

    public void setImagen(Image imagen) {
        Imagen = imagen;
    }

    public MediaStore.Video getVideo() {
        return Video;
    }

    public void setVideo(MediaStore.Video video) {
        Video = video;
    }

    public Integer getVInteger1() {
        return VInteger1;
    }

    public void setVInteger1(Integer VInteger1) {
        this.VInteger1 = VInteger1;
    }

    public Integer getVInteger2() {
        return VInteger2;
    }

    public void setVInteger2(Integer VInteger2) {
        this.VInteger2 = VInteger2;
    }

    public Integer getVInteger3() {
        return VInteger3;
    }

    public void setVInteger3(Integer VInteger3) {
        this.VInteger3 = VInteger3;
    }
}
