import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Date;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 20;
    static final int gameUnits = (screenWidth*screenHeight)/unitSize;
    static final int DELAY = 55;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    File f = new File("Score.txt");
    Date date = new Date();
    int bodyParts = 5;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdaptor());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < screenHeight / unitSize; i++) {
                g.drawLine(i * unitSize, 0, i * unitSize, screenHeight);
                g.drawLine(0, i * unitSize, screenWidth, i * unitSize);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Arial",Font.BOLD,35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,
                    (screenWidth - metrics.stringWidth("Score: " + applesEaten))/2,
                    g.getFont().getSize());
        }else{gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(screenWidth/unitSize))*unitSize;
        appleY = random.nextInt((int)(screenHeight/unitSize))*unitSize;
    }

    public void move(){
        for(int i = bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollison(){
        //checks if head collides with body
        for (int i = bodyParts;i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left border
        if(x[0]<0){
            running = false;
        }
        //check if head touches right border
        if(x[0]>screenWidth){
            running = false;
        }
        //check if head touches top border
        if(y[0] <0){
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > screenHeight){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Score text
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,35));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,
                (screenWidth - metrics1.stringWidth("Score: " + applesEaten))/2,
                g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,65));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (screenWidth - metrics2.stringWidth("Game Over"))/2,
                screenHeight/2);
        //Restart text
        g.setColor(Color.blue);
        g.setFont(new Font("Arial",Font.BOLD,40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press ENTER to restart",
                (screenWidth - metrics3.stringWidth("Press ENTER to restart"))/2,
                screenHeight*3/4);
        //Save score text
        g.setColor(Color.blue);
        g.setFont(new Font("Arial",Font.BOLD,40));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString("Press S to SAVE the score",
                (screenWidth - metrics4.stringWidth("Press S to SAVE the score"))/2,
                screenHeight*5/6);

    }

    public void saveScore(int score) throws FileNotFoundException {
        try {
            if(!f.exists()) {
                FileWriter obj = new FileWriter(f);
            }
            FileWriter fileWriter = new FileWriter(f,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(" Ai mancat -> " + applesEaten + " mere la data de " + date.toString() + "\n" );
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(running){
            move();
            checkApple();
            checkCollison();
        }
        repaint();
    }

    public class MyKeyAdaptor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if(!running) {
                        new GameFrame();
                    }
                    break;
                case KeyEvent.VK_S:
                    if(!running) {
                        try {
                            saveScore(applesEaten);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}

