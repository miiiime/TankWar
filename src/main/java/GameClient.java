import object.Direction;
import object.Tank;
import object.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameClient extends JComponent {
    private int screenWide;
    private int screenHeight;
    private boolean stop;

    Tank playerTank;
    private ArrayList<Tank> enemyTank = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();


    GameClient() {
        this(800, 600);
    }

    GameClient(int screenWide, int screenHeight) {
        this.screenWide = screenWide;
        this.screenHeight = screenHeight;
        this.setPreferredSize(new Dimension(screenWide, screenHeight));

        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; !stop; ) {
                    repaint();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //物件建構
    public void init() {
        playerTank = new Tank(50, 425, Direction.RIGHT);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                enemyTank.add(new Tank(600 + i * 50, 200 + j * 50, Direction.LEFT, true));
            }
        }
        walls.add(new Wall(160,140,false,3));
        walls.add(new Wall(160,396,false,3));
        walls.add(new Wall(0,108,true,9));
        walls.add(new Wall(0,492,true,9));
        walls.add(new Wall(480,204,false,7));
    }


    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, screenWide, screenHeight);
        playerTank.draw(g);
        for (Tank t : enemyTank) {
            t.draw(g);
        }
        for (Wall w : walls) {
            w.draw(g);
        }
    }

    public void keyPressed(KeyEvent k) {
        boolean[] dirs = playerTank.getDirs();
        switch (k.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                dirs[0] = true;
                break;
            case KeyEvent.VK_DOWN:
                dirs[1] = true;
                break;
            case KeyEvent.VK_LEFT:
                dirs[2] = true;
                break;
            case KeyEvent.VK_UP:
                dirs[3] = true;
                break;
        }
    }

    public void keyReleased(KeyEvent k) {
        boolean[] dirs = playerTank.getDirs();
        switch (k.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                dirs[0] = false;
                break;
            case KeyEvent.VK_DOWN:
                dirs[1] = false;
                break;
            case KeyEvent.VK_LEFT:
                dirs[2] = false;
                break;
            case KeyEvent.VK_UP:
                dirs[3] = false;
                break;
        }
    }
}
