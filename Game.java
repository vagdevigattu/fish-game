import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class Game {
    JFrame f=new JFrame("Fish Game");
    final int speed=5;
    int score=0;
    int lives=3;
    int highscore=0;
    JButton restart=new JButton("Start Over");

    ArrayList<Rectangle> food=new ArrayList<>();
    ArrayList<Rectangle> harmful=new ArrayList<>();

    final int fishX=50;
    int fishY=300;
    Random rand=new Random();

    class myCanvas extends JPanel
    {
        public void paintComponent(Graphics g1)
        {
            super.paintComponent(g1);
            Graphics2D g=(Graphics2D) g1;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(173, 216, 230)); // Light blue color
            g.fillRect(0, 0, getWidth(), getHeight());

            if(lives<=0)
            {
                t.stop();
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD , 50));
                g.drawString("GAME OVER!!", 200, 300);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("Score: " + score, 300, 370);
                if(score>highscore)
                {
                    highscore=score;
                }
                g.drawString("HighScore: " + highscore, 300, 400);
                restart.setBounds(300, 430, 150, 30);
                add(restart);
                g.setFont(new Font("Arial", Font.ITALIC, 10));
                g.drawString("*Press space to restart", 300, 470);
            }
            else
            {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Lives: " + lives, 10, 40);  
                g.drawString("HighScore: " + highscore, 550, 20);


                drawFish(g);
                g.setColor(Color.GREEN);
                for (Rectangle fo : food) {
                    g.fillOval(fo.x, fo.y, fo.width, fo.height);
                }
                g.setColor(Color.RED);
                for (Rectangle h : harmful) {
                    g.fillOval(h.x, h.y, h.width, h.height);
                }        
            }   
        }
    }

    private void drawFish(Graphics2D g2d) {
        // Body
        g2d.setColor(new Color(102, 204, 255));
        g2d.fillOval(fishX, fishY, 60, 30); // Oval body

        // Tail
        g2d.setColor(new Color(0, 153, 204));
        int[] tailX = {fishX - 20, fishX, fishX - 20};
        int[] tailY = {fishY, fishY + 15, fishY + 30};
        g2d.fillPolygon(tailX, tailY, 3);

        // Eye
        g2d.setColor(Color.WHITE);
        g2d.fillOval(fishX + 45, fishY + 5, 10, 10); // Eye
        g2d.setColor(Color.BLACK);
        g2d.fillOval(fishX + 50, fishY + 10, 5, 5); // Pupil

        // Mouth 
        g2d.setColor(Color.BLACK); 
        g2d.drawArc(fishX + 55, fishY + 10, 10, 10, 0, -180);
     
    }
    
    myCanvas canva=new myCanvas();
    Timer t;

    public Game()
    {
        for(int i=0;i<5;i++)
        {
            food.add(new Rectangle(rand.nextInt(500)+200,rand.nextInt(400),20,20));
            harmful.add(new Rectangle(rand.nextInt(500)+200,rand.nextInt(400),20,20));
        }

        KeyListener k=new KeyAdapter() {
            public void keyPressed(KeyEvent e)
            {
                int code=e.getKeyCode();
                if(code==KeyEvent.VK_UP)
                {
                    fishY-=10;
                }
                if(code==KeyEvent.VK_DOWN)
                {
                    fishY+=10;
                }
                if(lives<=0 && code==KeyEvent.VK_SPACE)
                {
                    lives=3;
                    score=0;
                    canva.remove(restart);
                    canva.repaint();
                    t.restart();
                }
            }
        };
        canva.addKeyListener(k);
        f.addKeyListener(k);

        
        ActionListener al=new ActionListener() {

            public void actionPerformed(ActionEvent ev)
            {
                if(lives<=0)
                {
                    canva.repaint();
                }
                for(int i=0;i<food.size();i++)
                {
                    if(new Rectangle(fishX, fishY, 60, 30).intersects(food.get(i)))
                    {
                        food.remove(i);
                        score+=10;
                    }
                    else
                    {
                        Rectangle r=food.get(i);
                        r.x-=speed;
                        if(r.x<=0)
                        {
                            food.remove(i);
                            i--;
                        }
                        else
                        food.set(i, r);
                    }
                }
                for(int i=0;i<harmful.size();i++)
                {
                    if(new Rectangle(fishX, fishY, 60, 30).intersects(harmful.get(i)))
                    {
                        harmful.remove(i);
                        lives-=1;
                    }
                    else
                    {
                        Rectangle r=harmful.get(i);
                        r.x-=speed;
                        if(r.x<=0)
                        {
                            harmful.remove(i);
                            i--;
                        }
                        else
                        harmful.set(i, r);
                    }
                }

                if(food.size()<3)
                {
                    for(int i=0;i<3;i++)
                    {
                        food.add(new Rectangle(rand.nextInt(10)+700, rand.nextInt(500)+100, 20, 20));
                    }
                }
                if(harmful.size()<2)
                {
                    for(int i=0;i<3;i++)
                    {
                        harmful.add(new Rectangle(rand.nextInt(10)+700, rand.nextInt(500)+100, 20, 20));
                    }
                }
                canva.repaint();
            }
        };
        

        t=new Timer(50, al);
        canva.setSize(700, 700);
        canva.setVisible(true);
        f.add(canva);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setSize(700,700);
        f.setVisible(true);
        t.start();
    }

    public static void main(String[] args) {
        new Game();
    }

}
