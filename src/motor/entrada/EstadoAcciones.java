package motor.entrada;
import java.util.EnumSet;

// una snapshot del estado de acciones
public record EstadoAcciones(EnumSet<Accion> estado) {    
    public EstadoAcciones(EnumSet<Accion> estado) {
        this.estado = EnumSet.copyOf(estado);
    }

    public boolean activa(Accion accion) { return estado.contains(accion); }
    public String getInfo(Accion accion) {
        return String.format(
            "ACCION: %10s | ESTADO: %5b",
            accion.toString(),
            activa(accion)            
        );
    }

    @Override 
    public String toString() {
        String cadenaRegistroEstado = new String("\n--- REGISTRO DE ESTADO DE ACCIONES ---\n");
        for (Accion a : Accion.values()) { cadenaRegistroEstado += getInfo(a) + "\n"; }
        cadenaRegistroEstado += "--- FIN REGISTRO --- ";
        return cadenaRegistroEstado;
    }
}
