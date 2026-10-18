package motor.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import motor.modelo.graficos.Recorte;
import motor.persistencia.PKRecorte;

public class RecorteDAOSQLite implements DAO<Recorte, PKRecorte> {

    private final Connection conexion;
    private final int idSheet;

    public RecorteDAOSQLite(Connection conexion, int idSheet) {
        if (conexion == null) {
            throw new IllegalArgumentException("Conexion no puede ser nula");
        }
        this.conexion = conexion;
        this.idSheet = idSheet;
    }

    @Override
    public boolean guardar(Recorte objeto) {
        if (objeto == null) {
            return false;
        }
        String sqlCrearTabla = """
            CREATE TABLE IF NOT EXISTS recorte (
                id_sheet INTEGER NOT NULL,
                nombre_recorte TEXT NOT NULL,
                offset_x REAL NOT NULL,
                offset_y REAL NOT NULL,
                ancho REAL NOT NULL,
                alto REAL NOT NULL,
                FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
                PRIMARY KEY (id_sheet, nombre_recorte)
            );
            """;
        String sqlInsertar = """
            INSERT INTO recorte (id_sheet, nombre_recorte, offset_x, offset_y, ancho, alto)
            VALUES (?, ?, ?, ?, ?, ?);
            """;
        try {
            try (Statement sentencia = conexion.createStatement()) {
                sentencia.execute(sqlCrearTabla);
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertar)) {
                sentencia.setInt(1, this.idSheet);
                sentencia.setString(2, objeto.getNombreRecorte());
                sentencia.setDouble(3, objeto.getOffsetX());
                sentencia.setDouble(4, objeto.getOffsetY());
                sentencia.setDouble(5, objeto.getAncho());
                sentencia.setDouble(6, objeto.getAlto());
                return sentencia.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Recorte cargar(PKRecorte clave) {
        if (clave == null) {
            return null;
        }
        String sql = "SELECT id_sheet, nombre_recorte, offset_x, offset_y, ancho, alto FROM recorte WHERE id_sheet = ? AND nombre_recorte = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave.idSheet());
            sentencia.setString(2, clave.nombreRecorte());
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    int idSheet = rs.getInt("id_sheet");
                    String nombreRecorte = rs.getString("nombre_recorte");
                    double offsetX = rs.getDouble("offset_x");
                    double offsetY = rs.getDouble("offset_y");
                    double ancho = rs.getDouble("ancho");
                    double alto = rs.getDouble("alto");
                    return new Recorte(idSheet, nombreRecorte, offsetX, offsetY, ancho, alto);
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean eliminar(PKRecorte clave) {
        if (clave == null) {
            return false;
        }
        String sql = "DELETE FROM recorte WHERE id_sheet = ? AND nombre_recorte = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave.idSheet());
            sentencia.setString(2, clave.nombreRecorte());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Recorte> cargarTodos() {
        List<Recorte> recortes = new ArrayList<>();
        String sql = """
            SELECT id_sheet, nombre_recorte, offset_x, offset_y, ancho, alto
            FROM recorte
            WHERE id_sheet = ?
            ORDER BY nombre_recorte ASC;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, this.idSheet);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    int idSheet = rs.getInt("id_sheet");
                    String nombreRecorte = rs.getString("nombre_recorte");
                    double offsetX = rs.getDouble("offset_x");
                    double offsetY = rs.getDouble("offset_y");
                    double ancho = rs.getDouble("ancho");
                    double alto = rs.getDouble("alto");
                    recortes.add(new Recorte(idSheet, nombreRecorte, offsetX, offsetY, ancho, alto));
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return recortes;
    }

    @Override
    public boolean modificar(Recorte objeto) {
        if (objeto == null) {
            return false;
        }
        String sql = """
            UPDATE recorte
            SET offset_x = ?, offset_y = ?, ancho = ?, alto = ?
            WHERE id_sheet = ? AND nombre_recorte = ?;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setDouble(1, objeto.getOffsetX());
            sentencia.setDouble(2, objeto.getOffsetY());
            sentencia.setDouble(3, objeto.getAncho());
            sentencia.setDouble(4, objeto.getAlto());
            sentencia.setInt(5, this.idSheet);
            sentencia.setString(6, objeto.getNombreRecorte());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void mostrar(PKRecorte clave) {
        Recorte r = cargar(clave);
        if (r == null) {
            System.out.println("Recorte no encontrado para clave: " + clave);
            return;
        }
        System.out.printf("  Recorte [idSheet=%d, nombre=%s, offsetX=%.2f, offsetY=%.2f, ancho=%.2f, alto=%.2f]%n",
            r.getIdSheet(), r.getNombreRecorte(), r.getOffsetX(), r.getOffsetY(), r.getAncho(), r.getAlto());
    }

    @Override
    public void mostrarTodos() {
        List<Recorte> recortes = cargarTodos();
        if (recortes == null || recortes.isEmpty()) {
            System.out.println("  (Sin recortes para idSheet=" + this.idSheet + ")");
            return;
        }
        for (Recorte r : recortes) {
            mostrar(new PKRecorte(r.getIdSheet(), r.getNombreRecorte()));
        }
    }
}
