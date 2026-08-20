import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Motor {
    public static void dibujar(ArrayList<EntidadViva> personajes) {

        //creo el marco
        JFrame marco = new JFrame ("Mi primera App swing");
        marco.setSize(300,400);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Graficos personaje = new Graficos(personajes);

        personaje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(marco, "hola");
            }
        });
     

    marco.add(personaje);


    marco.setVisible(true);
    }
}