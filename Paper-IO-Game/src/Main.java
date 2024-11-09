import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Paper.io Game");
        GamePanel panel = new GamePanel();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set frame size
        frame.add(panel);
        frame.setVisible(true);
        
        panel.startGame();
    }
}
