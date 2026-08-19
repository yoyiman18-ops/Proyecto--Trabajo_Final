import javax.swing.*;
import java.awt.event.*;

public class Motor {
    public static void main (String[] args){
        //creo el marco
        JFrame marco = new JFrame ("Mi primera App swing");
        marco.setSize(300,400);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton boton = new JButton ("HAZ CLIC AQUI");

        boton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        JOptionPane.showMessageDialog(marco, "hola");
    }
    });
     

    marco.getContentPane().add(boton);

    marco.setVisible(true);
      }
}