import object.Direction;
import object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class GameClient extends JComponent {
    private int screenWide;
    private int screenHeight;
    private boolean stop;

    Tank playerTank;

    private ArrayList<GameObject> gameObjects=new ArrayList<>();
    private ArrayList<GameObject> newObjects=new ArrayList<>();
    private Image[] missileImage=new Image[8];

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
        Image[] brickImage=new Image[]{Tools.getImage("brick.png")};
        Image[] iTankImage=new Image[8];
        Image[] eTankImage=new Image[8];

        String[] sub={"R.png","RD.png","D.png","LD.png","L.png","LU.png","U.png","RU.png"};
        for(int i=0;i<iTankImage.length;i++){
            iTankImage[i]=Tools.getImage("itank"+sub[i]);
            eTankImage[i]=Tools.getImage("etank"+sub[i]);
            missileImage[i]=Tools.getImage("missile"+sub[i]);
        }



        playerTank = new Tank(50, 405, Direction.RIGHT,iTankImage);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                gameObjects.add(new Tank(600 + i * 50, 200 + j * 50, Direction.LEFT, true,eTankImage));
            }
        }

        gameObjects.add(new Wall(160,140,false,3,brickImage));
        gameObjects.add(new Wall(160,396,false,3,brickImage));
        gameObjects.add(new Wall(0,108,true,9,brickImage));
        gameObjects.add(new Wall(0,492,true,9,brickImage));
        gameObjects.add(new Wall(480,204,false,7,brickImage));


        gameObjects.add(playerTank);

    }

    public void addGameObject(){
        gameObjects.addAll(newObjects);
        newObjects.removeAll(newObjects);

        gameObjects.remove(playerTank);
        gameObjects.add(playerTank);
    }

    public void addGameObject(GameObject object){
        newObjects.add(object);
    }

    public int getScreenWide() {
        return screenWide;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Image[] getMissileImage() {
        return missileImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, screenWide, screenHeight);
        for (GameObject gameObjects : gameObjects) {
            gameObjects.draw(g);
        }

        addGameObject();

        Iterator<GameObject> iterator=gameObjects.iterator();
        while (iterator.hasNext()){
            if(!iterator.next().isAlive())
                iterator.remove();
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
            case KeyEvent.VK_CONTROL:
                dirs[4] = true;
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
            case KeyEvent.VK_CONTROL:
                if (dirs[4]){
                    playerTank.fire();
                }
                dirs[4] = false;
                break;
        }
    }
}
