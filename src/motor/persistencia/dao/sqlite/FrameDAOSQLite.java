package motor.persistencia.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import motor.modelo.graficos.Frame;
import motor.modelo.graficos.Recorte;
import motor.persistencia.PKFrame;
import motor.persistencia.dao.FrameDAO;

public class FrameDAOSQLite extends FrameDAO {

    private final Connection conexion;


    public FrameDAOSQLite(Connection conexion, int idSheet, String nombreAnimacion) {
        super(nombreAnimacion,idSheet);
        if (conexion == null) { throw new IllegalArgumentException("Conexion nula invalida"); }
        this.conexion = conexion;
    }

    @Override
    public boolean guardar(Frame objeto) {
        if (objeto == null) {
            return false;
        }
        String sqlCrearTabla = """
            CREATE TABLE IF NOT EXISTS frame (
                id_sheet INTEGER NOT NULL,
                nombre_animacion TEXT NOT NULL,
                nro_frame INTEGER NOT NULL,
                nombre_recorte TEXT NOT NULL,
                duracion INTEGER NOT NULL,
                FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
                FOREIGN KEY (id_sheet, nombre_animacion) REFERENCES animacion(id_sheet, nombre_animacion) ON DELETE CASCADE,
                FOREIGN KEY (id_sheet, nombre_recorte) REFERENCES recorte(id_sheet,nombre_recorte) ON DELETE CASCADE,
                PRIMARY KEY (id_sheet, nombre_animacion, nro_frame)
            );
            """;
        String sqlInsertar = """
            INSERT INTO frame (id_sheet, nombre_animacion, nro_frame, nombre_recorte, duracion)
            VALUES (?, ?, ?, ?, ?);
            """;
        try {
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sqlCrearTabla);
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertar)) {
                sentencia.setInt(1, objeto.getIdSheet());
                sentencia.setString(2, objeto.getNombreAnimacion());
                sentencia.setInt(3, objeto.getNroFrame());
                sentencia.setString(4, objeto.getNombreRecorte());
                sentencia.setInt(5, objeto.getDuracion());
                return sentencia.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Frame cargar(PKFrame clave) {
        if (clave == null) {
            return null;
        }
        String sql = "SELECT id_sheet, nombre_animacion, nro_frame, nombre_recorte, duracion FROM frame WHERE id_sheet = ? AND nombre_animacion = ? AND nro_frame = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave.idSheet());
            sentencia.setString(2, clave.nombreAnimacion());
            sentencia.setInt(3, clave.nroFrame());
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    int idSheet = rs.getInt("id_sheet");
                    String nombreAnimacion = rs.getString("nombre_animacion");
                    int nroFrame = rs.getInt("nro_frame");
                    String nombreRecorte = rs.getString("nombre_recorte");
                    int duracion = rs.getInt("duracion");

                    Recorte recorte = null;
                    try (PreparedStatement sentenciaRecorte = conexion.prepareStatement(
                            "SELECT offset_x, offset_y, ancho, alto FROM recorte WHERE id_sheet = ? AND nombre_recorte = ?;")) {
                        sentenciaRecorte.setInt(1, idSheet);
                        sentenciaRecorte.setString(2, nombreRecorte);
                        try (ResultSet rsRecorte = sentenciaRecorte.executeQuery()) {
                            if (rsRecorte.next()) {
                                recorte = new Recorte(
                                    idSheet,
                                    nombreRecorte,
                                    rsRecorte.getDouble("offset_x"),
                                    rsRecorte.getDouble("offset_y"),
                                    rsRecorte.getDouble("ancho"),
                                    rsRecorte.getDouble("alto")
                                );
                            }
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException("Error cargando los recortes del frame.");
                    }
                    if (recorte == null) {
                        throw new IllegalStateException("Recorte no encontrado al cargar el frame.");
                    }
                    return new Frame(idSheet, nombreAnimacion, nroFrame, nombreRecorte, recorte, duracion);
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean eliminar(PKFrame clave) {
        if (clave == null) {
            return false;
        }
        String sql = "DELETE FROM frame WHERE id_sheet = ? AND nombre_animacion = ? AND nro_frame = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave.idSheet());
            sentencia.setString(2, clave.nombreAnimacion());
            sentencia.setInt(3, clave.nroFrame());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Frame> cargarTodos() {
        List<Frame> frames = new ArrayList<>();
        String sql = """
            SELECT id_sheet, nombre_animacion, nro_frame, nombre_recorte, duracion
            FROM frame
            WHERE id_sheet = ? AND nombre_animacion = ?
            ORDER BY nro_frame ASC;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, getIdSheet());
            sentencia.setString(2, getNombreAnimacion());
            try (ResultSet rs = sentencia.executeQuery()) {
                // mientras haya frames correspondientes, los 
                while (rs.next()) {
                    int idSheet = rs.getInt("id_sheet");
                    String nombreAnimacion = rs.getString("nombre_animacion");
                    int nroFrame = rs.getInt("nro_frame");
                    String nombreRecorte = rs.getString("nombre_recorte");
                    int duracion = rs.getInt("duracion");

                    // busca el recorte correspondiente a este frame en la bd
                    Recorte recorte = null;
                    try (PreparedStatement sentenciaRecorte = conexion.prepareStatement(
                            "SELECT offset_x, offset_y, ancho, alto FROM recorte WHERE id_sheet = ? AND nombre_recorte = ?;")) {
                        sentenciaRecorte.setInt(1, idSheet);
                        sentenciaRecorte.setString(2, nombreRecorte);
                        try (ResultSet rsRecorte = sentenciaRecorte.executeQuery()) {
                            if (rsRecorte.next()) {
                                recorte = new Recorte(
                                    idSheet,
                                    nombreRecorte,
                                    rsRecorte.getDouble("offset_x"),
                                    rsRecorte.getDouble("offset_y"),
                                    rsRecorte.getDouble("ancho"),
                                    rsRecorte.getDouble("alto")
                                );
                            }
                        }
                    } catch (SQLException e) { throw new IllegalStateException("Error cargando los recortes del frame."); }
                    if (recorte == null) {
                        recorte = new Recorte(idSheet, nombreRecorte, 0, 0, 1, 1);
                    }
                    frames.add(new Frame(idSheet, nombreAnimacion, nroFrame, nombreRecorte, recorte, duracion));
                }
            }
        } catch (SQLException e) { throw new IllegalStateException("Error cargando los frames."); }
        return frames;
    }

    @Override
    public boolean modificar(Frame objeto) {
        if (objeto == null) {
            return false;
        }
        String sql = """
            UPDATE frame
            SET nombre_recorte = ?, duracion = ?
            WHERE id_sheet = ? AND nombre_animacion = ? AND nro_frame = ?;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, objeto.getNombreRecorte());
            sentencia.setInt(2, objeto.getDuracion());
            sentencia.setInt(3, objeto.getIdSheet());
            sentencia.setString(4, objeto.getNombreAnimacion());
            sentencia.setInt(5, objeto.getNroFrame());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
}
