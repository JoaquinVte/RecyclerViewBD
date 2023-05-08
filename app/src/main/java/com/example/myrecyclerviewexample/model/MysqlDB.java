package com.example.myrecyclerviewexample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDB {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection("jdbc:mysql://192.168.1.53:3306/java","jalonso","1111");
    }

    public List<Usuario> getAllUsers(){
        List<Usuario> usuarios = new ArrayList<>();

        try(Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Usuario")
        ){
            int id,oficio;
            String nombre,apellidos;
            while(rs.next()){
                id=rs.getInt("idUsuario");
                nombre = rs.getString("nombre");
                apellidos= rs.getString("apellidos");
                oficio = rs.getInt("Oficio_idOficio");
                usuarios.add(new Usuario(id,nombre,apellidos,oficio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public Usuario updateUsuario(Usuario u){
        String sql = "UPDATE Usuario " +
                "SET nombre='" + u.getNombre() +"', apellidos='"+u.getApellidos()+"', Oficio_idOficio="+u.getOficio() +
                " WHERE idUsuario="+u.getIdUsuario();
        try(Connection c = getConnection();
        Statement stmt = c.createStatement();
        ) {

            if(stmt.executeUpdate(sql)==1)
                return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Usuario insertUsuario(Usuario u){
        return (u.getIdUsuario()==null)?insertUsuarioWithoutId(u):insertUsuarioWithId(u);
    }

    private Usuario insertUsuarioWithId(Usuario u){
        String sql = "INSERT INTO Usuario (idUsuario,nombre,apellidos,Oficio_idOficio) VALUES (?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            int pos=0;
            statement.setInt(++pos,u.getIdUsuario());
            statement.setString(++pos,u.getNombre());
            statement.setString(++pos,u.getApellidos());
            statement.setInt(++pos,u.getOficio());

            if( statement.executeUpdate() == 0 )
                throw new SQLException("Ninguna fila afectada a la hora de crear el usuario.");

            return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Usuario insertUsuarioWithoutId(Usuario u){
        String sql = "INSERT INTO Usuario (nombre,apellidos,Oficio_idOficio) VALUES (?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1,u.getNombre());
            statement.setString(2,u.getApellidos());
            statement.setInt(3,u.getOficio());

            if( statement.executeUpdate() == 0 )
                throw new SQLException("Ninguna fila afectada a la hora de crear el usuario.");

            try(ResultSet rs = statement.getGeneratedKeys()){

                if(rs.next())
                    u.setIdUsuario(rs.getInt(1));
                else
                    throw new SQLException("No obtenida el id asignado");
            }

            return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario removeUser(Usuario u) {
        String sql = "DELETE FROM Usuario " +
                " WHERE idUsuario="+u.getIdUsuario();
        try(Connection c = getConnection();
            Statement stmt = c.createStatement();
        ) {

            if(stmt.executeUpdate(sql)==1)
                return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public List<Oficio> getAllOficios() {
        List<Oficio> oficios = new ArrayList<>();

        try(Connection c = getConnection();
            Statement statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Oficio")
        ){

            int idOficio;
            String descripcion;

            while (rs.next()){
                idOficio = rs.getInt("idOficio");
                descripcion = rs.getString("descripcion");
                oficios.add(new Oficio(idOficio,descripcion));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oficios;
    }


}
