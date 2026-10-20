package motor.persistencia.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private final Connection conexion;
    private static Conexion instancia;

    private Conexion(Connection conexion) {
        this.conexion = conexion;
    }

    public static Conexion getInstancia() {
        if (Conexion.instancia == null) { 
            try {
            Conexion.instancia = new Conexion(DriverManager.getConnection("jdbc:sqlite:src/recursos/datos.db")); 
            } catch (SQLException e) { throw new IllegalStateException("Error al cargar los datos del juego."); }
        }
        return Conexion.instancia;
    }

    public Connection get() { return this.conexion; }
}
