package dao;

import java.sql.SQLException;

import modelo.Puntaje;

public interface PuntajeDAO {
    void guardar(Puntaje puntaje) throws SQLException;

    void borrar(Puntaje puntaje) throws SQLException;
}
