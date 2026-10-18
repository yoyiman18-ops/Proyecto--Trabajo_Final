package motor.persistencia;

public record PKRecorte(
    Integer id_sheet,
    String nombre_recorte
) {
    public Integer idSheet() { return id_sheet; }
    public String nombreRecorte() { return nombre_recorte; }
}
