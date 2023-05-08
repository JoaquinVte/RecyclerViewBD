package com.example.myrecyclerviewexample.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Model {

    private static Model model;
    private MysqlDB mysqlDB;
    private List<Usuario> usuarios;
    private List<Oficio> oficios;

    private Model() {
        usuarios = new ArrayList<>();
        oficios = new ArrayList<>();
        mysqlDB = new MysqlDB();
    }


    public static Model getInstance() {
        if (model == null)
            model = new Model();

        return model;
    }

    public void loadDataFromDB(){
        getUsuarios();
        getOficios();
    }

    public List<Usuario> getUsuarios() {
        if (usuarios.isEmpty())
            usuarios = mysqlDB.getAllUsers();

        return usuarios;
    }

    public List<Oficio> getOficios() {
        if (oficios.isEmpty())
            oficios = mysqlDB.getAllOficios();

        return oficios;
    }

    public boolean updateUsuario(Usuario u){

        Usuario aux = mysqlDB.updateUsuario(u);

        if(aux!=null){
            int pos = usuarios.indexOf(u);
            Usuario usuario = usuarios.get(pos);
            usuario.setNombre(aux.getNombre());
            usuario.setApellidos(aux.getApellidos());
            usuario.setOficio(aux.getOficio());
            return true;
        }
        return false;
    }
    public Usuario insertUsuario(Usuario u){
        Usuario usuario = mysqlDB.insertUsuario(u);
        if (usuario!=null) {
            usuarios.add(usuario);
            usuarios.sort(Comparator.comparingInt(Usuario::getIdUsuario));
        }
        return usuario;
    }

    public Usuario removeUser(Usuario u) {
        Usuario usuario = mysqlDB.removeUser(u);
        if(usuario!=null)
            usuarios.remove(usuario);
        return usuario;
    }
}
