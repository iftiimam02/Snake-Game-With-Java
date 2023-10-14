package snake_moving_game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class snake extends JFrame implements KeyListener, Runnable {
    private JPanel gamePanel, scorePanel;
    private JButton[] snakeBody = new JButton[200];
    private JButton bonusFood;
    private JTextArea scoreTextArea;
    private int x = 1000, y = 500, snakeLength = 3, directionX = 10, directionY = 0, speed = 100, score = 0;
    private int[] snakeX = new int[600];
    private int[] snakeY = new int[600];
    private Point bonusFoodPoint = new Point();
    private Thread gameThread;
    private boolean food = false, runLeft = false, runRight = true, runUp = true, runDown = true, bonusFlag = true;
    private Random random = new Random();

    public snake() {
        super("Snake");
        initializeValues();
        setupUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        gameThread = new Thread(this);
        gameThread.start();
        generateBonusFood();
    }

    private void initializeValues() {
        snakeLength = 3;
        snakeX[0] = 200;
        snakeY[0] = 300;
        directionX = 10;
        directionY = 0;
        score = 0;
        food = false;
        runLeft = false;
        runRight = true;
        runUp = true;
        runDown = true;
        bonusFlag = true;
    }

    private void setupUI() {
        gamePanel = new JPanel();
        scorePanel = new JPanel();
        scoreTextArea = new JTextArea("Score==> " + score);
        scoreTextArea.setEnabled(false);
        scoreTextArea.setBackground(Color.BLACK);
        bonusFood = new JButton();
        bonusFood.setEnabled(false);

        gamePanel.setLayout(null);
        scorePanel.setLayout(new GridLayout(1, 0));
        gamePanel.setBounds(0, 0, x, y);
        scorePanel.setBounds(0, y, x, 30);
        gamePanel.setBackground(Color.black);
        scorePanel.setBackground(Color.RED);
        scorePanel.add(scoreTextArea);
        getContentPane().setLayout(null);
        getContentPane().add(gamePanel);
        getContentPane().add(scorePanel);

        // Create the initial snake
        createSnake();
    }

    private void createSnake() {
        for (int i = 0; i < snakeLength; i++) {
            snakeBody[i] = new JButton("lb" + 10);
            snakeBody[i].setEnabled(false);
            gamePanel.add(snakeBody[i]);
            snakeBody[i].setBounds(snakeX[i], snakeY[i], 10, 10);
            snakeX[i + 1] = snakeX[i] - 10;
            snakeY[i + 1] = snakeY[i];
        }
    }

    void generateBonusFood() {
        bonusFlag = true;
        int bonusX = 10 * random.nextInt(50);
        int bonusY = 10 * random.nextInt(25);
        bonusFoodPoint = new Point(bonusX, bonusY);
        bonusFood.setBounds(bonusX, bonusY, 10, 10);
        gamePanel.add(bonusFood);
    }

    private void growSnake() {
        snakeBody[snakeLength] = new JButton();
        snakeBody[snakeLength].setEnabled(false);
        gamePanel.add(snakeBody[snakeLength]);
        int a = 10 + (10 * random.nextInt(48));
        int b = 10 + (10 * random.nextInt(23));
        snakeX[snakeLength] = a;
        snakeY[snakeLength] = b;
        snakeBody[snakeLength].setBounds(a, b, 10, 10);
        snakeLength++;
    }

    void moveForward() {
        Point prevHeadLocation = new Point(snakeX[0], snakeY[0]);

        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
            snakeBody[i].setBounds(snakeX[i], snakeY[i], 10, 10);
        }

        snakeX[0] += directionX;
        snakeY[0] += directionY;
        snakeBody[0].setBounds(snakeX[0], snakeY[0], 10, 10);

        if (bonusFlag && bonusFoodPoint.equals(new Point(snakeX[0], snakeY[0]))) {
            bonusFlag = false;
            gamePanel.remove(bonusFood);
            score += 10;
            scoreTextArea.setText("Score ==> " + score);
            growSnake();
            generateBonusFood();
        }

        for (int i = 1; i < snakeLength; i++) {
            if (bonusFoodPoint.equals(new Point(snakeX[i], snakeY[i]))) {
                // Snake collided with itself
                scoreTextArea.setText("Game over - Score: " + score);
                try {
                    gameThread.join();
                } catch (InterruptedException e) {
                    // Handle interrupted exception
                }
                break;
            }
        }

        snakeBody[0].setLocation(prevHeadLocation);

        gamePanel.repaint();
        show();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT) && runLeft) {
            directionX = -10;
            directionY = 0;
            runLeft = false;
            runRight = true;
            runUp = true;
            runDown = true;
        }

        if ((key == KeyEvent.VK_RIGHT) && runRight) {
            directionX = 10;
            directionY = 0;
            runRight = false;
            runLeft = true;
            runUp = true;
            runDown = true;
        }

        if ((key == KeyEvent.VK_UP) && runUp) {
            directionX = 0;
            directionY = -10;
            runUp = false;
            runDown = true;
            runLeft = true;
            runRight = true;
        }

        if ((key == KeyEvent.VK_DOWN) && runDown) {
            directionX = 0;
            directionY = 10;
            runDown = false;
            runUp = true;
            runLeft = true;
            runRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
    public void run() {
        for (;;) {
            moveForward();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ie) {
                // Handle interrupted exception
            }
        }
    }
  
}
