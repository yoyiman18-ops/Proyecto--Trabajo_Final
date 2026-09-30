/**
 * 
 *      NO VOLVER A MODIFICAR CON LÓGICA ACOPLADA AL JUEGO
 * 
 * 
 *      LA LÓGICA DEL JUEGO NO VA EN ESTA CLASE
 * 
 * 
 * 
 * 
 * 
 * 
 *
 *
 *
 *
 *
 *
 *
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */


package motor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import controlador.EnemigoControlador;
import controlador.JugadorControlador;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import modelo.Personaje;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.GestorEntradaTeclado;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.NotificadorFuerte;
import motor.util.observer.Observador;

public class Motor {
    private static Motor instancia;

    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    private final Pane root;
    private final EnumMap<Sistema,Observador<Double>> sistemasObservadores;

    private final CopyOnWriteArrayList<Controlador> controladores;
    private final CopyOnWriteArrayList<Colisionable> colisionables;

    public static Motor getInstancia() {
        if (Motor.instancia == null) { Motor.instancia = new Motor(); } return Motor.instancia;
    }

    private Motor() {
        this.sistemaColisiones = new SistemaColisiones(
            new FaseGeneralSpatialHashGrid(128), 
            new FaseEspecificaSimple()
        );
        this.gestorRecursos = new GestorRecursos();
        this.gestorEntradaTeclado = new GestorEntradaTeclado();
        this.root = new Pane();
        this.relojDelta = new RelojDelta(new NotificadorFuerte<>());

        this.colisionables = new CopyOnWriteArrayList<>();
        this.controladores = new CopyOnWriteArrayList<>();
        
        this.sistemasObservadores = new EnumMap<>(Sistema.class);
        sistemasObservadores.put(Sistema.COLISIONES, dt -> this.sistemaColisiones.resolverColisiones(colisionables));
        sistemasObservadores.put(Sistema.ENTRADA, dt -> this.gestorEntradaTeclado.tick());
        sistemasObservadores.put(Sistema.CONTROLADORES, dt -> { for (Controlador c : controladores) { c.tick(dt); }});

        for (Sistema s : Sistema.values()) { iniciar(s); }
    }

    public void instanciarJugador(String nombre) {
        instanciarJugador(nombre, personaje -> {});
    }

    public void instanciarJugador(String nombre, Observador<Personaje> observadorVida) {
        JugadorControlador c = new JugadorControlador(nombre, gestorRecursos, observadorVida);
        EnemigoControlador enemigo = new EnemigoControlador(c.getPersonaje());
        this.controladores.add(c);
        this.controladores.add(enemigo);
        this.gestorEntradaTeclado.suscribir(c.getObservadorAcciones());
        Platform.runLater(() -> root.getChildren().addAll(c.getVista(), enemigo.getVista()));
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public Pane root() { return this.root; }




    public void iniciar(Sistema sistema) { relojDelta.suscribirObservador(sistemasObservadores.get(sistema));}
    public void detener(Sistema sistema) { relojDelta.desuscribirObservador(sistemasObservadores.get(sistema));}

    public void iniciarReloj() { this.relojDelta.iniciar(); }
    public void detenerReloj() { this.relojDelta.detener(); }
}



