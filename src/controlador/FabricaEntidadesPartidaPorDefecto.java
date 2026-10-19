package controlador;

import java.util.List;
import java.util.Objects;
import java.util.function.DoubleSupplier;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import modelo.Enemigo;
import modelo.ItemMejoraAtributo;
import modelo.ModeloEnemigo;
import modelo.ModeloJugador;
import modelo.ModeloProyectil;
import modelo.ObjetivoEnemigo;
import modelo.Personaje;
import modelo.Proyectil;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.ControladorUsuario;
import motor.recursos.Extension.Imagen;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.Animacion;
import vista.EnemigoVista;
import vista.ItemVista;
import vista.ProyectilVista;
import vista.SpriteVista;
import vista.VistaEnemigo;
import vista.VistaItem;
import vista.VistaJugador;
import vista.VistaProyectil;

public final class FabricaEntidadesPartidaPorDefecto implements FabricaEntidadesPartida {
    private final Image spriteSheetJugador;
    private final Image spriteSheetProyectil;
    private final Image frameEnemigoIdle1;
    private final Image frameEnemigoIdle2;
    private final Image frameEnemigoGolpe;

    public FabricaEntidadesPartidaPorDefecto(GestorRecursos gestorRecursos) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.spriteSheetJugador = gestorRecursos.getImagen("zorro_sheet", Imagen.PNG);
        this.spriteSheetProyectil = gestorRecursos.getImagen(
            "RPG Effect All Free/Free/Part 16/766",
            Imagen.PNG
        );
        this.frameEnemigoIdle1 = gestorRecursos.getImagen("Transparent PNG/idle/frame-1", Imagen.PNG);
        this.frameEnemigoIdle2 = gestorRecursos.getImagen("Transparent PNG/idle/frame-2", Imagen.PNG);
        this.frameEnemigoGolpe = gestorRecursos.getImagen("Transparent PNG/got hit/frame", Imagen.PNG);
    }

    @Override
    public ModeloJugador crearModeloJugador() {
        return new Personaje.Builder(
            "jugador",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 30, 20),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(
                    CategoriaColision.ENEMIGO,
                    CategoriaColision.EXP,
                    CategoriaColision.ITEM,
                    CategoriaColision.MURO
                )
            )
        )
        .friccion(2500)
        .aceleracion(3000)
        .velocidadMax(240)
        .build();
    }

    @Override
    public ControladorUsuario crearControladorJugador(
        ModeloJugador modelo,
        DoubleSupplier anchoArea,
        DoubleSupplier altoArea,
        JugadorControlador.LimitesArea limites,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    ) {
        VistaJugador vistaJugador = new SpriteVista(spriteSheetJugador, 4, 4, 50, 50);
        return new JugadorControlador(
            modelo,
            vistaJugador,
            anchoArea,
            altoArea,
            limites,
            observadorVida,
            accionAtaque
        );
    }

    @Override
    public EnemigoControlador crearEnemigo(
        double x,
        double y,
        double velocidad,
        ObjetivoEnemigo objetivo
    ) {
        ModeloEnemigo modeloEnemigo = new Enemigo(
            "enemigo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 32, 32),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ENEMIGO),
                MascaraColision.of(CategoriaColision.JUGADOR, CategoriaColision.ALIADO)
            ),
            new VecDouble2D(x, y),
            velocidad,
            50,
            10,
            10
        );

        VistaEnemigo vistaEnemigo = new EnemigoVista(
            new Animacion(List.of(frameEnemigoIdle1, frameEnemigoIdle2), 0.35),
            frameEnemigoGolpe
        );

        return new EnemigoControlador(modeloEnemigo, vistaEnemigo, objetivo);
    }

    @Override
    public ExperienciaControlador crearExperiencia(
        ModeloJugador jugador,
        double x,
        double y,
        int cantidad
    ) {
        Entidad modeloExp = new Entidad(
            "experiencia",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 18, 18),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.EXP),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );

        VistaItem vistaExp = new ItemVista(9, Color.web("#83df4e"), Color.web("#e4ff9b"), 2);
        return new ExperienciaControlador(jugador, modeloExp, vistaExp, cantidad);
    }

    @Override
    public MejoraAtributoControlador crearMejoraAtributo(
        ModeloJugador jugador,
        ItemMejoraAtributo mejora,
        double x,
        double y
    ) {
        Entidad modeloMejora = new Entidad(
            "mejora-atributo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 22, 22),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ITEM),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );

        VistaItem vistaMejora = new ItemVista(11, Color.web("#e8bd45"), Color.web("#fff0a6"), 2);
        return new MejoraAtributoControlador(jugador, mejora, modeloMejora, vistaMejora);
    }

    @Override
    public ProyectilControlador crearProyectil(
        double x,
        double y,
        VecDouble2D direccion,
        double danio,
        boolean esCritico
    ) {
        Objects.requireNonNull(direccion, "La dirección del proyectil no puede ser nula");

        double tamanoHitbox = esCritico ? 20 : 16;
        double tamanoVisual = esCritico ? 38 : 30;
        int filaColor = esCritico ? 7 : 0;
        double velocidad = esCritico ? 430 : 380;

        ModeloProyectil modeloProyectil = new Proyectil(
            "proyectil",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, tamanoHitbox, tamanoHitbox),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO)
            ),
            new VecDouble2D(x, y),
            direccion,
            danio,
            velocidad,
            1.2
        );

        VistaProyectil vistaProyectil = new ProyectilVista(
            spriteSheetProyectil,
            new Animacion(spriteSheetProyectil, 8, 9, 0.08),
            filaColor,
            tamanoVisual,
            tamanoVisual,
            tamanoHitbox
        );

        return new ProyectilControlador(modeloProyectil, vistaProyectil);
    }
}
