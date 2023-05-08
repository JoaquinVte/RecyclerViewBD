package com.example.myrecyclerviewexample.model;

import androidx.annotation.Nullable;

import com.example.myrecyclerviewexample.R;

public class Oficio {
    private int idOficio;
    private String descripcion;

    public Oficio(int idOficio, String descripcion) {
        this.idOficio = idOficio;
        this.descripcion = descripcion;
    }

    public int getIdOficio() {
        return idOficio;
    }

    public void setIdOficio(int idOficio) {
        this.idOficio = idOficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImageResource(){
        return getImageResource(idOficio);
    }
    public static int getImageResource(int idOficio) {
        switch (idOficio) {
            case 1:
                return R.mipmap.ic_1_foreground;
            case 2:
                return R.mipmap.ic_2_foreground;
            case 3:
                return R.mipmap.ic_3_foreground;
            case 4:
                return R.mipmap.ic_4_foreground;
            case 5:
                return R.mipmap.ic_5_foreground;
            case 6:
                return R.mipmap.ic_6_foreground;
            case 7:
                return R.mipmap.ic_7_foreground;
            case 8:
                return R.mipmap.ic_8_foreground;
            case 9:
                return R.mipmap.ic_9_foreground;
            case 10:
                return R.mipmap.ic_10_foreground;
            case 11:
                return R.mipmap.ic_11_foreground;
            default:
                return R.mipmap.ic_12_foreground;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Oficio) {
            Oficio o = (Oficio) obj;
            return o.idOficio == idOficio;
        }
        return false;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
