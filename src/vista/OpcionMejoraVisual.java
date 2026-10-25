package vista;

import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) para representar una opción de mejora
 * en la interfaz gráfica, manteniendo la vista totalmente desacoplada del modelo de dominio.
 */
public record OpcionMejoraVisual(
    String rareza,
    String nombreAtributo,
    String valorBonus,
    String descripcion,
    String claveIcono
) {
    public OpcionMejoraVisual {
        Objects.requireNonNull(rareza, "La rareza no puede ser nula");
        Objects.requireNonNull(nombreAtributo, "El nombre del atributo no puede ser nulo");
        Objects.requireNonNull(valorBonus, "El valor bonus no puede ser nulo");
        Objects.requireNonNull(descripcion, "La descripción no puede ser nula");
        Objects.requireNonNull(claveIcono, "La clave de icono no puede ser nula");
    }
}
