import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int screen_width = 600;
	static final int screen_height = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (screen_width * screen_height) / UNIT_SIZE;
	static final int delay = 75;
	final int x[] = new int[GAME_UNITS];
	final int y [] = new int[GAME_UNITS];
	int bodyparts = 6;
	int applesEaten;
	int applex;
	int appley;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(screen_width,screen_height));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			// This for loop creates a grid like sheet of graph paper 
			// with the size of the boxes being dependent on the value of the UNIT_SIZE variable
			for(int i = 0; i < screen_height / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, screen_height);
				g.drawLine(0, i * UNIT_SIZE, screen_width, i * UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(applex, appley, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyparts; i++) {
				if(i == 0) {
					g.setColor(Color.blue);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(Color.cyan);
					// Randomly changes the color of snake 
					// g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			// Game Over
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			// Prints the Game Over message in the middle of the screen 
			g.drawString("Score: " + applesEaten, (screen_width - metrics.stringWidth("Score: " + applesEaten)) / 2 ,g.getFont().getSize());

		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		applex = random.nextInt((int)screen_width / UNIT_SIZE) * UNIT_SIZE;
		appley = random.nextInt((int)screen_height / UNIT_SIZE) * UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i = bodyparts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i -1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == applex) && (y[0] == appley)) {
			bodyparts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollision() {
		// Checks if the snakes head collides with its body
		for(int i = bodyparts; i > 0; i--) {
			if((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
		}
		// Checks if the snake's head collides with the left border
		if(x[0] < 0) {
			running = false;
		}
		
		// Checks if the snake's head collides with the right border
		if(x[0] > screen_width) {
			running = false;
		}
		
		// Checks if the snake's head collides with the top border
		if(y[0] < 0) {
			running = false;
		}
		
		// Checks if the snake's head collides with the bottom border
		if(y[0] > screen_height) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		// Game Over
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		// Prints the Game Over message in the middle of the screen 
		g.drawString("Game Over", (screen_width - metrics.stringWidth("Game Over ")) / 2 , screen_height / 2);
		
		// Game Over with Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		// Prints the Game Over message in the middle of the screen 
		g.drawString("You scored: " + applesEaten, (screen_width - metrics2.stringWidth("You scored: " + applesEaten)) / 2 ,g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
	}

}
