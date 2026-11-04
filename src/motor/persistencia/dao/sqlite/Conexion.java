package motor.persistencia.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
    private static Conexion instancia;
    private final Connection conexion;

    synchronized public static Conexion getInstancia() {
        if (Conexion.instancia == null) { Conexion.instancia = new Conexion("jdbc:sqlite:src/recursos/datos.db"); }
        return Conexion.instancia;
    }

    private Conexion(String url) {
        if (url == null || url.isBlank() || url.isEmpty()) { throw new IllegalArgumentException("URL de conexión inválida."); }
        try { this.conexion = DriverManager.getConnection(url); } catch (SQLException e) {
            throw new IllegalStateException(
                "Error fatal cargando la base de datos principal" + url + " : " + e.getMessage(), e
            );
        }
    }

    public Connection get() { return conexion; }

    public void cerrar() {
        try { if (conexion != null && !conexion.isClosed()) { conexion.close(); }
        } catch (SQLException e) { throw new IllegalStateException("Error cerrando la conexión: " + e.getMessage()); }
    }

}
