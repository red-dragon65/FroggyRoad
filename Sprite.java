//Class to draw graphics.

import javax.swing.*;
import java.awt.*;

//Class to make rectangles for collision method.
//Class to set sprite image.
//Class for graphics.


/*
 * Class that creates a sprite with an image, location,
 * directional movement and a collision method.
 */
public class Sprite {

	//Sprite location.
	protected double xloc, yloc;

	//Sprite direction.
	private double xdir, ydir;

	//Holds the image of the sprite.
	private ImageIcon image;

	//Draw sprite image or not.
	private boolean show = true;

	//Holds the image filename.
	private String filename = "";


	/**
	 * The default constructor.
	 */
	public Sprite() {
		image = null;
		xloc = 0;
		yloc = 0;
		xdir = 0;
		ydir = 0;
	}

	/**
	 * Constructor that sets the sprite
	 * image and location.
	 */
	public Sprite(String filename, int xloc, int yloc) {
		setImage(filename);
		this.xloc = xloc;
		this.yloc = yloc;
	}

	/**
	 * Constructor that takes the location
	 * as the argument.
	 */
	public Sprite(int xloc, int yloc) {
		this.xloc = xloc;
		this.yloc = yloc;
	}

	/**
	 * Constructor that takes an image filename as the argument.
	 */
	public Sprite(String filename) {
		setImage(filename);
	}


	/*
	 * Method to set the image variable.
	 */
	public void setImage(String filename) {
		this.filename = filename;

		try {
			this.image = new ImageIcon(getClass().getResource(filename));
		} catch (Exception e) {
			image = null;
		}
	}

	/*
	 * Getters.
	 */
	//Get xloc.
	public int getXLoc() {
		return (int) xloc;
	}

	/*
	 * Setters
	 */
	//Sets xloc.
	public void setXLoc(int xloc) {
		this.xloc = xloc;
	}

	//Get yloc.
	public int getYLoc() {
		return (int) yloc;
	}

	//Sets yloc.
	public void setYLoc(int yloc) {
		this.yloc = yloc;
	}

	//Get xdir.
	public double getXDir() {
		return xdir;
	}

	//Sets xdir.
	public void setXDir(double xdir) {
		this.xdir = xdir;
	}

	//Get ydir.
	public double getYDir() {
		return ydir;
	}

	//Sets ydir.
	public void setYDir(double ydir) {
		this.ydir = ydir;
	}

	//Get image filename.
	public String getFileName() {
		return filename;
	}


	/*
	 * Moves character by adding the 
	 * direction to the location.
	 */
	public void move() {
		xloc += xdir;
		yloc += ydir;
	}


	/*
	 * Collision method.
	 */
	//Return the width of the sprite
	//or 20 if the image is null.
	public int getWidth() {
		if (image == null)
			return 20;
		else
			return image.getIconWidth();
	}

	//Return the height of the sprite
	//or 20 if the image in null.
	public int getHeight() {
		if (image == null)
			return 20;
		else
			return image.getIconHeight();
	}

	//Method to check for collisions.
	public boolean isCollision(Sprite obj) {

		//Creates rectangles around sprites and checks for interesection.
		Rectangle sprite = new Rectangle((int) xloc, (int) yloc, getWidth(), getHeight());
		Rectangle object = new Rectangle(obj.getXLoc(), obj.getYLoc(), obj.getWidth(), obj.getHeight());

		return sprite.intersects(object);
	}


	/*
	 * Method to draw sprite onto JPanel.
	 */
	public void paint(Graphics g, JPanel panel) {
		if (show) {
			if (image == null)
				g.drawRect((int) xloc, (int) yloc, 20, 20);
			else
				image.paintIcon(panel, g, (int) xloc, (int) yloc);
		}
	}
	
	
}
