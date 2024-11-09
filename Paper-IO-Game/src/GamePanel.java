import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int CELL_SIZE = 20;
    private final int GRID_WIDTH = 40;
    private final int GRID_HEIGHT = 30;

    private boolean[][] player1Territory;
    private boolean[][] player2Territory;

    private Set<Point> player1Trail;
    private Set<Point> player2Trail;

    private int player1X = 5, player1Y = 5;
    private int player2X = GRID_WIDTH - 6, player2Y = GRID_HEIGHT - 6;
    private int player1DirX = 0, player1DirY = 0;
    private int player2DirX = 0, player2DirY = 0;

    private Timer timer;
    private boolean gameOver = false;
    private String winnerMessage = "";

    public GamePanel() {
        setBackground(Color.WHITE);
        player1Territory = new boolean[GRID_WIDTH][GRID_HEIGHT];
        player2Territory = new boolean[GRID_WIDTH][GRID_HEIGHT];

        player1Trail = new HashSet<>();
        player2Trail = new HashSet<>();

        // Initialize starting territories
        player1Territory[player1X][player1Y] = true;
        player2Territory[player2X][player2Y] = true;

        timer = new Timer(100, this);
        addKeyListener(this);
        setFocusable(true);
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPlayers(g);

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(winnerMessage, getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    private void drawGrid(Graphics g) {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                if (player1Territory[x][y]) {
                    g.setColor(new Color(135, 206, 250)); // Light blue color for Player 1 territory
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (player2Territory[x][y]) {
                    g.setColor(new Color(255, 99, 71)); // Light red color for Player 2 territory
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawPlayers(Graphics g) {
        // Draw trails
        g.setColor(Color.CYAN); // Player 1 trail color
        for (Point p : player1Trail) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        g.setColor(Color.PINK); // Player 2 trail color
        for (Point p : player2Trail) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw player current positions
        g.setColor(Color.BLUE);
        g.fillRect(player1X * CELL_SIZE, player1Y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        g.setColor(Color.RED);
        g.fillRect(player2X * CELL_SIZE, player2Y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateGame();
            repaint();
        }
    }

    private void updateGame() {
        // Move players
        player1X += player1DirX;
        player1Y += player1DirY;
        player2X += player2DirX;
        player2Y += player2DirY;

        // Check boundaries and trails
        checkBounds();
        handleTrailUpdates();
        checkCollisions();
    }

    private void checkBounds() {
        if (player1X < 0 || player1X >= GRID_WIDTH || player1Y < 0 || player1Y >= GRID_HEIGHT) {
            gameOver = true;
            winnerMessage = "Red Wins!";
        } else if (player2X < 0 || player2X >= GRID_WIDTH || player2Y < 0 || player2Y >= GRID_HEIGHT) {
            gameOver = true;
            winnerMessage = "Blue Wins!";
        }
    }

    private void handleTrailUpdates() {
        // Add current positions to trails if outside their territory
        if (!player1Territory[player1X][player1Y]) {
            player1Trail.add(new Point(player1X, player1Y));
        } else {
            // Player 1 reconnects trail to their territory
            for (Point p : player1Trail) {
                player1Territory[p.x][p.y] = true;
            }
            player1Trail.clear();
        }

        if (!player2Territory[player2X][player2Y]) {
            player2Trail.add(new Point(player2X, player2Y));
        } else {
            // Player 2 reconnects trail to their territory
            for (Point p : player2Trail) {
                player2Territory[p.x][p.y] = true;
            }
            player2Trail.clear();
        }
    }

    private void checkCollisions() {
        Point player1Pos = new Point(player1X, player1Y);
        Point player2Pos = new Point(player2X, player2Y);

        // Check if player 1 collides with player 2's trail or starting position
        if (player2Trail.contains(player1Pos) || (player1Pos.equals(new Point(GRID_WIDTH - 6, GRID_HEIGHT - 6)))) {
            gameOver = true;
            winnerMessage = "Blue Wins!";
        } 
        // Check if player 2 collides with player 1's trail or starting position
        else if (player1Trail.contains(player2Pos) || (player2Pos.equals(new Point(5, 5)))) {
            gameOver = true;
            winnerMessage = "Red Wins!";
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Movement keys for player 1
        if (key == KeyEvent.VK_UP) {
            player1DirX = 0;
            player1DirY = -1;
        } else if (key == KeyEvent.VK_DOWN) {
            player1DirX = 0;
            player1DirY = 1;
        } else if (key == KeyEvent.VK_LEFT) {
            player1DirX = -1;
            player1DirY = 0;
        } else if (key == KeyEvent.VK_RIGHT) {
            player1DirX = 1;
            player1DirY = 0;
        }

        // Movement keys for player 2
        if (key == KeyEvent.VK_W) {
            player2DirX = 0;
            player2DirY = -1;
        } else if (key == KeyEvent.VK_S) {
            player2DirX = 0;
            player2DirY = 1;
        } else if (key == KeyEvent.VK_A) {
            player2DirX = -1;
            player2DirY = 0;
        } else if (key == KeyEvent.VK_D) {
            player2DirX = 1;
            player2DirY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
