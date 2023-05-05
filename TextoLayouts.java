package pixelArtApp;

import javax.swing.JLabel;
import java.awt.*;

public class TextoLayouts {
  private JLabel label;
  public TextoLayouts(int x, int y, int width, int height, Color foreground) {
    label = new JLabel();
    label.setBounds(x, y, width, height); 
    label.setForeground(foreground); 
  }

  public void setText(String text) {
    label.setText(text);
  }

  public void setColor(Color color) {
    label.setForeground(color); 
  }

  public JLabel getLabel() {
    return label;
  }
}