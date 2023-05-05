package pixelArtApp;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelPrincipal extends JPanel {
  private JButton cargarImage, guardarImage;
  protected JButton jpgButton;
  protected JButton pincelButton, borrarButton;
  protected JDialog dialog;
  public JPanel colorSquare;
  public JPanel sidebar, gridPanel, textPanel;
  private final int rows;
  private final int cols;
  private final JPanel[][] squares;
  public Color pencil;
  protected String format = "";
  protected boolean isPen = true, isEraser = false, isEraseAll = false;
  public JPanel square;
  public int redVal = 0, greenVal = 0, blueVal = 0;
  protected boolean smallLoop = false;

  protected Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE, Color.GRAY, Color.YELLOW,  Color.ORANGE, Color.MAGENTA	 };
  public Color selectedColor;

  protected String mFilename = "";

  JFrame frame = Ventana.frame;
  TextoLayouts textmanager = new TextoLayouts(5, 500, 300, 300, Color.white); 


  public JSlider redSlider = new JSlider(0, 255),
   greenSlider = new JSlider(0, 255),
   blueSlider = new JSlider(0, 255);


  public PanelPrincipal(int rows, int cols) {
	  
//Slide izquierda 	  
    pencil = getCurrColor(0, 0, 0);
    this.rows = rows;
    this.cols = cols;
    this.squares = new JPanel[rows][cols];
    sidebar = new JPanel();
    gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(rows, cols));
    sidebar.setPreferredSize(new Dimension(220, 0));//tamaño de las opciones a la izquierda
    sidebar.setBackground(Color.WHITE);
    
    
// Texto arriba
    textPanel = new JPanel();
    textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100 , 0));//texto arriba del panel y tamaño
    textPanel.setPreferredSize(new Dimension(1, 25));
    textPanel.setBackground(Color.BLACK);
    
    
    cargarImage = new JButton("Cargar");
    cargarImage.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    String path = selectedFile.getAbsolutePath();
                    if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif")) {
                        // Load the selected image file
                        ImageIcon imageIcon = new ImageIcon(path);
                        Image image = imageIcon.getImage();

                        // Create a JLabel and set its icon to the loaded image
                        JLabel imageLabel = new JLabel();
                        imageLabel.setIcon(imageIcon);

                        // Add the JLabel to the panel or canvas
                        gridPanel.removeAll();
                        gridPanel.setLayout(new GridBagLayout());
                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.gridx = 0;
                        constraints.gridy = 0;
                        constraints.fill = GridBagConstraints.CENTER;
                        constraints.weightx = 1.0;
                        constraints.weighty = 1.0;
                        gridPanel.add(imageLabel, constraints);
                        gridPanel.revalidate();
                        gridPanel.repaint();

                    } else {
                        JOptionPane.showMessageDialog(null, "Formato de archivo no válido. Selecciona un archivo con extensión PNG, JPG o GIF.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    });

    
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            //JPanel square = new JPanel();
            square = new JPanel();
            square.setPreferredSize(new Dimension(20, 20)); // Set the size of each square
            square.setBackground(Color.white);
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridPanel.add(square);
            squares[row][col] = square;
        }
    }
    colorSquare = new JPanel();
    colorSquare.setPreferredSize(new Dimension(200, 100));//cuadrado selector de color
    colorSquare.setBackground(Color.WHITE);
    sidebar.add(colorSquare, BorderLayout.SOUTH);
    
    JPanel defaultColorsPanel = new JPanel();
    defaultColorsPanel.setLayout(new GridLayout(3, 2));//Forma cuadros para escoger colores
    defaultColorsPanel.setBackground(Color.WHITE);
    // loop through the colors array and create a button for each color
    
    
    // Create pen & eraser buttons
    JPanel middPanel = new JPanel();
    middPanel.setLayout(new BoxLayout(middPanel, BoxLayout.Y_AXIS));
    middPanel.setBackground(Color.WHITE);
    pincelButton =  new JButton("Pincel");
    borrarButton = new JButton("Borrar");

    // Create the green range input
        JPanel greenPanel = new JPanel();
        greenPanel.setLayout(new BoxLayout(greenPanel, BoxLayout.Y_AXIS));// se mueven los sliders a un lado 
        JLabel greenLabel = new JLabel("                            Verde                            ");
        greenSlider = new JSlider(0, 255, greenVal);
        greenSlider.setMajorTickSpacing(250);//valores del slider
        greenSlider.setPaintLabels(true);
        greenSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        greenSlider.setBackground(Color.WHITE);
        greenPanel.add(greenLabel);
        greenPanel.add(greenSlider);
        greenSlider.setValue(0);
        greenSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int red = redSlider.getValue();
                redVal = redSlider.getValue();
                int green = greenSlider.getValue();
                greenVal = greenSlider.getValue();
                int blue = blueSlider.getValue();
                blueVal = blueSlider.getValue();
                getCurrColor(red, green, blue);
                updateColorSquare(red, green, blue);
            }
        });

    // Create the blue range input
        JPanel bluePanel = new JPanel();
        bluePanel.setLayout(new BoxLayout(bluePanel, BoxLayout.Y_AXIS));
        JLabel blueLabel = new JLabel("                            Azul                            ");
        blueSlider = new JSlider(0, 255, blueVal);
        blueSlider.setMajorTickSpacing(250);
        blueSlider.setPaintLabels(true);
        blueSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        blueSlider.setBackground(Color.WHITE);
        bluePanel.add(blueLabel);
        bluePanel.add(blueSlider);
        blueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int red = redSlider.getValue();
                redVal = redSlider.getValue();
                int green = greenSlider.getValue();
                greenVal = greenSlider.getValue();
                int blue = blueSlider.getValue();
                blueVal = blueSlider.getValue();
                getCurrColor(red, green, blue);
                updateColorSquare(red, green, blue);
            }
        });
        // Create the red range input
        JPanel redPanel = new JPanel();
        redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.PAGE_AXIS));
        JLabel redLabel = new JLabel("                            Rojo                            ");
        redSlider = new JSlider(0, 255, redVal);
        redSlider.setMajorTickSpacing(250);
        redSlider.setPaintLabels(true);
        redSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        redSlider.setBackground(Color.WHITE);
        redPanel.add(redLabel);
        redPanel.add(redSlider);
        redSlider.setValue(0);
        redSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int red = redSlider.getValue();
                redVal = redSlider.getValue();
                int green = greenSlider.getValue();
                greenVal = greenSlider.getValue();
                int blue = blueSlider.getValue();
                blueVal = blueSlider.getValue();
                getCurrColor(red, green, blue);
                updateColorSquare(red, green, blue);
            }
        });


        // Add the back button to the panel with some margin
     JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10 , 25));//tamaño de la caja detras, color roja
     topPanel.setBackground(Color.WHITE);//back para botones save y back
     topPanel.add(cargarImage);


      
      for (Color c : colors) {
      JButton colorButton = new JButton();
      colorButton.setPreferredSize(new Dimension(20, 20));//tamaño cuadros de colores
      colorButton.setBackground(c);
      colorButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // set the selected color to the button's background color
            selectedColor = c;
            smallLoop = true;
            if (smallLoop) {
              updateBlueSilder(selectedColor);
              updateRedSlider(selectedColor);
              updateGreenSlider(selectedColor);
              smallLoop = false;
            }
        }
    });
    defaultColorsPanel.add(colorButton);
    }

    sidebar.add(defaultColorsPanel, BorderLayout.CENTER);

     

      pincelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          isEraser = false;
          isPen = true;
        }
      });

      borrarButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            isEraseAll = true;

            if (isEraseAll) {
              // erase the grid
              for (int row = 0; row < rows; row++) {
                  for (int col = 0; col < cols; col++) {
                      squares[row][col].setBackground(Color.white);
                  }
              }

              isEraseAll = false;
            }
          }
        });

      
      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
      inputPanel.setBackground(Color.WHITE);//back para los sliders
      inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//area que se pinta de rosa
      inputPanel.add(redPanel);
      inputPanel.add(greenPanel);
      inputPanel.add(bluePanel);
      sidebar.add(inputPanel, BorderLayout.CENTER);
      
      middPanel.add(borrarButton);
      
     

      sidebar.add(middPanel, BorderLayout.CENTER);

      // Create the color square panel

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JPanel square = squares[row][col];
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                          if(isPen) {
                            square.setBackground(pencil);
                          }

                          if(isEraser) {
                            square.setBackground(Color.white);
                          }
                          //
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                      if(isPen) {
                        square.setBackground(pencil);
                      }

                      if(isEraser) {
                        square.setBackground(Color.white);
                      }
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }
                });
            }
        }
    
    setLayout(new BorderLayout());
    add(sidebar, BorderLayout.EAST);//ubicacion del panel de acciones 
    add(gridPanel, BorderLayout.CENTER);// orientacion del canvas 
    add(textPanel, BorderLayout.PAGE_START);


    //default mess
    textmanager.setText("Bienvenidos a nuestro proyecto FINAL");
    textPanel.add(textmanager.getLabel());

    guardarImage = new JButton("Guardar");
    guardarImage.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Crear el diálogo modal
        dialog = new JDialog(frame, "Guardar Archivo", true);
        
        // Crear el panel con los botones
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel fieldInstructions = new JLabel("Nombre:");
        JTextField mTextInputField = new JTextField(20);
        //pngButton = new JButton("PNG");
        jpgButton = new JButton("Guardar");
        //gifButton = new JButton("GIF");
        panel.add(fieldInstructions);
        panel.add(mTextInputField);
        //panel.add(pngButton);
        panel.add(jpgButton);
        //panel.add(gifButton);

        
        
        // Agregar el panel al diálogo
        dialog.add(panel);

        jpgButton.addActionListener(new ActionListener() {
        	@Override
          public void actionPerformed(ActionEvent e ) {
            mFilename = mTextInputField.getText();
            saveAsJpg();
            dialog.dispose();
          }
        });
        
        // Configurar el diálogo
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);//checar
        dialog.setVisible(true);
        

      }
    });
       
    guardarImage.setLayout(new BoxLayout( guardarImage, BoxLayout.Y_AXIS));
    guardarImage.setAlignmentX(LEFT_ALIGNMENT);
    topPanel.add(guardarImage);

     sidebar.add(topPanel, BorderLayout.NORTH);
     
     
  }

  public JPanel updateColorSquare(int red, int green, int blue) {
    //colorSquare.setBackground(new Color(red, green, blue));
    colorSquare.setBackground(pencil);
    return colorSquare;
  }

  public Color getCurrColor(int red, int green, int blue) {
    Color drawingColor = new Color(red, green, blue);
    pencil = drawingColor;
    return drawingColor;
  }

  public void updateRedSlider (Color theColor) {
    redVal = theColor.getRed();
    redSlider.setValue(redVal);
  }

  public void updateGreenSlider (Color theColor) {
    greenVal = theColor.getGreen();
    greenSlider.setValue(greenVal);
  }

  public void updateBlueSilder (Color theColor) {
    blueVal = theColor.getBlue();
    blueSlider.setValue(blueVal);
  }

  public void saveAsJpg() {
    format = "jpg";
    System.out.println(format);
    saveAs(mFilename, format);
  }


  private void saveAs(String theFileName, String format) {
    try {
      // Create an image in the required format
      ImageIO.write(getPanelImage(), format, new File(theFileName + "." + format));
    } catch (IOException ex) {
    System.err.println("Problema con la imagen");
    }
  }

  public BufferedImage getPanelImage() {
    BufferedImage image = new BufferedImage(gridPanel.getSize().width, gridPanel.getSize().height, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = image.createGraphics();
    gridPanel.paint(g);
    g.dispose();
    return image;
  }

}