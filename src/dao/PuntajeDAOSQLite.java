package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import modelo.Puntaje;

public final class PuntajeDAOSQLite implements PuntajeDAO {
    private static final String CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS puntajes ("
            + "fecha INTEGER NOT NULL, "
            + "puntos INTEGER NOT NULL"
            + ")";
    private static final String INSERTAR_PUNTAJE =
        "INSERT INTO puntajes (fecha, puntos) VALUES (?, ?)";
    private static final String BORRAR_PUNTAJE =
        "DELETE FROM puntajes WHERE fecha = ? AND puntos = ?";

    private final Connection conexion;

    public PuntajeDAOSQLite(Connection conexion) throws SQLException {
        this.conexion = Objects.requireNonNull(conexion, "La conexión no puede ser nula.");

        try (Statement sentencia = this.conexion.createStatement()) {
            sentencia.executeUpdate(CREAR_TABLA);
        }
    }

    @Override
    public void guardar(Puntaje puntaje) throws SQLException {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");

        try (PreparedStatement sentencia = conexion.prepareStatement(INSERTAR_PUNTAJE)) {
            sentencia.setInt(1, puntaje.getFecha());
            sentencia.setInt(2, puntaje.getPuntos());
            sentencia.executeUpdate();
        }
    }

    @Override
    public void borrar(Puntaje puntaje) throws SQLException {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");

        try (PreparedStatement sentencia = conexion.prepareStatement(BORRAR_PUNTAJE)) {
            sentencia.setInt(1, puntaje.getFecha());
            sentencia.setInt(2, puntaje.getPuntos());
            sentencia.executeUpdate();
        }
    }
}
