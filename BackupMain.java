import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public class BackupMain extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static int max = 9;
	private int i;
	private int j;
	private static int difficulty = 50, checkRow, checkCol, checkSq, iCheck, jCheck, diffWin, score;
	private int win = 0;
	private int diffcount;
	private boolean found[][] = new boolean[max][max];
	private static JTextField[][] tfPuzzle = new JTextField[max][max];
	private static int[][] puzzle = new int[max][max];
	private static int[][] initialPuzzle = new int[max][max];
	public static final Font fontNum = new Font("Verdana", Font.BOLD, 40);
	public static Color colorIniText = new Color(65, 105, 225);
	public static Color colorIniBack = null;
	public static Color colorSetBack = Color.DARK_GRAY;
	public static Color colorSetText = Color.green;
	public static Color colorConflictBack = Color.red;
	public static Color colorMenuBack = new Color(49, 79, 79);
	public static Color colorMenuText = new Color(240, 255, 240);

	public static JLabel statusLabel;
	public static JLabel timeLabel;
	public int sec = 0, min = 0, hour = 0;

	public static int[][] playerInput = new int[max][max];
	public static boolean[][] correctBoard = new boolean[max][max];
	public static boolean[][] rowCon = new boolean[max][max];
	public static boolean[][] colCon = new boolean[max][max];
	public static boolean[][] sqCon = new boolean[max][max];

	JMenuBar menubar = new JMenuBar();
	static ActionListener timeListener;
	public static Timer timer, themeTime;

	Container cp = getContentPane();

	public JMenuBar menuBar;
	public JMenu menu;

	public BackupMain() {

		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		setTitle("BackupMain");
		setSize(600, 745);

		createMenuBar();

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				c.gridy = i;
				c.gridx = j;
				c.ipadx = 60;
				c.ipady = 20;

				tfPuzzle[i][j] = new JTextField(10);
				tfPuzzle[i][j].setHorizontalAlignment(JTextField.CENTER);
				tfPuzzle[i][j].setEditable(false);
				tfPuzzle[i][j].setBackground(colorIniBack);
				tfPuzzle[i][j].setForeground(colorIniText);
				cp.add(tfPuzzle[i][j], c);
				tfPuzzle[i][j].addKeyListener(this);
				tfPuzzle[i][j].setFont(fontNum);
				tfPuzzle[i][j].setText(null);
				tfPuzzle[i][j].setDisabledTextColor(colorSetText);
				tfPuzzle[i][j].setBorder(BorderFactory.createLineBorder(new Color(139, 137, 137), 2));
				initialPuzzle[i][j] = 0;
			}
		}

		c.gridy = 9;
		c.gridx = 0;
		c.gridwidth = 5;
		c.ipady = 5;
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(cp.getWidth(), 5));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Number of cells remaining: " + Integer.toString(81 - difficulty));
		statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		statusPanel.setBackground(colorMenuBack);
		statusLabel.setForeground(colorMenuText);
		statusPanel.add(statusLabel);
		cp.add(statusPanel, c);

		c.gridy = 9;
		c.gridx = 5;
		c.gridwidth = 4;
		c.ipady = 4;
		JPanel timePanel = new JPanel();
		timePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		timePanel.setPreferredSize(new Dimension(cp.getWidth(), 5));
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		timeLabel = new JLabel("");
		timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		timePanel.setBackground(colorMenuBack);
		timeLabel.setForeground(colorMenuText);
		timePanel.add(timeLabel);
		cp.add(timePanel, c);
		timer();

		printPuzzle();
		cp.setVisible(true);

		SoundEffect.init();
		SoundEffect.Background.play();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BackupMain();
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		JTextField location = (JTextField) evt.getSource();

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				if (location == tfPuzzle[i][j]) {
					score++;
					tfPuzzle[i][j].setText(null);
					if ((evt.getKeyChar() >= '1') && (evt.getKeyChar() <= '9')) {
						playerInput[i][j] = ((int) (evt.getKeyChar() - '0'));
						check();
					} else {
						playerInput[i][j] = 0;

					}
				}
			}
		}
		conflict();
		setStatus();
		gameWin();
	}

	@Override
	public void keyPressed(KeyEvent evt) {

		int iCon, jCon;

		for (iCon = 0; iCon < max; iCon++) {
			for (jCon = 0; jCon < max; jCon++) {
				if ((JTextField) evt.getSource() == tfPuzzle[iCon][jCon]) {
					if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						rowCon[iCon][jCon] = false;
						colCon[iCon][jCon] = false;
						sqCon[iCon][jCon] = false;
						playerInput[iCon][jCon] = 0;
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {

	}

	private void printPuzzle() {

		diffcount = 0;
		score = 0;
		resetPuzzle();

		puzzle = SudokuGenerator.Generate();

		diffWin = 81 - difficulty;
		statusLabel.setText("Number of cells remaining: " + Integer.toString(diffWin));

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				found[i][j] = false;
				correctBoard[i][j] = false;

				initialPuzzle[i][j] = 0;
				tfPuzzle[i][j].setText(null);
			}
		}

		initialPuzzle = new int[max][max];

		System.out.println();
		System.out.println();
		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.println("");
		}

		for (int iRand = (int) (Math.random() * 9); diffcount < difficulty;) {
			for (int jRand = (int) (Math.random() * 9); diffcount < difficulty;) {
				if (found[iRand][jRand] == false) {
					diffcount++;
					tfPuzzle[iRand][jRand].setText(Integer.toString(puzzle[iRand][jRand]));
					tfPuzzle[iRand][jRand].setDisabledTextColor(colorSetText);
					tfPuzzle[iRand][jRand].setBackground(colorSetBack);
					tfPuzzle[iRand][jRand].setEnabled(false);
					initialPuzzle[iRand][jRand] = puzzle[iRand][jRand];
					found[iRand][jRand] = true;
					correctBoard[iRand][jRand] = true;
				}
				iRand = (int) (Math.random() * 9);
				jRand = (int) (Math.random() * 9);
			}
		}

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				if (found[i][j] == true) {
					win++;
				}
			}
		}
	}

	private void setStatus() {
		diffWin = 81;
		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				if (tfPuzzle[i][j].isEnabled() == false) {
					diffWin--;
					statusLabel.setText("Number of cells remaining: " + Integer.toString(diffWin));
				}
			}
		}
	}

	private void gameWin() {
		String[] options = new String[2];
		options[0] = "Restart";
		options[1] = "Quit";

		win = 0;

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				if (correctBoard[i][j] == true) {
					win++;
				}
			}
		}

		if (win == 81) {
			timer.stop();
			SoundEffect.Background.stop();
			SoundEffect.volume(-10.0f, SoundEffect.Win.clip);
			SoundEffect.Win.play();
			int result = JOptionPane.showOptionDialog(null, "Congratulations! You took " + score + " moves!",
					"YOU WON!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
			if (result == 1) {
				System.exit(0);
			}
			if (result == 0) {
				SoundEffect.Win.stop();
				resetPuzzle();
				printPuzzle();
				SoundEffect.Background.play();
				score = 0;
				sec = 0;
				min = 0;
				hour = 0;
				timer.start();
			}
		}
	}

	private void resetPuzzle() {
		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {

				tfPuzzle[i][j].setEnabled(true);
				tfPuzzle[i][j].setEditable(true);
				tfPuzzle[i][j].setForeground(colorIniText);
				tfPuzzle[i][j].setBackground(colorIniBack);
				found[i][j] = false;
				playerInput[i][j] = 0;
			}
		}
		score = 0;
		sec = 0;
		min = 0;
		hour = 0;
	}

	private void createMenuBar() {

		JMenu Game = new JMenu("Game");

		menubar.setBackground(colorMenuBack);

		Game.setForeground(colorMenuText);

		JMenuItem newGame = new JMenuItem("New Game");
		newGame.setToolTipText("Start a New Game");
		newGame.addActionListener((ActionEvent event) -> {
			printPuzzle();
		});
		Game.add(newGame);

		JMenuItem resetGame = new JMenuItem("Reset Game");
		resetGame.setToolTipText("Reset Current Game");
		resetGame.addActionListener((ActionEvent event) -> {
			resetPuzzle();
			int iReset, jReset;

			for (iReset = 0; iReset < max; iReset++) {
				for (jReset = 0; jReset < max; jReset++) {

					if (initialPuzzle[iReset][jReset] == 0) {
						found[iReset][jReset] = false;
						correctBoard[iReset][jReset] = false;
						playerInput[iReset][jReset] = 0;
						tfPuzzle[iReset][jReset].setText(null);
					}
					if (initialPuzzle[iReset][jReset] != 0) {
						tfPuzzle[iReset][jReset].setText(Integer.toString(initialPuzzle[iReset][jReset]));
						tfPuzzle[iReset][jReset].setEnabled(false);
						tfPuzzle[iReset][jReset].setForeground(colorIniText);
						tfPuzzle[iReset][jReset].setBackground(colorSetBack);
						found[iReset][jReset] = true;
					}
				}
			}
			diffWin = 81 - difficulty;
			statusLabel.setText("Number of cells remaining: " + Integer.toString(diffWin));
		});

		Game.add(resetGame);

		JMenuItem cheatMenu = new JMenuItem("Hint");
		cheatMenu.setToolTipText("Reveal 1 tile, increases your move count by 10");
		cheatMenu.addActionListener((ActionEvent event) -> {
			score += 10;
			for (i = (int) (Math.random() * 9);;) {
				for (j = (int) (Math.random() * 9);;) {
					if (tfPuzzle[i][j].getBackground() != colorSetBack) {
						diffcount++;
						tfPuzzle[i][j].setText(null);
						tfPuzzle[i][j].setText(Integer.toString(puzzle[i][j]));
						tfPuzzle[i][j].setDisabledTextColor(colorSetText);
						tfPuzzle[i][j].setBackground(colorSetBack);
						tfPuzzle[i][j].setEnabled(false);
						correctBoard[i][j] = true;
						playerInput[i][j] = puzzle[i][j];
						break;
					}
					i = (int) (Math.random() * 9);
					j = (int) (Math.random() * 9);
				}
				break;
			}
			check();
			conflict();
			setStatus();
			gameWin();
		});
		Game.add(cheatMenu);

		JMenuItem exitMenu = new JMenuItem("Exit");
		exitMenu.setToolTipText("Exit application");
		exitMenu.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		Game.add(exitMenu);

		menubar.add(Game);

		JMenu DiffMenu = new JMenu("Difficulty");
		DiffMenu.setForeground(colorMenuText);

		JMenuItem Easy = new JMenuItem("Easy");
		Easy.setToolTipText("Set difficulty to Easy");
		Easy.addActionListener((ActionEvent event) -> {
			difficulty = 50;
			printPuzzle();
		});
		DiffMenu.add(Easy);

		JMenuItem Med = new JMenuItem("Medium");
		Med.setToolTipText("Set difficulty to Medium");
		Med.addActionListener((ActionEvent event) -> {
			difficulty = 40;
			printPuzzle();
		});
		DiffMenu.add(Med);

		JMenuItem Diffi = new JMenuItem("Difficult");
		Diffi.setToolTipText("Set difficulty to Difficult");
		Diffi.addActionListener((ActionEvent event) -> {
			difficulty = 35;
			printPuzzle();
		});
		DiffMenu.add(Diffi);

		JMenuItem VDiff = new JMenuItem("Very Difficult");
		VDiff.setToolTipText("Set difficulty to Very Difficult");
		VDiff.addActionListener((ActionEvent event) -> {
			difficulty = 30;
			printPuzzle();
		});
		DiffMenu.add(VDiff);

		JMenuItem Test = new JMenuItem("Test");
		Test.setToolTipText("Test Difficulty");
		Test.addActionListener((ActionEvent event) -> {
			difficulty = 80;
			printPuzzle();
		});
		DiffMenu.add(Test);

		menubar.add(DiffMenu);
		setJMenuBar(menubar);
		themeMenu();
		volMenu();
	}

	private void check() {

		checkRow = 0;
		checkCol = 0;
		checkSq = 0;

		for (iCheck = 0; iCheck < max; iCheck++) {
			for (jCheck = 0; jCheck < max; jCheck++) {
				if (initialPuzzle[iCheck][jCheck] == 0) {
					found[iCheck][jCheck] = false;
				}
			}
		}

		for (iCheck = i, jCheck = 0; jCheck < max; jCheck++) {

			if (playerInput[iCheck][jCheck] == puzzle[iCheck][jCheck]) {
				found[iCheck][jCheck] = true;
			}

			if (found[iCheck][jCheck] == true) {
				checkRow++;
			}
		}

		for (jCheck = j, iCheck = 0; iCheck < max; iCheck++) {
			if (playerInput[iCheck][jCheck] == puzzle[iCheck][jCheck]) {
				found[iCheck][jCheck] = true;
			}
			if (found[iCheck][jCheck] == true) {
				checkCol++;
			}
		}

		for (iCheck = (i / 3) * 3; iCheck <= ((i / 3) * 3) + 2; iCheck++) {
			for (jCheck = (j / 3) * 3; jCheck <= ((j / 3) * 3) + 2; jCheck++) {
				if (playerInput[iCheck][jCheck] == puzzle[iCheck][jCheck]) {
					found[iCheck][jCheck] = true;
				}
				if (found[iCheck][jCheck] == true) {
					checkSq++;
				}
			}
		}

		solidify();
	}

	private void solidify() {

		if (checkRow == 9) {
			for (iCheck = i, jCheck = 0; jCheck < max; jCheck++) {
				tfPuzzle[iCheck][jCheck].setEnabled(false);
				tfPuzzle[iCheck][jCheck].setText(null);
				tfPuzzle[iCheck][jCheck].setText(Integer.toString(puzzle[iCheck][jCheck]));
				tfPuzzle[iCheck][jCheck].setForeground(colorIniText);
				tfPuzzle[iCheck][jCheck].setBackground(colorSetBack);
				correctBoard[iCheck][jCheck] = true;
			}
		}

		if (checkCol == 9) {
			for (jCheck = j, iCheck = 0; iCheck < max; iCheck++) {
				tfPuzzle[iCheck][jCheck].setEnabled(false);
				tfPuzzle[iCheck][jCheck].setText(null);
				tfPuzzle[iCheck][jCheck].setText(Integer.toString(puzzle[iCheck][jCheck]));
				tfPuzzle[iCheck][jCheck].setForeground(colorIniText);
				tfPuzzle[iCheck][jCheck].setBackground(colorSetBack);
				correctBoard[iCheck][jCheck] = true;
			}
		}

		if (checkSq == 9) {
			for (iCheck = (i / 3) * 3; iCheck <= ((i / 3) * 3) + 2; iCheck++) {
				for (jCheck = (j / 3) * 3; jCheck <= ((j / 3) * 3) + 2; jCheck++) {
					tfPuzzle[iCheck][jCheck].setEnabled(false);
					tfPuzzle[iCheck][jCheck].setText(null);
					tfPuzzle[iCheck][jCheck].setText(Integer.toString(puzzle[iCheck][jCheck]));
					tfPuzzle[iCheck][jCheck].setForeground(colorIniText);
					tfPuzzle[iCheck][jCheck].setBackground(colorSetBack);
					correctBoard[iCheck][jCheck] = true;
				}
			}
		}
	}

	private void conflict() {
		int iCon, jCon, numCon;

		for (iCon = 0; iCon < max; iCon++) {
			for (jCon = 0; jCon < max; jCon++) {
				if (correctBoard[iCon][jCon] == true) {
					tfPuzzle[iCon][jCon].setBackground(colorSetBack);
				} else {
					tfPuzzle[iCon][jCon].setBackground(colorIniBack);
				}
			}
		}

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				numCon = playerInput[i][j];

				for (iCon = i, jCon = 0; jCon < max; jCon++) {
					if (jCon != j) {
						if (initialPuzzle[iCon][jCon] != 0) {
							if ((numCon == initialPuzzle[iCon][jCon])) {
								tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
								tfPuzzle[i][j].setBackground(colorConflictBack);
							}
						}
						if (playerInput[iCon][jCon] != 0) {
							if (numCon == playerInput[iCon][jCon]) {
								tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
								tfPuzzle[i][j].setBackground(colorConflictBack);
							}
						}
					}
				}

				for (iCon = 0, jCon = j; iCon < max; iCon++) {
					if (iCon != i) {
						if (initialPuzzle[iCon][jCon] != 0) {
							if ((numCon == initialPuzzle[iCon][jCon])) {
								tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
								tfPuzzle[i][j].setBackground(colorConflictBack);
							}
						}
						if (playerInput[iCon][jCon] != 0) {
							if (numCon == playerInput[iCon][jCon]) {
								tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
								tfPuzzle[i][j].setBackground(colorConflictBack);
							}
						}
					}
				}

				for (iCon = (i / 3) * 3; iCon <= ((i / 3) * 3) + 2; iCon++) {
					for (jCon = (j / 3) * 3; jCon <= ((j / 3) * 3) + 2; jCon++) {
						if ((iCon != i) && (jCon != j)) {
							if (initialPuzzle[iCon][jCon] != 0) {
								if ((numCon == initialPuzzle[iCon][jCon])) {
									tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
									tfPuzzle[i][j].setBackground(colorConflictBack);
								}
							}
							if (playerInput[iCon][jCon] != 0) {
								if (numCon == playerInput[iCon][jCon]) {
									tfPuzzle[iCon][jCon].setBackground(colorConflictBack);
									tfPuzzle[i][j].setBackground(colorConflictBack);
								}
							}
						}
					}
				}
			}
		}
	}

	private void timer() {

		ActionListener timeListener = new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent event) {
				sec++;
				if (sec == 60) {
					min++;
					sec = 0;
					if (min == 60) {
						hour++;
						min = 0;
					}
				}
				timeLabel.setText(String.format("Time: %2d : %2d : %2d", hour, min, sec));
			}
		};
		timer = new Timer(1000, timeListener);
		timer.start();
	}

	private void themeMenu() {

		JMenu Theme = new JMenu("Theme");
		Theme.setForeground(colorMenuText);

		JMenuItem Default = new JMenuItem("Default");
		Default.setToolTipText("Set theme to default");
		Default.addActionListener((ActionEvent event) -> {
			themeTime.stop();
			colorIniText = new Color(65, 105, 225);
			colorIniBack = null;
			colorSetBack = Color.DARK_GRAY;
			colorSetText = Color.green;
			colorConflictBack = Color.red;
			themeChange();
		});
		Theme.add(Default);

		JMenuItem Dark = new JMenuItem("Dark");
		Dark.setToolTipText("Set theme to dark");
		Dark.addActionListener((ActionEvent event) -> {
			themeTime.stop();
			colorIniText = new Color(211, 211, 211);
			colorIniBack = new Color(112, 138, 144);
			colorSetBack = new Color(0, 0, 0);
			colorSetText = new Color(238, 203, 173);
			colorConflictBack = Color.red;
			themeChange();
		});
		Theme.add(Dark);

		JMenuItem XMen = new JMenuItem("X-Men");
		XMen.setToolTipText("Set theme to X-Men");
		XMen.addActionListener((ActionEvent event) -> {
			themeTime.stop();
			colorIniText = new Color(205, 92, 92);
			colorIniBack = new Color(100, 149, 237);
			colorSetBack = new Color(0, 0, 0);
			colorSetText = new Color(255, 215, 0);
			colorConflictBack = new Color(255, 165, 0);
			themeChange();
		});
		Theme.add(XMen);

		ActionListener themeListener = new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent event) {
				int redRand = (int) (Math.random() * 255 + 1);
				int greenRand = (int) (Math.random() * 255 + 1);
				int blueRand = (int) (Math.random() * 255 + 1);
				int alphaRand = (int) (Math.random() * 255 + 1);
				colorIniText = new Color(redRand, greenRand, blueRand, alphaRand);

				redRand = (int) (Math.random() * 255 + 1);
				greenRand = (int) (Math.random() * 255 + 1);
				blueRand = (int) (Math.random() * 255 + 1);
				alphaRand = (int) (Math.random() * 255 + 1);
				colorIniBack = new Color(redRand, greenRand, blueRand);

				redRand = (int) (Math.random() * 255 + 1);
				greenRand = (int) (Math.random() * 255 + 1);
				blueRand = (int) (Math.random() * 255 + 1);
				alphaRand = (int) (Math.random() * 255 + 1);
				colorSetBack = new Color(redRand, greenRand, blueRand);

				redRand = (int) (Math.random() * 255 + 1);
				greenRand = (int) (Math.random() * 255 + 1);
				blueRand = (int) (Math.random() * 255 + 1);
				alphaRand = (int) (Math.random() * 255 + 1);
				colorSetText = new Color(redRand, greenRand, blueRand, alphaRand);

				redRand = (int) (Math.random() * 255 + 1);
				greenRand = (int) (Math.random() * 255 + 1);
				blueRand = (int) (Math.random() * 255 + 1);
				alphaRand = (int) (Math.random() * 255 + 1);
				colorConflictBack = new Color(redRand, greenRand, blueRand, alphaRand);

				themeChange();
			}
		};

		JMenuItem Random = new JMenuItem("Random");
		Random.addActionListener((ActionEvent event) -> {

			themeTime = new Timer(100, themeListener);
			themeTime.start();

		});

		Theme.add(Random);

		menubar.add(Theme);
		setJMenuBar(menubar);

	}

	private void themeChange() {
		int rowTheme, colTheme;

		for (rowTheme = 0; rowTheme < max; rowTheme++) {
			for (colTheme = 0; colTheme < max; colTheme++) {
				if (tfPuzzle[rowTheme][colTheme].isEnabled() == false) {
					tfPuzzle[rowTheme][colTheme].setBackground(colorSetBack);
					tfPuzzle[rowTheme][colTheme].setForeground(colorSetText);
				} else {
					tfPuzzle[rowTheme][colTheme].setForeground(colorIniText);
					tfPuzzle[rowTheme][colTheme].setBackground(colorIniBack);
				}
				tfPuzzle[rowTheme][colTheme].setDisabledTextColor(colorSetText);
			}
		}
		conflict();
	}

	private void volMenu() {

		JMenu Music = new JMenu("Music");
		Music.setForeground(colorMenuText);

		JMenuItem Low = new JMenuItem("Low");
		Low.setToolTipText("Set volume to low");
		Low.addActionListener((ActionEvent event) -> {
			SoundEffect.volume(-40.0f, SoundEffect.Background.clip);
		});
		Music.add(Low);

		JMenuItem Med = new JMenuItem("Medium");
		Med.setToolTipText("Set volume to medium");
		Med.addActionListener((ActionEvent event) -> {
			SoundEffect.volume(-20.0f, SoundEffect.Background.clip);
		});
		Music.add(Med);

		JMenuItem High = new JMenuItem("High");
		High.setToolTipText("Set volume to high");
		High.addActionListener((ActionEvent event) -> {
			SoundEffect.volume(6.0f, SoundEffect.Background.clip);
		});
		Music.add(High);

		JMenuItem Play = new JMenuItem("Play");
		Play.setToolTipText("Play music");
		Play.addActionListener((ActionEvent event) -> {
			SoundEffect.Background.stop();
			SoundEffect.Background.play();
		});
		Music.add(Play);

		JMenuItem Stop = new JMenuItem("Stop");
		Stop.setToolTipText("Stop music");
		Stop.addActionListener((ActionEvent event) -> {
			SoundEffect.Background.stop();
		});
		Music.add(Stop);

		menubar.add(Music);
	}
}
