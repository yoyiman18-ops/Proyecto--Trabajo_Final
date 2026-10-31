package motor.persistencia.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Conexion {
    private static Map<String,Conexion> instancias;
    private final Connection conexion;

    public static Conexion getInstancia(String url) {
        if (Conexion.instancias == null) { Conexion.instancias = new HashMap<>(); }
        return Conexion.instancias.computeIfAbsent(url, u -> new Conexion(u));
    }

    private Conexion(String url) {
        Objects.requireNonNull(url, "La URL de conexión no puede ser nula.");
        try {
            this.conexion = DriverManager.getConnection(url);
        } catch (SQLException e) { throw new IllegalStateException("Error fatal cargando la base de datos " + url +" : " + e.getMessage()); }
    }

    public Connection getConexion() {
        return conexion;
    }
}
