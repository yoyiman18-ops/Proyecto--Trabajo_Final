import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Graficos extends JButton {

    private ArrayList<EntidadViva> personajes;
    private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();

    public Graficos(ArrayList<EntidadViva> personajes) {

        this.personajes = personajes;
        
        for (EntidadViva personaje : this.personajes) {
            BufferedImage sprite;
            try {
            sprite = ImageIO.read(new File(personaje.getPathSprite()));
            sprites.add(sprite); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se ha encontrado la imagen.");
        }

        }


    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);

        for (int i = 0; i < sprites.size(); i++) {
            BufferedImage sprite = sprites.get(i);
            Vec2 posicion = personajes.get(i).getPosicion();
        if (sprite != null) {
            graficos.drawImage(sprite, (int) posicion.x, (int) posicion.y, this);
            }
        }

    }
}
