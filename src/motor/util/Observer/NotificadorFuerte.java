package motor.util.Observer;
import java.util.HashSet;

// este notificador guarda referencias fuertes, impidiendo
// que los objetos sean liberados por el garbage collector
public class NotificadorFuerte<T> extends Notificador<T> {
    public NotificadorFuerte() {
        super(new HashSet<>());
    }

}
