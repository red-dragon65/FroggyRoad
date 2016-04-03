//For random numbers.

import java.util.Random;


class StripGenerator {

	/**
	 * Default constructor that returns a randomly
	 * generated sprite strip.
	 */
	Sprite[] getStrip() {

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
					Sprite strip = new Sprite("Misc/Road.png");
					spriteStrip[i] = strip;
				}
				break;

			//Tracks.
			case 1:
				for (int i = 0; i < y; i++) {
					Sprite strip = new Sprite("Misc/Tracks.png");
					spriteStrip[i] = strip;
				}
				break;

			//Special Land.
			case 2:
				for (int i = 0; i < y; i++) {
					//Holds random number.
					x = gen.nextInt(5);
					spriteStrip[i] = makeSpecialStrip(i, x, "Misc/Grass.png", "Misc/Grass.png", "Misc/Tree_One.png");
				}
				break;

			//Special Water.
			case 3:
				for (int i = 0; i < y; i++) {
					//Holds random number.
					x = gen.nextInt(5);
					spriteStrip[i] = makeSpecialStrip(i, x, "Misc/Water.png", "Misc/Water.png", "Misc/Lillypad.png");
				}
		}

		return spriteStrip;
	}


	/*
	 * Method to make a special strip (land/water)
	 * with blockages/platforms.
	 */
	private Sprite makeSpecialStrip(int i, int x, String background, String specialBlockOne, String specialBlockTwo) {

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
	Sprite[] getLandStrip() {

		//Makes random numbers.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Misc/Grass.png", "Misc/Grass.png", "Misc/Tree_One.png");
		}
		return spriteStrip;
	}

	/*
	 * Returns Specialized land strip.
	 */
	Sprite[] getSpecialLandStrip() {

		//Makes random number.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Misc/Grass.png", "Misc/Tree_Two.png", "Misc/Shrub.png");
		}

		return spriteStrip;
	}


	/*
	 * Returns a special water strip.
	 */
	Sprite[] getWaterStrip() {

		//Makes random numbers.
		Random gen = new Random();

		//Array to hold strip.
		Sprite[] spriteStrip = new Sprite[8];

		for (int i = 0; i < 8; i++) {
			//Holds random number.
			int x = gen.nextInt(5);
			spriteStrip[i] = makeSpecialStrip(i, x, "Misc/Water.png", "Misc/Water.png", "Misc/Lillypad.png");
		}

		return spriteStrip;
	}
//////////
}
