package dao;

import modelo.Puntaje;

public interface PuntajeDAO {
    void guardar(Puntaje puntaje);

    void borrar(Puntaje puntaje);
}
