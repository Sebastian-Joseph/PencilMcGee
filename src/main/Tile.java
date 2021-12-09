package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.concurrent.TimeUnit;

public class Tile {

	private BufferedImage image;
	private int type;
	private int x;
	private int y;
	private int initialx;
	private int initialy;

	private BufferedImage black;
	private BufferedImage white;

	public Tile(BufferedImage image, int type, int x, int y) throws IOException {
		this.image = image;
		this.type = type;
		this.x = x;
		this.y = y;
		initialx = x;
		initialy = y;

		white = ImageIO.read(getClass().getResourceAsStream("images/small_pooper.png"));
		black = ImageIO.read(getClass().getResourceAsStream("images/other-pooper.png"));
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void scroll(double scrollAmount) {
		x -= scrollAmount;
	}

	public void reset(){
		x = initialx;
		y = initialy;
	}

	public void newImage(String path) throws IOException{
		image = ImageIO.read(getClass().getResourceAsStream(path));
	}

	public void change() {
		if (type == 0) {
			new Thread(() -> {
				image = black;
				type = 1;
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				image = white;
				type = 0;


			}).start();
		}
	}
}