package motor.persistencia;

public record PKAnimacion(
    Integer id_sheet,
    String nombre_animacion
) {
    public Integer idSheet() { return id_sheet; }
    public String nombreAnimacion() { return nombre_animacion; }
}
