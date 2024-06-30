package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import other.StaticFunctions;

/**
 * Represents the Form Page panel.
 * @author Naor
 *
 */
@SuppressWarnings("serial")
public class StartPage extends JPanel {
	
	private static final int BUTTONS_FONT = 18;
	private static final int CANNON_HEIGHT = 70;
	private static final int CANNON_WIDTH = 80;
	public static String name = "";
	private int bulletLevel = 1, numBulletsInRow = 1;
	public static boolean isSoundOn = true;
	private final JFrame frame;
	private JPanel p;
	private JButton playButton, settings, n1, n2, n3, b1, b2, b3;
	private JLabel addNameLbl, msg;
	private JTextField nickNameTxt;
	private BufferedImage image, coin, cannonImage;
	private JLabel label, kingOfAllTimes;
	
	public StartPage(JFrame frame) throws IOException {
		this.frame = frame;
		p = this;
		setLayout(null);
		image = ImageIO.read(new File("src/img/backgroundImg.jpg"));
		cannonImage = ImageIO.read(new File("src/img/cannon3.png"));
		addLabels();
	    addUperButtons();
	    addBulletsChoises();
	    addCoins();
	    addNickName();
		addButtons();
	}

	private void addBulletsChoises() {
		JLabel ChooseYourBullet = new JLabel("<html>Choose your Bullet: </html>");
		ChooseYourBullet.setFont(new Font("Garamond", Font.BOLD, 20));
		ChooseYourBullet.setBounds(460, 550, 120, 100);
		ChooseYourBullet.setForeground(Color.WHITE);
		ChooseYourBullet.setVisible(true);
		add(ChooseYourBullet);
		b1 = new JButton(new ImageIcon("src/img/bullet0.png"));
		b2 = new JButton(new ImageIcon("src/img/bullet1.png"));
		b3 = new JButton(new ImageIcon("src/img/bullet2.png"));
		makeNumberImages(b1, 1, false);
		makeNumberImages(b2, 2, false);
		makeNumberImages(b3, 3, false);
		
		JLabel numB = new JLabel("<html>Number of bullets in row: </html>");
		numB.setFont(new Font("Garamond", Font.BOLD, 20));
		numB.setBounds(460, 620, 120, 100);
		numB.setForeground(Color.WHITE);
		numB.setVisible(true);
		add(numB);
		n1 = new JButton(new ImageIcon("src/img/number1.png"));
		n2 = new JButton(new ImageIcon("src/img/number2.png"));
		n3 = new JButton(new ImageIcon("src/img/number3.png"));
		makeNumberImages(n1, 1, true);
		makeNumberImages(n2, 2, true);
		makeNumberImages(n3, 3, true);
	}

	private void makeNumberImages(JButton b, final int i, final boolean isNumber) {
		if (i == 1)
			b.setVisible(true);
		else 
			b.setVisible(false);
		b.setBorder(null);
		b.setContentAreaFilled(false);
		if (isNumber)
			b.setBounds(585, 640, 60, 60);
		else
			b.setBounds(600, 590, 20, 20);
		add(b);
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isNumber) {
					numBulletsInRow = numBulletsInRow % 3 + 1;
					switch (i) {
					case 1:
						n2.setVisible(true);
						n1.setVisible(false);
						break;
					case 2:
						n3.setVisible(true);
						n2.setVisible(false);
						break;
					case 3:
						n1.setVisible(true);
						n3.setVisible(false);
						break;
					default:
						break;
					}
				}
				else {
					bulletLevel = bulletLevel % 3 + 1;
					switch (i) {
					case 1:
						b2.setVisible(true);
						b1.setVisible(false);
						break;
					case 2:
						b3.setVisible(true);
						b2.setVisible(false);
						break;
					case 3:
						b1.setVisible(true);
						b3.setVisible(false);
						break;
					default:
						break;
					}
				}
			}
		});
		
	}

	private void addCoins() throws IOException {
		int coins = 1000;
		coin = ImageIO.read(new File("src/img/coin.png"));
		JLabel money = new JLabel(StaticFunctions.returnBigNumbersInShortcat(coins));
		money.setFont(new Font("Garamond", Font.BOLD, 30));
		money.setBounds(520, 5, 100, 50);
		money.setForeground(Color.WHITE);
		money.setVisible(true);
		add(money);
	}

	private void addUperButtons() throws IOException {
		settings = new JButton(new ImageIcon("src/img/settings.png"));
	    changeSettingsButton(settings);
	    add(settings);
		
	    settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Settings");
			}
		});
	}

	private void addLabels() {
		label = new RotatedLabel("Ball Blast");
		label.setFont(new Font("Garamond", Font.BOLD, 70));
		label.setBounds(150, -50, 400, 400);
		label.setForeground(Color.RED);
		label.setVisible(true);
		add(label);
		int max;
		String maxName;
//		FileResult fileRes = FileManager.readFromFile();
//		max = fileRes.getScore();
//		maxName = fileRes.getName();
		max = 0;
		maxName = "Naor";
		kingOfAllTimes = new RotatedLabel("Best Score: " + max + " # by " + maxName);
		kingOfAllTimes.setFont(new Font("Garamond", Font.BOLD, 30));
		kingOfAllTimes.setBounds(150, 0, 400, 400);
		kingOfAllTimes.setForeground(Color.RED);
		kingOfAllTimes.setVisible(true);
		add(kingOfAllTimes);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
		g.drawImage(cannonImage, (int) GameGraphics.getScreenWidth() / 2, (int) GameGraphics.getScreenHeight() - CANNON_HEIGHT - 90, CANNON_WIDTH, CANNON_HEIGHT, null);
		g.drawImage(coin, frame.getWidth() - 60, 5, 50, 49, null);
	}
	
	private void changeSettingsButton(JButton button) {
		button.setBorder(null);
		button.setContentAreaFilled(false);
		//button.setOpaque(false);
		button.setBounds(20, 10, 44, 50);
	}
	
	private void addNickName() {
		addNameLbl = new JLabel("<html>Enter your nickname:</html> ");
		addNameLbl.setFont(new Font("Garamond", Font.BOLD, 45));
		addNameLbl.setBounds(10, 400, 300, 150);
		addNameLbl.setForeground(Color.WHITE);
		add(addNameLbl);
		
		nickNameTxt = new JTextField();
		nickNameTxt.setFont(new Font("Garamond", Font.BOLD, 30));
		nickNameTxt.setBounds(250, 460, 250, 40);
		nickNameTxt.setForeground(Color.RED);
		nickNameTxt.setBorder(null);
		add(nickNameTxt);
		
		msg = new JLabel();
		msg.setFont(new Font("Garamond", Font.BOLD, 30));
		msg.setBounds(250, 450, 400, 150);
		msg.setForeground(Color.RED);
		msg.setVisible(false);
		add(msg);
	}
	
	private void addButtons() {
		
		playButton = new JButton("Start Play Now");
		playButton.setForeground(Color.RED);
		playButton.setFont(new Font("verdana", Font.ITALIC, BUTTONS_FONT));
		playButton.setBounds(200, 560, 230, 50);
		add(playButton);
		
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				name = nickNameTxt.getText();
				name = "Naor";
				if (name.equals("")) {
					msg.setText("Must enter a name !!");
					msg.setVisible(true);
				} else if (name.contains("\n")) {
					msg.setText("Thought you can trick me ?");
					msg.setVisible(true);
				} else {
					frame.remove(p);
					GameGraphics graphics = null;
					try {
						graphics = new GameGraphics(frame, bulletLevel, numBulletsInRow);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					frame.setVisible(true);
					graphics.gameLoop();
				}
			}
		});
	}
	
	private MouseAdapter createMouseOver() {
		return new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				((Component) e.getSource()).setFont(new Font("verdana", Font.BOLD, BUTTONS_FONT));
				super.mouseEntered(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				((Component)e.getSource()).setFont(new Font("verdana", Font.ITALIC, BUTTONS_FONT));
				super.mouseExited(e);
			}
			
		};
	}
}

class RotatedLabel extends JLabel {
	public RotatedLabel(String text) {
		super(text);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D gx = (Graphics2D) g;
		gx.rotate(-0.2, 100, 200);
		super.paintComponent(g);
	}
}
