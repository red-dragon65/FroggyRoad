//Class for graphics..

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

//Class for event/key listeners.
//Class for random numbers.
//Class for array lists.
//Class for timer, JPanel and dialog boxes.



/**
 * JPanel to hold the game in the window.
 */
//@SuppressWarnings("serial")
class Display extends JPanel implements ActionListener {

	//Variable for the game logo 'Froggy Road'.
	private Sprite logo = new Sprite("Misc/Logo.png");
	private boolean showLogo = false;
	//New game variables.
	private boolean newGame = false;

	/*
	 * Variables.
	 */
	//Creates a strip generator object.
	private StripGenerator stripGen = new StripGenerator();
	//Holds Number of strips on screen.
	private int numOfStrips = 9;
	//2D array for holding sprite strips.
	private Sprite[][] allStrips = new Sprite[numOfStrips][8];
	//Holds the index values of special strip images.
	private ArrayList<Integer> special = new ArrayList<>();
	//Holds number of special images in special strip.
	private int land = 1, water = 0;
	//Array that holds the cars.
	private ArrayList<Sprite> cars = new ArrayList<>();
	//Array that holds the trains.
	private ArrayList<Sprite> trains = new ArrayList<>();
	private JButton startButton, controlsButton;


	//Create hero sprite.
	private Sprite hero = new Sprite("Frog/Frog_up.png");

	//Variable to hold score and travel.
	private int score = 0, movement = 0;
	private Score scoreManager = new Score();

	//Variables for directional control.
	private int up = 0, down = 0, left = 0, right = 0;
	private boolean press = false;

	//Variables for hero invincibility power.
	private int invincibility = 0, invDuration = 150, invTimeLeft = 0;

	//Create timer.
	private Timer gameLoop;

	//Create random generator.
	private Random rand = new Random();



	/**
	 * Default constructor.
	 */
	Display(boolean pause) {

		//Set layout to absolute for buttons.
		setLayout(null);

		//Create button component, set image, remove borders.
		startButton = new JButton(new ImageIcon(getClass().getResource("Misc/Start.png")));
		startButton.setBorder(BorderFactory.createEmptyBorder());
		controlsButton = new JButton(new ImageIcon(getClass().getResource("Misc/Controls.png")));
		controlsButton.setBorder(BorderFactory.createEmptyBorder());

		startButton.addActionListener(this);
		controlsButton.addActionListener(this);

		add(startButton);
		add(controlsButton);

		startButton.setBounds(250, 175, 300, 200);
		controlsButton.setBounds(300, 390, 200, 100);


		//Create key listener for character.
		addKeyListener(new KeyPressing());

		//Set the focus to JPanel.
		setFocusable(true);

		//Make the movement smooth.
		setDoubleBuffered(true);

		//Method to set the sprite locations.
		setInitialLocations();

		//Create the game timer and start it.
		gameLoop = new Timer(25, this);

		///Pauses the game on first run.
		if (!pause) {
			startButton.setVisible(false);
			controlsButton.setVisible(false);
			gameLoop.start();
		} else
			showLogo = true;

	}


	/**
	 * Method to set the initial location of all the sprites.
	 */
	private void setInitialLocations() {

		//Sets the heros location.
		hero.setXLoc(298);
		hero.setYLoc(400);


		//Initializes game with land strips.
		for (int i = 0; i < numOfStrips; i++) {

			//Creates a new land sprite strip.
			Sprite[] strip = stripGen.getLandStrip();

			//Adds sprite strip to strips array.
			allStrips[i] = strip;
		}

		//Sets a grass image under and in front of the frog location.
		//(Prevents the frog from starting on a tree or shrub)
		allStrips[5][3].setImage("Misc/Grass.png");
		allStrips[4][3].setImage("Misc/Grass.png");
		
		
		/*
		 * Sets the location for the sprites in the strip array.
		 */
		//Spaces sprites 100 pixels apart horizontally.
		int x = 0;
		//Spaces sprites 100 pixels apart vertically.
		int y = -100;

		for (int i = 0; i < numOfStrips; i++) {

			for (int z = 0; z < 8; z++) {

				allStrips[i][z].setXLoc(x);

				allStrips[i][z].setYLoc(y);

				x += 100;
			}
			x = 0;
			y += 100;
		}

		//Sets special array to first initialized land sprite array.
		//Prevents water/lillypad offset if it is generated right after the grass field.
		for (int i = 0; i < 8; i++) {
			if (allStrips[0][i].getFileName().equals("Misc/Grass.png")) {
				special.add(i);
				land++;
			}
		}
	}


	/**
	 * Timer runs these statement on a loop.
	 */
	public void actionPerformed(ActionEvent e) {

		//Makes a new game if start button is clicked.
		if (e.getSource() == startButton) {

			newGame = true;
			newGame();

		}
		//Show message dialog with controls.
		else if (e.getSource() == controlsButton) {

			JOptionPane.showMessageDialog(null, "Arrow Keys:  Move the frog." +
					"\nCtrl:  Activates 3 seconds of invincibility once per game." +
					"\n         (Makes frog pass through any object)" +
					"\nShift:  Pause / Resume the game." +
					"\nEnter:  Start game / Restart game while paused.");

		}
		//Runs the timer.
		else {

			//Method that prevents hero from moving onto 
			//trees and checks for death and invincibility.
			heroCollision();

			//Method to smoothly move the character one block.
			jumpHero();

			//Sprite method that moves the hero.
			hero.move();


			//Method to move cars.
			manageCars();

			//Method to move trains.
			manageTrains();


			//Moves all the sprites in the sprite strips.
			for (int i = 0; i < numOfStrips; i++) {
				for (int x = 0; x < 8; x++) {
					allStrips[i][x].move();
				}
			}

			//Method that resets the strips.
			manageStrips();


			//Method to set the scrolling speed.
			scrollScreen();

			//Assigns farthest travel to score.
			if (movement > score)
				score = movement;


			//Redraws sprites on screen.
			repaint();
		}
	}


	/**
	 * Method that starts a new game.
	 */
	private void newGame() {

		if (newGame) {

			//Get this JFrame and destroy it.
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
			frame.dispose();

			//Create new main menu JFrame.
			new Window(false);
		}
	}



	/**
	 * Method that prevent hero from moving on trees, and checks
	 * for death with water, train, or cars.
	 */
	private void heroCollision() {

		//Invincibility timing.
		if (invincibility < invDuration && invincibility > 0)
			invincibility++;
		else if (invincibility == invDuration)
			invincibility = -1;

		//Counts down from 3.
		if (invincibility == 10)
			invTimeLeft = 3;
		else if (invincibility == 50)
			invTimeLeft = 2;
		else if (invincibility == 100)
			invTimeLeft = 1;


		//Collision method for frog.
		for (int i = 0; i < numOfStrips; i++) {
			for (Sprite s : allStrips[i]) {

				//Checks too see if user is invincible.
				if (invincibility <= 0) {

					//Prevents hero from jumping through trees.
					if (s.getFileName().equals("Misc/Tree_One.png") || s.getFileName().equals("Misc/Tree_Two.png")) {
						if (hero.isCollision(s)) {

							if ((s.getYLoc() + 100) - (hero.getYLoc()) < 5 && (s.getXLoc() + 100) - hero.getXLoc() < 125 && (s.getXLoc() + 100) - hero.getXLoc() > 20) {
								up = 0;
							} else if ((hero.getYLoc() + 105) > (s.getYLoc()) && (hero.getXLoc() + 100) - s.getXLoc() < 125 && (hero.getXLoc() + 100) - s.getXLoc() > 20) {
								down = 0;
							} else if (hero.getXLoc() - (s.getXLoc() + 100) > -5 && (s.getYLoc() + 100) - hero.getYLoc() < 125 && (s.getYLoc() + 100) - hero.getYLoc() > 20) {
								left = 0;
							} else if (s.getXLoc() - (hero.getXLoc() + 100) > -5 && (hero.getYLoc() + 100) - s.getYLoc() < 125 && (hero.getYLoc() + 100) - s.getYLoc() > 20) {
								right = 0;
							}
						}
					}

					//Ends game if user lands on water.
					if (s.getFileName().equals("Misc/Water.png")) {
						if (s.getXLoc() - hero.getXLoc() > 0 && s.getXLoc() - hero.getXLoc() < 10) {
							if (s.getYLoc() - hero.getYLoc() > 0 && s.getYLoc() - hero.getYLoc() < 10) {

								//Method to end game.
								killMsg("water");
							}
						}
					}
				}


				//Ends game if user goes too far down.
				if (hero.getYLoc() > 800) {

					//Reset hero location.
					hero.setYLoc(500);
					hero.setXLoc(900);

					//Method to end game.
					killMsg("tooFarDown");
				}

				//Ends game if user goes too far up.
				if (hero.getYLoc() < -110) {

					//Reset hero location.
					hero.setYLoc(500);
					hero.setXLoc(900);

					//Method to end game.
					killMsg("tooFarUp");
				}
			}
		}

	}

	/**
	 * Method to end game.
	 * Stops loop, saves scores, displays message.
	 */
	private void killMsg(String killer) {

		repaint();
		gameLoop.stop();
		scoreManager.updateScores(score);

		//Displays correct message based on death.
		switch (killer) {
			case "water":
				JOptionPane.showMessageDialog(null, "You drowned!" + "\nScore: " + score);
				break;
			case "tooFarDown":
				JOptionPane.showMessageDialog(null, "You were trapped!" + "\nScore: " + score);
				break;
			case "tooFarUp":
				JOptionPane.showMessageDialog(null, "You left the game!" + "\nScore: " + score);
				break;
			case "car":
				JOptionPane.showMessageDialog(null, "You got hit by a car!" + "\nScore: " + score);
				break;
			case "train":
				JOptionPane.showMessageDialog(null, "You got hit by a train!" + "\nScore: " + score);
				break;
		}

		//Show start button.
		//Start button makes new window.
		startButton.setVisible(true);
		controlsButton.setVisible(true);

		showLogo = true;
	}



	/**
	 * Moves the character one strip forward or
	 * one strip backwards WITHOUT OFF-SETTING THE
	 * LOCATION DUE TO SCROLLING.
	 */
	private void jumpHero() {

		//Holds the hero's location.
		int location;


		//Moves hero sprite smoothly by movement and not location.
		//up,down,left,right : number of iterations.
		//press : prevents over moving issue.
		if (left > 0 && press) {
			hero.setXDir(-12.5);
			left--;
			hero.setImage("Frog/Frog_Left.png");
		} else if (right > 0 && press) {
			hero.setXDir(12.5);
			right--;
			hero.setImage("Frog/Frog_Right.png");
		} else if (left == 0 && right == 0 && up == 0 && down == 0) {
			hero.setXDir(0);
			press = false;
		}


		//If up is pressed.
		if (up > 0 && press) {

			//Set hero speed.
			hero.setYDir(-10);
			hero.move();
			hero.setImage("Frog/Frog_up.png");

			//Get hero Y location.
			location = hero.getYLoc();

			//Sets the hero's location up one strip.
			for (int i = 0; i < numOfStrips; i++) {

				Sprite x = allStrips[i][0];

				//Aligns hero to strip after movement.
				if (location - x.getYLoc() > 95 && location - x.getYLoc() < 105) {

					hero.setYDir(0);
					up = 0;
					press = false;

					hero.setYLoc(x.getYLoc() + 101);

					//Increases travel keeper.
					movement++;

					i = numOfStrips;
				}
			}
		}

		//If down in pressed.
		else if (down > 0 && press) {

			//Set hero speed.
			hero.setYDir(10);
			hero.move();
			hero.setImage("Frog/Frog_Down.png");

			//Get hero location
			location = hero.getYLoc();

			//Sets the heros location down one strip.
			for (int i = 0; i < numOfStrips; i++) {

				Sprite x = allStrips[i][0];

				//Align hero to strip after movement.
				if (location - x.getYLoc() < -95 && location - x.getYLoc() > -105) {

					hero.setYDir(0);
					down = 0;
					press = false;

					hero.setYLoc(x.getYLoc() - 99);

					//location = x.getYLoc() - 100;

					//Decreases travel keeper.
					movement--;

					i = numOfStrips;
				}
			}
		}
	}

	/**
	 * Method that:
	 * Moves cars.
	 * Removes cars passing Y bounds.
	 * Checks for car collisions.
	 */
	private void manageCars() {

		//Cycles through car sprites.
		for (int i = 0; i < cars.size(); i++) {

			Sprite car = cars.get(i);

			//Moves car sprites.
			car.move();

			//Removes cars passing Y bounds.
			if (car.getYLoc() > 800)
				cars.remove(i);

			//Checks for car collisions.
			if (car.isCollision(hero) && invincibility <= 0) {

				//Method to end game.
				killMsg("car");
			}
		}
	}

	/**
	 * Method that:
	 * Moves trains.
	 * Removes trains passing Y bounds.
	 * Checks for train collisions.
	 */
	private void manageTrains() {

		//Cycles through car sprites.
		for (int i = 0; i < trains.size(); i++) {

			Sprite train = trains.get(i);

			//Moves train sprites.
			train.move();

			//Removes trains passing Y bounds.
			if (train.getYLoc() > 800)
				trains.remove(i);

			//Checks for train collisions.
			if (train.isCollision(hero) && invincibility <= 0) {

				//Method to end game.
				killMsg("train");
			}
		}

	}



	/**
	 * Method that correctly resets the strips.
	 */
	private void manageStrips() {

		//All water strip variable.
		int allWater;
		int allGrass;

		//Variable to reset horizontal strip location.
		int X = 0;

		//Cycles through each strip.
		for (int v = 0; v < numOfStrips; v++) {

			//Checks location of first strip.
			if (allStrips[v][0].getYLoc() > 800) {

				//Generates a new strip.
				allStrips[v] = stripGen.getStrip();


				//Prevents an all water or grass strip.
				do {
					//Reset variables.
					allWater = 0;
					allGrass = 0;

					//Check sprites in strip.
					for (Sprite s : allStrips[v]) {
						if (s.getFileName().equals("Misc/Water.png"))
							allWater++;
						if (s.getFileName().equals("Misc/Grass.png"))
							allGrass++;
					}

					if (allWater == 8)
						allStrips[v] = stripGen.getWaterStrip();
					if (allGrass == 8)
						allStrips[v] = stripGen.getLandStrip();


				} while (allWater == 8 || allGrass == 8);


				//If there was previously a water strip, and this strip is a water strip, match this strips lillypads to the previous strip.
				if (water > 0) {
					if (allStrips[v][0].getFileName().equals("Misc/Water.png") || allStrips[v][0].getFileName().equals("Misc/Lillypad.png")) {

						water = 0;

						for (int i : special) {
							allStrips[v][i].setImage("Misc/Lillypad.png");
						}
					}
				}

				//If there was previously a water strip, and this strip is a land strip, match the grass to the previous strips lillypads.
				if (water > 0) {
					if (allStrips[v][0].getFileName().equals("Misc/Grass.png") || allStrips[v][0].getFileName().equals("Misc/Shrub.png") ||
							allStrips[v][0].getFileName().equals("Misc/Tree_One.png") || allStrips[v][0].getFileName().equals("Misc/Tree_Two.png")) {

						allStrips[v] = stripGen.getSpecialLandStrip();

						water = 0;

						for (int i : special) {
							allStrips[v][i].setImage("Misc/Grass.png");
						}
					}
				}

				//If there was previously a land strip, and this strip is a water strip, match the lillypads to the grass.
				if (land > 0) {
					if (allStrips[v][0].getFileName().equals("Misc/Water.png") || allStrips[v][0].getFileName().equals("LilyPad.png")) {

						land = 0;

						int val = 0;

						while (val == 0) {

							allStrips[v] = stripGen.getWaterStrip();

							for (int i = 0; i < 8; i++) {
								if (allStrips[v][i].getFileName().equals("Misc/Lillypad.png")) {
									//TODO: Remove
									for (int x = 0; x < special.size(); x++) {
										if (i == special.get(x)) {
											val++;
										}
									}
								}
							}
						}

					}
				}


				//if there is a water strip, write down the index of the Lillypads.
				if (allStrips[v][0].getFileName().equals("Misc/Water.png") || allStrips[v][0].getFileName().equals("Misc/Lillypad.png")) {

					special.clear();

					water = 0;

					for (int i = 0; i < 8; i++) {
						if (allStrips[v][i].getFileName().equals("Misc/Lillypad.png")) {
							special.add(i);
							water++;
						}
					}
				} else
					water = 0;

				//if there is a land strip, write down the index of the grass.
				if (allStrips[v][0].getFileName().equals("Misc/Grass.png") || allStrips[v][0].getFileName().equals("Misc/Shrub.png") ||
						allStrips[v][0].getFileName().equals("Misc/Tree_One.png") || allStrips[v][0].getFileName().equals("Misc/Tree_Two.png")) {

					special.clear();

					land = 0;

					for (int i = 0; i < 8; i++) {
						if (allStrips[v][i].getFileName().equals("Misc/Grass.png")) {
							special.add(i);
							land++;
						}
					}
				}


				//Reset the location of the strip.
				for (int i = 0; i < 8; i++) {

					allStrips[v][i].setYLoc(-99);
					allStrips[v][i].setXLoc(X);

					X += 100;
				}

				//Method to set cars.
				setCars(v);

				//Method to set trains.
				setTrains(v);
			}
		}
	}

	/**
	 * Method that creates and resets cars on the road strip.
	 */
	private void setCars(int v) {

		//Sets car sprite.
		if (allStrips[v][0].getFileName().equals("Misc/Road.png")) {

			//Makes sprite.
			Sprite car = new Sprite();

			//Scrolls sprite.
			car.setYDir(2);

			//Set sprite to strip location.
			car.setYLoc(allStrips[v][0].getYLoc() + 10);

			if (rand.nextInt(2) == 1) {
				//Right to left.
				car.setXLoc(900);
				car.setXDir(-(rand.nextInt(10) + 10));
				car.setImage(randomCar("left"));

			} else {
				//Left to right.
				car.setXLoc(-200);
				car.setXDir((rand.nextInt(10) + 10));
				car.setImage(randomCar("right"));
			}

			//Add sprite to array.
			cars.add(car);
		}


		//Resets cars passing X bounds.
		for (Sprite s : cars) {

			if (s.getXLoc() < -200) {

				//Right to left.
				s.setXDir(-(rand.nextInt(10) + 10));

				s.setXLoc(900);

				s.setImage(randomCar("left"));
			} else if (s.getXLoc() > 900) {

				//Left to right.
				s.setXDir((rand.nextInt(10) + 10));

				s.setXLoc(-200);

				s.setImage(randomCar("right"));
			}
		}
	}

	/**
	 * Method to return random car color.
	 */
	private String randomCar(String dir) {

		//Car color variables.
		int carColor = rand.nextInt(8);
		String carImage = "";

		if (dir.equals("left")) {

			switch (carColor) {
				case 0:
					carImage = "/Car_Left/Car_Left_Blue.png";
					break;
				case 1:
					carImage = "/Car_Left/Car_Left_Green.png";
					break;
				case 2:
					carImage = "/Car_Left/Car_Left_Grey.png";
					break;
				case 3:
					carImage = "/Car_Left/Car_Left_Orange.png";
					break;
				case 4:
					carImage = "/Car_Left/Car_Left_Purple.png";
					break;
				case 5:
					carImage = "/Car_Left/Car_Left_Red.png";
					break;
				case 6:
					carImage = "/Car_Left/Car_Left_White.png";
					break;
				case 7:
					carImage = "/Car_Left/Car_Left_Yellow.png";
					break;
			}
		}

		if (dir.equals("right")) {

			switch (carColor) {
				case 0:
					carImage = "/Car_Right/Car_Right_Blue.png";
					break;
				case 1:
					carImage = "/Car_Right/Car_Right_Green.png";
					break;
				case 2:
					carImage = "/Car_Right/Car_Right_Grey.png";
					break;
				case 3:
					carImage = "/Car_Right/Car_Right_Orange.png";
					break;
				case 4:
					carImage = "/Car_Right/Car_Right_Purple.png";
					break;
				case 5:
					carImage = "/Car_Right/Car_Right_Red.png";
					break;
				case 6:
					carImage = "/Car_Right/Car_Right_White.png";
					break;
				case 7:
					carImage = "/Car_Right/Car_Right_Yellow.png";
					break;
			}
		}

		return carImage;
	}

	/**
	 * Method that creates and resets trains on the track strip.
	 */
	private void setTrains(int v) {

		//Sets train sprite.
		if (allStrips[v][0].getFileName().equals("Misc/Tracks.png")) {

			//Makes sprite.
			Sprite train = new Sprite(randomTrain());

			//Scrolls sprite.
			train.setYDir(2);

			//Set sprite to strip location.
			train.setYLoc(allStrips[v][0].getYLoc() + 10);

			if (rand.nextInt(2) == 1) {
				//Right to left.
				train.setXLoc(900);
				train.setXDir(-(rand.nextInt(10) + 30));
			} else {
				//Left to right.
				train.setXLoc(-1500);
				train.setXDir((rand.nextInt(10) + 30));
			}

			//Add sprite to array.
			trains.add(train);
		}


		//Resets trains passing X bounds.
		for (Sprite s : trains) {

			//Method to change train color.
			if (s.getXLoc() < -(rand.nextInt(2000) + 1300)) {
				s.setXDir(-(rand.nextInt(10) + 30));

				s.setXLoc(900);

				s.setImage(randomTrain());
			} else if (s.getXLoc() > rand.nextInt(2000) + 900) {
				s.setXDir((rand.nextInt(10) + 30));

				s.setXLoc(-1500);

				s.setImage(randomTrain());
			}
		}
	}

	/**
	 * Method to return a random colored train.
	 */
	private String randomTrain() {

		int trainNum = rand.nextInt(10);
		String trainImage = "";


		switch (trainNum) {
			case 0:
				trainImage = "/Trains/Train_Blue.png";
				break;
			case 1:
				trainImage = "/Trains/Train_Green.png";
				break;
			case 2:
				trainImage = "/Trains/Train_Grey.png";
				break;
			case 3:
				trainImage = "/Trains/Train_Orange.png";
				break;
			case 4:
				trainImage = "/Trains/Train_Purple.png";
				break;
			case 5:
				trainImage = "/Trains/Train_Red.png";
				break;
			case 6:
				trainImage = "/Trains/Train_White.png";
				break;
			case 7:
				trainImage = "/Trains/Train_Yellow.png";
				break;
		}

		return trainImage;
	}

	/**
	 * Scrolls the strips and the hero.
	 */
	private void scrollScreen() {

		//Cycles through strip array.
		for (int v = 0; v < numOfStrips; v++) {
			for (int x = 0; x < 8; x++) {
				allStrips[v][x].setYDir(2);
			}
		}
		//Sets scrolling if hero is not moving.
		if (!press) {
			hero.setYDir(2);
		}
	}







	/**
	 * Draws graphics onto screen.
	 */
	public void paintComponent(Graphics g) {

		//Erases the previous screen.
		super.paintComponent(g);

		//Draws strips.
		for (int i = 0; i < numOfStrips; i++) {
			for (int x = 0; x < 8; x++) {
				allStrips[i][x].paint(g, this);
			}
		}

		//Draw hero sprite.
		hero.paint(g, this);

		//Draw car sprites.
		for (Sprite s : cars)
			s.paint(g, this);

		//Draw train sprites.
		for (Sprite s : trains)
			s.paint(g, this);

		//Set the font size and color.
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 3f);
		g.setFont(newFont);
		g.setColor(Color.green);

		//Draws the high score on the screen.
		g.drawString("Top: " + scoreManager.readScore(), 50, 50);


		//Set the font size and color.
		Font cF = g.getFont();
		Font nF = cF.deriveFont(cF.getSize() * 3f);
		g.setFont(nF);
		g.setColor(Color.yellow);

		//Draws the score on the screen.
		g.drawString("" + score, 50, 150);


		//Set the font size and color.
		Font CF = g.getFont();
		Font NF = CF.deriveFont(CF.getSize() * 1f);
		g.setFont(NF);
		g.setColor(Color.red);

		//Draws invincibility status.
		if (invincibility > 0)
			g.drawString("" + invTimeLeft, 350, 350);


		//Draws logo on screen.
		if (showLogo) {
			logo.setXLoc(175);
			logo.setYLoc(75);
			logo.paint(g, this);
		}
	}


	/**
	 * Reads keyboard input for moving
	 * when key is pressed down.
	 */
	private class KeyPressing extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {

				//Moves hero within left and right bounds.
				case KeyEvent.VK_RIGHT:
					if (!press && hero.getXLoc() < 695) {
						right = 8;
						press = true;
					}
					break;
				case KeyEvent.VK_LEFT:
					if (!press && hero.getXLoc() > 0) {
						left = 8;
						press = true;
					}
					break;
				case KeyEvent.VK_UP:
					if (!press) {
						up = 10;
						press = true;
					}
					break;
				case KeyEvent.VK_DOWN:
					if (!press) {
						down = 10;
						press = true;
					}
					break;
				case KeyEvent.VK_CONTROL:
					if (invincibility == 0)
						invincibility++;
					break;
				case KeyEvent.VK_SHIFT:
					if (gameLoop.isRunning())
						gameLoop.stop();
					else
						gameLoop.start();
					break;
				case KeyEvent.VK_ENTER:
					if (!gameLoop.isRunning()) {
						newGame = true;
						newGame();
					}
					break;
			}
		}

		/**
		 * Reads keyboard for input for stopping
		 * when key is not pressed down.
		 */
		public void keyReleased(KeyEvent e) {

			switch (e.getKeyCode()) {

				case KeyEvent.VK_RIGHT:
					hero.setXDir(0);
					break;
				case KeyEvent.VK_LEFT:
					hero.setXDir(0);
					break;
				case KeyEvent.VK_UP:
					hero.setYDir(2);
					break;
				case KeyEvent.VK_DOWN:
					hero.setYDir(2);
					break;
			}
		}
	}
//////////
}
