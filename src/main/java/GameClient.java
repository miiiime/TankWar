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
        playerTank=new Tank(100,100,Direction.LEFT);

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
        g.drawImage(playerTank.getImage(),playerTank.getX(),playerTank.getY(),null);
    }

    public void keyPressed(KeyEvent k){
        switch (k.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
                playerTank.setDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_DOWN:
                playerTank.setDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                playerTank.setDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_UP:
                playerTank.setDirection(Direction.UP);
                break;
        }
        playerTank.move();
    }


}
