//Class for JFrame extension.

import javax.swing.*;


/*/
 * Window that holds the Display JPanel.
 */
//@SuppressWarnings("serial")
public class Window extends JFrame {

	//Variable for final JFrame size.
	public final int HEIGHT = 800;
	public final int WIDTH = 800;


	/**
	 * Default constructor.
	 */
	public Window(boolean pause) {

		//Set the title.
		setTitle("Frogger Remix");

		//Set the size of the JFrame.
		setSize(WIDTH, HEIGHT);

		//Set window to screen center.
		setLocationRelativeTo(null);

		//Specify the close button action.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//set resize.
		setResizable(false);

		//Add panel to frame.
		add(new Display(pause));

		//Display the window.
		setVisible(true);
	}


	/**
	 * Main constructor to start program.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Pause game if first run.
		final boolean pause = true;

		//Create window for game.
		new Window(pause);
	}
}
