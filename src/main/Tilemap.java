package src.main;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;


public class Tilemap{

    private int tileSize = 24;
    private int numVisibleRows;
    private int numVisibleColumns;
    private Tile[][] map;
    private int Rows;
    private int Columns;

    public Tilemap(int width, int height){        
        numVisibleRows = height/(tileSize + 2);
        numVisibleColumns = width/(tileSize + 2);
    }

    public void createMap(String filename) throws IOException{
        BufferedImage tileimage = ImageIO.read(getClass().getResourceAsStream(filename));
        Columns = tileimage.getWidth();
        Rows = tileimage.getHeight();
        map = new Tile[Rows][Columns];

        BufferedImage subimage;
        for (int col = 0; col < Columns; col++) {
            for (int row = 0; row < Rows; row++) {
                subimage = tileimage.getSubimage(col, row, 1, 1);
                BufferedImage scaledImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
                final AffineTransform at = AffineTransform.getScaleInstance(24.0, 24.0);
                final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
                scaledImage = ato.filter(subimage, scaledImage);

                Color c = new Color(subimage.getRGB(0,0));
                if(c.getRed() == 0){
                    map[row][col] = new Tile(scaledImage, 1, 24 * col, 24 * row);
                }
                else{
                    map[row][col] = new Tile(scaledImage, 0, 24 * col, 24 * row);
                }


                

            }
        }

		


    }

    public void draw(Graphics2D g){
        System.out.println(map[0][0].getImage());
        for(int col = 0; col < numVisibleColumns; col++){
            for(int row = 0; row < numVisibleRows; row++){
                g.drawImage(map[row][col].getImage(), col * 24, row * 24, null);
            }
        }
    }


}