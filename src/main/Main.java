package main;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.GameGraphics;
import graphics.StartPage;

public class Main {

	public static GameGraphics graphics;
	public static JFrame frame;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		frame = new JFrame("Ball Blast");
		JPanel startPage = new StartPage(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(GameGraphics.getScreenWidth(), GameGraphics.getScreenHeight());
		frame.setVisible(true);
		frame.add(startPage);
		frame.setResizable(false);		
	}
}
