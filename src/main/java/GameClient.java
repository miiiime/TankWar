import object.Direction;
import object.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameClient extends JComponent
{
    private int screenWide;
    private int screenHeight;
    private boolean stop;

    Tank playerTank;

    GameClient(){
        this(800,600);
    }

    GameClient(int screenWide,int screenHeight){
        this.screenWide=screenWide;
        this.screenHeight=screenHeight;
        this.setPreferredSize(new Dimension(screenWide,screenHeight));
        playerTank=new Tank(100,100,Direction.RIGHT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;!stop;){
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

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,screenWide,screenHeight);
        playerTank.draw(g);
    }

    public void keyPressed(KeyEvent k){
        boolean[] dirs=playerTank.getDirs();
        switch (k.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
                dirs[0]=true;
                break;
            case KeyEvent.VK_DOWN:
                dirs[1]=true;
                break;
            case KeyEvent.VK_LEFT:
                dirs[2]=true;
                break;
            case KeyEvent.VK_UP:
                dirs[3]=true;
                break;
        }
    }

    public void keyReleased(KeyEvent k){
        boolean[] dirs=playerTank.getDirs();
        switch (k.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
                dirs[0]=false;
                break;
            case KeyEvent.VK_DOWN:
                dirs[1]=false;
                break;
            case KeyEvent.VK_LEFT:
                dirs[2]=false;
                break;
            case KeyEvent.VK_UP:
                dirs[3]=false;
                break;
        }
    }
}
