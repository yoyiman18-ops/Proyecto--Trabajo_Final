package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
    private static Conexion instancia;

    private final Connection conexion;

    private Conexion() throws SQLException {
        conexion = DriverManager.getConnection("jdbc:sqlite:puntajes.db");
    }

    public static synchronized Conexion getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}
