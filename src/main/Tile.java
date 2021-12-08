package src.main;
import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	private int type;
	private int x;
	private int y;

	// tile types

	public Tile(BufferedImage image, int type, int x, int y) {
		this.image = image;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public BufferedImage getImage() {
		return image;
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

