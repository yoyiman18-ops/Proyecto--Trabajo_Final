package motor.persistencia.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Conexion {
    private static final Map<String, Conexion> INSTANCIAS = new ConcurrentHashMap<>();
    private final Connection conexion;

    public static Conexion getInstancia(String url) {
        Objects.requireNonNull(url, "La URL de conexión no puede ser nula.");
        return INSTANCIAS.computeIfAbsent(url, Conexion::new);
    }

    private Conexion(String url) {
        Objects.requireNonNull(url, "La URL de conexión no puede ser nula.");
        try {
            this.conexion = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new IllegalStateException("Error fatal cargando la base de datos " + url + " : " + e.getMessage(), e);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public Connection get() {
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void cerrarTodas() {
        for (Conexion c : INSTANCIAS.values()) {
            c.cerrar();
        }
        INSTANCIAS.clear();
    }
}
