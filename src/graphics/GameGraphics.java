package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import game.Game;

public class GameGraphics {
	
	private JFrame frame;
	private GamePanel panel;
	private Game game;
	private PlayerActions actions;
	private JLabel score, youWon, msg;
	private JButton retry, stop;
	private Thread gameThread;
	private int bulletLevel, numBulletsInRow;
	private static final int SCREEN_WIDTH = 650;
	private static final int SCREEN_HEIGHT = 800;
	private static int FPS = 60;
	private static int ITERATION_MILLIS = 1000 / FPS;
	public static boolean isGameOver;
	public static boolean suspended = false;
	private MouseListener mouseListener;
	private ImageIcon newGame;
	
	public GameGraphics(JFrame frame, int bulletLevel, int numBulletsInRow) throws IOException {
		this.frame = frame;
		this.bulletLevel = bulletLevel;
		this.numBulletsInRow = numBulletsInRow;
		createNewGame(bulletLevel, numBulletsInRow);
		frame.requestFocus();
		intiallizeFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	public void createNewGame(int bulletLevel, int numBulletsInRow) throws IOException {
		isGameOver = false;
		game = new Game(bulletLevel, numBulletsInRow);
		actions = new PlayerActions(game.getCannon());
		panel = new GamePanel(game.getBalls(), game.getCannon());
		addScore();
	}

	public void gameLoop() {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					createNewGameLoop();
				} catch (InterruptedException e) {
					System.out.println("A thread in the game loop was interrupted!");
				}
			}
		};
		gameThread = new Thread(runnable);
		gameThread.start();
	}
	
	public void createNewGameLoop() throws InterruptedException {
		int cont = 0;
		while (true) {
            synchronized(this) {
                while (suspended) {
                   wait();
                }
             }
			if (cont != 0 && !isGameOver) {
				isGameOver = true;
				stop.setVisible(false);
				if (cont == 2) {
					retry.setIcon(newGame);
					retry.setBounds(SCREEN_WIDTH / 2 - 80, 100, 182, 62);
					youWon.setText("You Won !!");
				}
				else {
					youWon.setText("You Lose !!");
				}
				youWon.setVisible(true);
				retry.setVisible(true);
				if (cont == 1)
					Thread.sleep(700);
			}
			long startTime = System.currentTimeMillis();
			if (!isGameOver)
				actions.updatePlayerActions();
			cont = game.update((GamePanel) panel);
			updateScoreLabel();
			panel.repaint();
			long endTime = System.currentTimeMillis();
			long gameCalculationTime = endTime - startTime;
			long timeToSleep = ITERATION_MILLIS - gameCalculationTime;
			if (timeToSleep > 0) {
				Thread.sleep(timeToSleep);
			}
		}
	}
	
	private void addYouWonLabel() {
		youWon = new JLabel();
		youWon.setForeground(Color.RED);
		youWon.setBounds(SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2, 300, 50);
		youWon.setFont(new Font("verdana", Font.ROMAN_BASELINE, 50));
		youWon.setVisible(false);
		panel.add(youWon);
	}
	
	private void addScore() {
		score = new JLabel();
		score.setForeground(Color.BLUE);
		score.setBounds(SCREEN_WIDTH / 2 - 50, 0, 200, 50);
		score.setFont(new Font("verdana", Font.ROMAN_BASELINE, 18));
		score.setVisible(true);
		panel.add(score);
	}
	
	private void updateScoreLabel() {
		score.setText("Points: " + String.valueOf(game.getScore()));
	}
	
	private void addStopButton() {
		stop = new JButton(new ImageIcon("src/img/stopButton.png"));
		stop.setBackground(Color.ORANGE);
		stop.setBorder(null);
		stop.setOpaque(false);
		stop.setBounds(10, 10, 38, 40);
		stop.setFont(new Font("verdana", Font.ROMAN_BASELINE, 18));
		stop.setVisible(true);
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!suspended)
					suspend();
				else 
					resume();
			}
		});
		stop.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) { }
			
			@Override
			public void mousePressed(MouseEvent e) {
				frame.requestFocus();
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 
				stop.setOpaque(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) { 
				stop.setOpaque(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		panel.add(stop);
		addMsg();
	}
	
	void suspend() {
		suspended = true;
		msg.setVisible(true);
		panel.addMouseListener(mouseListener);
		panel.repaint();
	}
	
	private void addMsg() {
		msg = new JLabel("<html><center>Click anywhere to resume</center></html>");
		msg.setForeground(Color.WHITE);
		msg.setBackground(new Color(0, 0, 96));
		msg.setOpaque(true);
		msg.setBounds(SCREEN_WIDTH / 2 - 150, 300, 270, 150);
		msg.setFont(new Font("verdana", Font.ROMAN_BASELINE, 31));
		msg.setVisible(false);
		panel.add(msg);
	}

	synchronized void resume() {
		suspended = false;
		msg.setVisible(false);
		panel.removeMouseListener(mouseListener);
		frame.requestFocus();
		notify();
	}
	
	
	private void addRetryButton() {
		retry = new JButton(new ImageIcon("src/img/button_retry.png"));
		retry.setOpaque(false);
		retry.setBorder(null);
		retry.setBackground(Color.BLUE);
		retry.setBounds(SCREEN_WIDTH / 2 - 50, 100, 119, 62);
		retry.setVisible(false);
		retry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gameThread.interrupt();
					restart();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(retry);
	}
	
	protected void restart() throws IOException {
		panel.remove(retry);
		createNewGame(bulletLevel, numBulletsInRow);
		intiallizeFrame();
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
		frame.requestFocus();
		gameLoop();
	}

	private void initMouseListener() {
		mouseListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) { resume(); }
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		};
	}
	
	public void intiallizeFrame() {
		frame.add(panel);
		frame.repaint();
		for (KeyListener listener : frame.getKeyListeners()) {
			frame.removeKeyListener(listener);
		}
		frame.addKeyListener(actions);
		newGame = new ImageIcon("src/img/button_new-game.png");
		initMouseListener();
		addRetryButton();
		addYouWonLabel();
		addStopButton();
	}
	
	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}
	
	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}
}
