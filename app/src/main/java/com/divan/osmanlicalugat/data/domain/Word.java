package com.divan.osmanlicalugat.data.domain;

import androidx.annotation.NonNull;

public class Word extends BaseModel {

    public static final String TABLE_NAME = "mytable";
    public static final String keyIndex = "indeks";
    public static final String keyKelime = "kelime";
    public static final String keyKirpilmis = "kirpilmis";
    public static final String keyAnlam = "anlam";


    private String id;
    private int indeks;
    public String kelime;
    private String kirpilmis;
    private String anlam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKelime() {
        return kelime;
    }

    public void setKelime(String kelime) {
        this.kelime = kelime;
    }

    public String getKirpilmis() {
        return kirpilmis;
    }

    public void setKirpilmis(String kirpilmis) {
        this.kirpilmis = kirpilmis;
    }

    public String getAnlam() {
        return anlam;
    }

    public void setAnlam(String anlam) {
        this.anlam = anlam;
    }


    public int getIndeks() {
        return indeks;
    }


    public void setIndeks(int indeks) {
        this.indeks = indeks;
    }


    @Override
    public String getIdString() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return kelime;
    }
}
