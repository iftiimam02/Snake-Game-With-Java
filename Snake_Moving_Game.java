package snake_moving_game;
public class Snake_Moving_Game {
    public static void main(String[] args) {
        snake snakeGame = new snake();
        snakeGame.setSize(1000, 590);
        snakeGame.setVisible(true);
        snakeGame.generateBonusFood();
        snakeGame.moveForward();
    }  
    }
    
