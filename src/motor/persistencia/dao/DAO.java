package motor.persistencia.dao;

/**
Interfaz genérica de DAO para guardar objetos de tipo T identificados por una clave de tipo K
*/
public interface DAO<K,T> extends DAOSoloLectura<K,T> {
    boolean guardar(T objeto);
    boolean eliminar(K clave);
    boolean modificar(T objeto);
}
