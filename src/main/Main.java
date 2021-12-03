import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.awt.Image;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Java Game");

        

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        
        try
        {
            URL pooper = getClass().getResource("/Images/pooper2.png");
            BufferedImage myImage = ImageIO.read(pooper);
        } 
        catch (IOException e)
        {
            System.out.println(e);
        }

        //JFrame myJFrame = new JFrame("Image pane");
        //myJFrame.setContentPane(new ImagePanel(myImage));

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}