import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Image;
import java.awt.Graphics;


public class GamePanel extends JPanel {
    // Screen Settings
    final int originalTileSize = 8;
    final int scale = 4;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 48;
    final int maxScreenRow = 27;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.decode("#ff0000"));
        this.setDoubleBuffered(true);
    }

    

    public void run() {

    }
}


class ImagePanel extends JComponent {
    private Image image;
    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}