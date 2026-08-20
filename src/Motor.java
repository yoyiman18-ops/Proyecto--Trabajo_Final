import javax.swing.*;
import java.awt.event.*;

public class Motor {
    public static void dibujar() {

        //creo el marco
        JFrame marco = new JFrame ("Mi primera App swing");
        marco.setSize(300,400);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Graficos personaje = new Graficos();

        personaje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(marco, "hola");
            }
        });
     

    marco.add(personaje);


    marco.setVisible(true);
    }
}