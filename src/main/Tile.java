package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {

	private String imagePath;
	private int type;
	private int x;
	private int y;

	public Tile(String imagePath, int type, int x, int y) {
		this.imagePath = imagePath;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public String getImagePath() {
		return imagePath;
	}

	public BufferedImage getImage() throws IOException {
		return ImageIO.read(getClass().getResourceAsStream(getImagePath()));
	}

	public int getType() {
		return type;
	}
	
	public int getx(){
		return x;
	}

	public int gety(){
		return y;
	}

}