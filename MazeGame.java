/* Name of Program: MazeGame.java
 * Purpose of Program: Create a Maze game
 * Author of Program: B. Yacoob
 */

// Import of packages
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

//Superclass that is a child of JFrame to inherit its methods and properties
public class MazeGame extends JFrame {
	// Declaration of variables
	private JPanel gameBorderPanel, mazePanel, bottomBtnsPanel; // Panels
	private JButton newGameButton, restartGameButton, undoGameButton, exitGameButton; // Buttons
	private JButton[][] mazePathBtns = new JButton[ARRAY_SIZE][ARRAY_SIZE]; // An array of buttons for panel
	public int numOfTilesClicked = 0, numOfUndos = 0, swapMaze = 0, lastClickedRow, lastClickedColumn; // Keep track of to output at the end of game
	private Color currentPos = Color.BLUE; // Keeps track of where player is
	private String playerNameResult = playerName(); // Gets player username from input
	private Stack<JButton> stackMazeBtns = new Stack<JButton>();// Stack that will be used to push clicked tiles into its list
	private Stack<Integer> rowStackPosition = new Stack<Integer>();
	private Stack<Integer> columnStackPosition = new Stack<Integer>();
	private static final int WINDOW_WIDTH = 400; // Window width
	private static final int WINDOW_HEIGHT = 400; // Window height
	private static final int ARRAY_SIZE = 12; // Because the array size is not going to change, we can set it as a constant

	private final static int[][] mazeOne = { // Creating the predefined 2D array for first maze
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 2, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1 }, { 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 3 }, { 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 }, { 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 }, { 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	private final static int[][] mazeTwo = { // Creating the predefined 2D array for second maze
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1 }, { 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1 }, { 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1 }, { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1 },
			{ 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1 }, { 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1 },
			{ 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1 }, { 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	private final static int[][] mazeThree = { // Creating the predefined 2D array for third maze
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1 },
			{ 3, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1 },
			{ 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1 }, { 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1 }, { 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1 },
			{ 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, { 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 } };

	// Constructor
	public MazeGame() {
		// Setting the title
		setTitle("Maze Game");

		// Setting size of the window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// What happens when close button is clicked
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// What the smaller panels will be added on top of
		gameBorderPanel = new JPanel();

		gameBorderPanel.setLayout(new BorderLayout());

		// Building the panel and adding it to the main frame
		centerGridPanel(); // Center
		bottomGridPanel(); // Bottom

		add(gameBorderPanel); // This adds the panel to the content frame

		// Visibility is shown for the window
		setVisible(true);
	}

	// UI to find player's name
	private String playerName() {
		return JOptionPane.showInputDialog("Hello Player! Enter a username:");
	}

	private void centerGridPanel() {
		mazePanel = new JPanel();
		mazePanel.setLayout(new GridLayout(12, 12)); // Match to number of buttons

		for (int a = 0; a < ARRAY_SIZE; a++) { // Loops to create the 144 buttons and add them to panel
			for (int b = 0; b < ARRAY_SIZE; b++) {
				mazePathBtns[a][b] = new JButton();

				switch (swapMaze) { // What determines the maze grid that player gets
				case 0:
					mazePathBtns[a][b].setName("" + mazeOne[a][b]); // Each button holds a spot for the predefined maze path
					break;
				case 1:
					clearMazeContent(); // Clear the grid content so there is no overlap
					mazePathBtns[a][b].setName("" + mazeTwo[a][b]); // Each button holds a spot for the predefined maze path
					break;
				case 2:
					clearMazeContent(); // Clear the grid content so there is no overlap
					mazePathBtns[a][b].setName("" + mazeThree[a][b]); // Each button holds a spot for the predefined path
					break;
				default:
					swapMaze = 0; // Reset the counter back to 0 so grids can be played through again
					mazePathBtns[a][b].setName("" + mazeOne[a][b]);
					break;
				}

				// Add the buttons to the panel
				mazePanel.add(mazePathBtns[a][b]);

				if (mazePathBtns[a][b].getName().equals("0")) { // What buttons are free for the player to take
					mazePathBtns[a][b].setBackground(Color.WHITE);
				} else if (mazePathBtns[a][b].getName().equals("1")) { // What buttons are restricted to the player
					mazePathBtns[a][b].setBackground(Color.RED);
					mazePathBtns[a][b].setEnabled(false); // Cannot change blocked walls
				} else if (mazePathBtns[a][b].getName().equals("2")) { // Start position of maze path
					mazePathBtns[a][b].setBackground(Color.YELLOW);
					mazePathBtns[a][b].setEnabled(false); // Cannot change start path
					rowStackPosition.push(a);
					columnStackPosition.push(b);
				} else if (mazePathBtns[a][b].getName().equals("3")) { // End position of maze path
					mazePathBtns[a][b].setBackground(Color.BLACK);
				}

				final int tempA = a, tempB = b; // To pass as parameters for the method below

				// Register an event listener with the 144 buttons
				mazePathBtns[a][b].addActionListener(new ActionListener() {
					// When button is clicked
					public void actionPerformed(ActionEvent e) { // Waits for the action
						JButton buttonClicked = (JButton) e.getSource();
						buttonClicked.setName("" + 0); // Setting to 0 so that whites can turn into blue
						checkForMazeWin(buttonClicked, tempA, tempB); // Checking if the player gets the game-winning
																		// path for the maze(s)
					}
				});
			}
		}

		// Adding all of these components to the panel
		add(mazePanel);

		gameBorderPanel.add(mazePanel, BorderLayout.CENTER); // Adding to the main panel
	}

	// What clears the JButton "tag" that holds each # so that grids do not overlap
	private void clearMazeContent() {
		for (int a = 0; a < ARRAY_SIZE; a++) {
			for (int b = 0; b < ARRAY_SIZE; b++) {
				mazePathBtns[a][b].setName(""); // Each button holds a spot
			}
		}
	}

	// What checks the game-winning path for all of the three maze grids
	private void checkForMazeWin(JButton buttonClick, int a, int b) {
		if (buttonClick.getBackground() == Color.BLACK) {	// If black tile is pressed at any moment, ignore it
			return;
		}

		// Solve problem of tiles that are not relative to the current position
		// A loop that iterates over the mazePathBtns[a][b].getBackground()
		// and finds if left, right, up, down matches a designated color
		if (mazePathBtns[a - 1][b].getBackground() == Color.YELLOW
				|| mazePathBtns[a - 1][b].getBackground() == currentPos
				|| mazePathBtns[a][b - 1].getBackground() == Color.YELLOW
				|| mazePathBtns[a][b - 1].getBackground() == currentPos
				|| mazePathBtns[a][b + 1].getBackground() == Color.YELLOW
				|| mazePathBtns[a][b + 1].getBackground() == currentPos
				|| mazePathBtns[a + 1][b].getBackground() == Color.YELLOW
				|| mazePathBtns[a + 1][b].getBackground() == currentPos) { // Up, Left, Right, Down
			if ((Math.abs((a - rowStackPosition.peek())) + Math.abs((b - columnStackPosition.peek())) <= 1)
					|| (mazePathBtns[a - 1][b].getBackground() == Color.YELLOW
							|| mazePathBtns[a][b - 1].getBackground() == Color.YELLOW
							|| mazePathBtns[a][b + 1].getBackground() == Color.YELLOW
							|| mazePathBtns[a + 1][b].getBackground() == Color.YELLOW)) {
				lastClickedRow = a;
				lastClickedColumn = b;
				mazePathBtns[a][b].setBackground(currentPos);
				mazePathBtns[a][b].setEnabled(false); // Does not allow player to press same tile more than once
				numOfTilesClicked++; // Increment each time a button is clicked on the maze panel
				stackMazeBtns.push(buttonClick); // Pushing each button that is pressed into the stack
				rowStackPosition.push(a);
				columnStackPosition.push(b);
			}
			
			if (((swapMaze == 0 && mazePathBtns[a][b + 1].getBackground() == Color.BLACK)
					|| swapMaze == 1 && mazePathBtns[a + 1][b].getBackground() == Color.BLACK)
					|| swapMaze == 2 && mazePathBtns[a][b - 1].getBackground() == Color.BLACK) {
				declareWinner(); // Once the player reaches the end of the maze and can click on the black tile
									// that means they have won
			}
		}
	}

	// Declare player as winner of the game
	private void declareWinner() {
		// What is shown once the player finds the game-winning maze path
		JOptionPane.showMessageDialog(null,
				"Congratulations ".concat(playerNameResult)
						.concat(" on finding a game-winning path! It took you " + numOfTilesClicked
								+ " clicks to find it (" + Math.addExact(numOfTilesClicked, numOfUndos)
								+ " clicks with undo used). You get to play a new round!")); // Display
		createNewGame();
	}

	// Function that creates the panel for the bottom portion of the GUI
	private void bottomGridPanel() {
		bottomBtnsPanel = new JPanel();
		bottomBtnsPanel.setLayout(new GridLayout(1, 4)); // Four buttons will be given to player

		// Buttons with certain captions
		newGameButton = new JButton("New Game");
		restartGameButton = new JButton("Restart");
		undoGameButton = new JButton("Undo");
		exitGameButton = new JButton("Exit");

		// Register an event listener with the four game buttons
		newGameButton.addActionListener(new bottomPanelBtnListener());
		restartGameButton.addActionListener(new bottomPanelBtnListener());
		undoGameButton.addActionListener(new bottomPanelBtnListener());
		exitGameButton.addActionListener(new bottomPanelBtnListener());

		bottomBtnsPanel.add(newGameButton);
		bottomBtnsPanel.add(restartGameButton);
		bottomBtnsPanel.add(undoGameButton);
		bottomBtnsPanel.add(exitGameButton);

		gameBorderPanel.add(bottomBtnsPanel, BorderLayout.SOUTH);
	}

	// Necessary things to execute if "Restart" is performed
	private void restartMaze() {
		mazePanel.removeAll(); // Remove UI components
		centerGridPanel(); // Rebuild with a new panel
		gameBorderPanel.revalidate(); // Reset the layout container after new container list
		numOfTilesClicked = 0; // Reset counter after a new game is started
		numOfUndos = 0; // Reset undo counter to zero if game is reset
		stackMazeBtns.clear(); // Make sure stack is empty if player restarts the game or plays a new one
	}

	// Necessary things to execute if "New Game" is performed
	private void createNewGame() {
		swapMaze++; // Increment so that player can play on a new maze
		mazePanel.removeAll(); // Remove UI components
		centerGridPanel(); // Rebuild with a new panel
		gameBorderPanel.revalidate(); // Reset the layout container after new container list
		numOfTilesClicked = 0; // Reset counter after a new game is started
		numOfUndos = 0; // Reset undo counter to zero if a new game is started
		stackMazeBtns.clear(); // Make sure stack is empty if player restarts the game or plays a new one
	}

	// Player goes back to previous clicked button(s) if "Undo" is pressed
	private void undoMazePath() {
		// What prevents player from pressing undo when there are no blue tiles present
		if (!stackMazeBtns.isEmpty()) {
			stackMazeBtns.peek().setBackground(Color.WHITE); // Without removing top element, set the JButton clicked to
																// default white tile
			stackMazeBtns.peek().setEnabled(true); // Player can press tile again after popping
			stackMazeBtns.pop(); // Popping the last pushed JButton element that was in the stack
			rowStackPosition.pop();
			columnStackPosition.pop();
			numOfUndos++; // Increment each time the player pops from the stack
			numOfTilesClicked--; // Popping a JButton tile means one less button clicked
		}
	}

	// What class implements the action listener for each button clicked
	private class bottomPanelBtnListener implements ActionListener {
		// When button is clicked
		public void actionPerformed(ActionEvent e) { // Waits for the action
			String buttonClicked = e.getActionCommand();

			// Searching for String that equals to what button player chose
			switch (buttonClicked) {
			case "New Game": // Creating a new game would mean a new player and new maze path
				JOptionPane.showMessageDialog(null,
						"You pressed a total of " + numOfTilesClicked + " tiles ("
								+ Math.addExact(numOfTilesClicked, numOfUndos)
								+ " clicks with undo used). A new game is now created!");
				playerNameResult = playerName();
				createNewGame();
				break;
			case "Restart": // Resets the maze path to its original state
				JOptionPane.showMessageDialog(null,
						"You pressed a total of " + numOfTilesClicked + " tiles ("
								+ Math.addExact(numOfTilesClicked, numOfUndos)
								+ " clicks with undo used). The maze board is now reset!");
				restartMaze();
				break;
			case "Undo": // Allowing the player to go back to their desired path spot
				undoMazePath();
				break;
			case "Exit":
				JOptionPane.showMessageDialog(null,
						"You pressed a total of " + numOfTilesClicked + " tiles ("
								+ Math.addExact(numOfTilesClicked, numOfUndos)
								+ " clicks with undo used). Thanks for playing!");
				System.exit(0); // Close program
				break;
			default:
				break;
			}
		}
	}

	public static void main(String[] args) {
		new MazeGame(); // Calling the class in main
	}
}