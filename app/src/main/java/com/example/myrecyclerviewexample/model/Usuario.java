package com.example.myrecyclerviewexample.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private int oficio;

    public Usuario(int idUsuario, String nombre, String apellidos, int oficio) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.oficio = oficio;
    }

    public Usuario(String nombre, String apellidos, int oficio) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.oficio = oficio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getOficio() {
        return oficio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setOficio(int oficio) {
        this.oficio = oficio;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Usuario){
            Usuario u = (Usuario) obj;
            return idUsuario==u.idUsuario;
        }
        return false;
    }
}
