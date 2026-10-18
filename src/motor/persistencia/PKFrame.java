package motor.persistencia;

public record PKFrame(
    Integer id_sheet,
    String nombre_animacion,
    Integer nro_frame
) {
    public Integer idSheet() { return id_sheet; }
    public String nombreAnimacion() { return nombre_animacion; }
    public Integer nroFrame() { return nro_frame; }
}
