package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import modelo.Puntaje;

public final class PuntajeDAOSQLite implements PuntajeDAO {

    private final String crearTabla;
    private final String insertarPuntaje;
    private final String borrarPuntaje;
    private final Connection conexion;

    public PuntajeDAOSQLite(ProveedorConexion proveedorConexion) throws SQLException {
        this(
            Objects.requireNonNull(proveedorConexion, "El proveedor de conexión no puede ser nulo.").getConexion()
        );
    }

    public PuntajeDAOSQLite(Connection conexion) throws SQLException {
        this.conexion = Objects.requireNonNull(conexion, "La conexión no puede ser nula.");
        this.crearTabla =
            "CREATE TABLE IF NOT EXISTS puntajes ("
                + "fecha INTEGER NOT NULL, "
                + "puntos INTEGER NOT NULL"
                + ")";
        this.insertarPuntaje = "INSERT INTO puntajes (fecha, puntos) VALUES (?, ?)";
        this.borrarPuntaje = "DELETE FROM puntajes WHERE fecha = ? AND puntos = ?";

        try (Statement sentencia = this.conexion.createStatement()) {
            sentencia.executeUpdate(this.crearTabla);
        }
    }

    @Override
    public void guardar(Puntaje puntaje) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");

        try (PreparedStatement sentencia = conexion.prepareStatement(insertarPuntaje)) {
            sentencia.setInt(1, puntaje.getFecha());
            sentencia.setInt(2, puntaje.getPuntos());
            sentencia.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException(); }
    }

    @Override
    public void borrar(Puntaje puntaje) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement(borrarPuntaje)) {
            sentencia.setInt(1, puntaje.getFecha());
            sentencia.setInt(2, puntaje.getPuntos());
            sentencia.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Error al borrar el puntaje"); }
    }
}
