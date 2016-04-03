//For random numbers.

import java.util.Random;


public class StripGenerator {

	/**
	 * Default constructor that returns a randomly
	 * generated sprite strip.
	 */
	public Sprite[] getStrip() {

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		//Creates random generator.
		Random gen = new Random();

		//Number of grids wide.
		int y = spriteStrip.length;

		//Selects landscape.
		int x = gen.nextInt(4);


		//Sets landscape.
		switch (x) {
			//Road.
			case 0:
				for (int i = 0; i < y; i++) {
					Sprite strip = new Sprite("Road.png");
					spriteStrip[i] = strip;
				}
				break;

			//Tracks.
			case 1:
				for (int i = 0; i < y; i++) {
					Sprite strip = new Sprite("Tracks.png");
					spriteStrip[i] = strip;
				}
				break;

			//Special Land.
			case 2:
				for (int i = 0; i < y; i++) {
					//Holds random number.
					x = gen.nextInt(5);
					spriteStrip[i] = makeSpecialStrip(i, x, "Grass.png", "Grass.png", "Tree_One.png");
				}
				break;

			//Special Water.
			case 3:
				for (int i = 0; i < y; i++) {
					//Holds random number.
					x = gen.nextInt(5);
					spriteStrip[i] = makeSpecialStrip(i, x, "Water.png", "Water.png", "Lillypad.png");
				}
		}

		return spriteStrip;
	}


	/*
	 * Method to make a special strip (land/water)
	 * with blockages/platforms.
	 */
	public Sprite makeSpecialStrip(int i, int x, String background, String specialBlockOne, String specialBlockTwo) {

		Sprite oneBlock = new Sprite();

		switch (x) {
			//Add normal.
			case 0:
				oneBlock.setImage(background);
				break;

			//Add normal.
			case 1:
				oneBlock.setImage(background);
				break;

			//Add normal.
			case 2:
				oneBlock.setImage(background);
				break;


			//Add blockage.
			case 3:
				oneBlock.setImage(specialBlockOne);
				break;

			//Add blockage.
			case 4:
				oneBlock.setImage(specialBlockTwo);
				break;
		}

		//Adds image to strip.
		return oneBlock;
	}


	/*
	 * Returns a special land strip.
	 */
	public Sprite[] getLandStrip() {

		//Makes random numbers.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Grass.png", "Grass.png", "Tree_One.png");
		}
		return spriteStrip;
	}

	/*
	 * Returns Specialized land strip.
	 */
	public Sprite[] getSpecialLandStrip() {

		//Makes random number.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Grass.png", "Tree_Two.png", "Shrub.png");
		}

		return spriteStrip;
	}


	/*
	 * Returns a special water strip.
	 */
	public Sprite[] getWaterStrip() {

		//Makes random numbers.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Water.png", "Water.png", "Lillypad.png");
		}

		return spriteStrip;
	}
//////////
}
