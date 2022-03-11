import javax.swing.*;

public class GameFrame extends JFrame {

        GameFrame(){
            GamePanel g = new GamePanel();
            this.add(g);
            this.setTitle("Snake");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.pack();
            this.setVisible(true);
            this.setLocationRelativeTo(null);

        }
}
