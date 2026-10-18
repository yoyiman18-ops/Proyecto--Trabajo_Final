package motor.persistencia.dao;
import java.util.List;

/**
Interfaz genérica de DAO para guardar objetos de tipo T identificados por una clave de tipo K
*/
public interface DAO<T,K> {
    boolean guardar(T objeto);
    T cargar(K clave);
    boolean eliminar(K clave);
    List<T> cargarTodos();
    boolean modificar(T objeto);
    void mostrar(K clave);
    void mostrarTodos();
}
