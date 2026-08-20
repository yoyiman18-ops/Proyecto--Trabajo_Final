import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Graficos extends JButton {

    private BufferedImage imagen;

    public Graficos() {
        try {
        imagen = ImageIO.read(new File("recursos/personaje.jpg")); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se ha encontrado la imagen.");
        }
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);

        if (imagen != null) {
            graficos.drawImage(imagen, 0, 0, this);
        }

    }
}
