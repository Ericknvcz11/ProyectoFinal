package pixelArtApp;

import java.awt.*;
import javax.swing.*;

public class Ventana {

    public static JFrame frame;

    public static void main(String[] args) {
        // Crear la ventana principal	
    	
        frame = new JFrame("Pixel Art Proyecto Final");

        //configurations
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setExtendedState(JFrame.ICONIFIED);
        frame.setBackground(Color.YELLOW);

        //system
        MenuIniciador menu = new MenuIniciador();

        //adding components
        frame.add(menu, BorderLayout.CENTER);


        // Mostrar la ventana
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
    }
}

