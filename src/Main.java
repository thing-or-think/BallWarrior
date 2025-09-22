import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}

class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Arkanoid Game - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final Timer timer;
    private final int PANEL_WIDTH = 600;
    private final int PANEL_HEIGHT = 400;

    private int ballX = 300, ballY = 200;
    private int ballDX = 2, ballDY = -2;
    private final int BALL_SIZE = 15;

    private int paddleX = 250;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 10;

    private boolean leftPressed = false, rightPressed = false;

    private final int brickRow = 5, brickCol = 8;
    private final int brickWidth = 60, brickHeight = 20;
    private final boolean[][] bricks;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        bricks = new boolean[brickRow][brickCol];
        for (int i = 0; i < brickRow; i++) {
            for (int j = 0; j < brickCol; j++) {
                bricks[i][j] = true;
            }
        }

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(paddleX, PANEL_HEIGHT - 40, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Bricks
        g.setColor(Color.RED);
        for (int i = 0; i < brickRow; i++) {
            for (int j = 0; j < brickCol; j++) {
                if (bricks[i][j]) {
                    int x = j * brickWidth + 30;
                    int y = i * brickHeight + 30;
                    g.fillRect(x, y, brickWidth - 5, brickHeight - 5);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move paddle
        if (leftPressed && paddleX > 0) {
            paddleX -= 5;
        }
        if (rightPressed && paddleX < PANEL_WIDTH - PADDLE_WIDTH) {
            paddleX += 5;
        }

        // Move ball
        ballX += ballDX;
        ballY += ballDY;

        // Bounce walls
        if (ballX <= 0 || ballX >= PANEL_WIDTH - BALL_SIZE) {
            ballDX = -ballDX;
        }
        if (ballY <= 0) {
            ballDY = -ballDY;
        }

        // Bounce paddle
        if (ballY + BALL_SIZE >= PANEL_HEIGHT - 40 &&
                ballX + BALL_SIZE >= paddleX &&
                ballX <= paddleX + PADDLE_WIDTH) {
            ballDY = -ballDY;
        }

        // Check brick collision
        for (int i = 0; i < brickRow; i++) {
            for (int j = 0; j < brickCol; j++) {
                if (bricks[i][j]) {
                    int brickX = j * brickWidth + 30;
                    int brickY = i * brickHeight + 30;

                    Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth - 5, brickHeight - 5);

                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        ballDY = -ballDY;
                    }
                }
            }
        }

        // Game over
        if (ballY > PANEL_HEIGHT) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!");
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
