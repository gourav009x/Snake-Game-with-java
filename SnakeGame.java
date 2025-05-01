import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int BOX_SIZE = 20;
    private final int TOTAL_BOXES = (WIDTH * HEIGHT) / (BOX_SIZE * BOX_SIZE);

    private Timer timer;
    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = true;
    private int score = 0;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        initGame();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        spawnFood();
        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH / BOX_SIZE), rand.nextInt(HEIGHT / BOX_SIZE));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            g.setColor(i == 0 ? Color.GREEN : Color.LIGHT_GRAY);
            Point p = snake.get(i);
            g.fillRect(p.x * BOX_SIZE, p.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);

        if (!running) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.YELLOW);
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case 'U' -> head.y -= 1;
            case 'D' -> head.y += 1;
            case 'L' -> head.x -= 1;
            case 'R' -> head.x += 1;
        }

        snake.add(0, head);
        snake.remove(snake.size() - 1);
    }

    private void checkFood() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            snake.add(new Point(food));
            score += 10;
            spawnFood();
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH / BOX_SIZE || head.y < 0 || head.y >= HEIGHT / BOX_SIZE) {
            running = false;
            timer.stop();
        }

        // Check self-collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                timer.stop();
                break;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        char newDir = direction;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') newDir = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') newDir = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') newDir = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') newDir = 'R';
                break;
        }
        direction = newDir;
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game - Java Edition");
        SnakeGame gamePanel = new SnakeGame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
