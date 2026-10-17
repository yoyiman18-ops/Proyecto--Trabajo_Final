package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class Conexion implements ProveedorConexion {
    private final Connection conexion;

    public Conexion() throws SQLException {
        this("jdbc:sqlite:puntajes.db");
    }

    public Conexion(String url) throws SQLException {
        Objects.requireNonNull(url, "La URL de conexión no puede ser nula.");
        this.conexion = DriverManager.getConnection(url);
    }

    @Override
    public Connection getConexion() {
        return conexion;
    }
}
