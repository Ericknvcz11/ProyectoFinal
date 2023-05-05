package pixelArtApp;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;


public class MenuIniciador extends JPanel {
    public int gridSize = 20;//tamaño de la cuadricula

    public MenuIniciador() {
    	
        PanelPrincipal mainPanel = new PanelPrincipal(gridSize, gridSize);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
}

