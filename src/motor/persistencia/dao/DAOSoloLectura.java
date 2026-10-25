package motor.persistencia.dao;
import java.util.List;

public interface DAOSoloLectura<K,T> {
    T cargar(K clave);
    List<T> cargarTodos();
}
