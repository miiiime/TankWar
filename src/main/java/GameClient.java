import object.Direction;
import object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GameClient extends JComponent {
    private int screenWide;
    private int screenHeight;
    private boolean stop;

    private PlayerTank playerTank;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<GameObject> newObjects = new ArrayList<>();
    private int[] play_zone;
    public static final int SPACE = 30;
    public static final int HALF_SPACE = 15;
    private Image[] brickImage;
    private Image[] eTankImage;
    private Image[] missileImage;

    GameClient() {
        this(800, 600);
    }

    GameClient(int screenWide, int screenHeight) {
        this.screenWide = screenWide;
        this.screenHeight = screenHeight;
        this.setPreferredSize(new Dimension(screenWide, screenHeight));
        play_zone = new int[]{0, 0, screenWide, screenHeight};

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
        brickImage = new Image[]{Tools.getImage("brick.png")};
        Image[] iTankImage = new Image[8];
        eTankImage = new Image[8];
        missileImage = new Image[8];

        String[] sub = {"R.png", "RD.png", "D.png", "LD.png", "L.png", "LU.png", "U.png", "RU.png"};
        for (int i = 0; i < iTankImage.length; i++) {
            iTankImage[i] = Tools.getImage("itank" + sub[i]);
            eTankImage[i] = Tools.getImage("etank" + sub[i]);
            missileImage[i] = Tools.getImage("missile" + sub[i]);
        }

        String[] audios = {"hitting.wav", "shoot.wav"};
        for (String fileName : audios)
            new File("assets/audios/" + fileName);

        playerTank = new PlayerTank(0, 0, Direction.RIGHT, iTankImage);
        reset(-1);
    }

    public void addGameObject() {
        gameObjects.addAll(newObjects);
        newObjects.removeAll(newObjects);

        gameObjects.remove(playerTank);
        gameObjects.add(playerTank);
    }

    public void addGameObject(GameObject object) {
        newObjects.add(object);
    }

    public int getScreenWide() {
        return screenWide;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int[] getPlay_zone() {
        return play_zone;
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
        g.fillRect(play_zone[0], play_zone[1], play_zone[2], play_zone[3]);

        for (GameObject gameObjects : gameObjects) {
            gameObjects.draw(g);
        }

        addGameObject();

        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isAlive())
                iterator.remove();
        }

        checkGameStatus();
        g.setColor(Color.WHITE);
        g.drawString("HP：" + playerTank.getHealth(), 0, 10);
    }

    private void checkGameStatus() {
        boolean out_of_enemy = true;

        for (GameObject gameObjects : gameObjects) {
            if (gameObjects instanceof EnemyTank) {
                out_of_enemy = false;
                break;
            }
        }

        if (out_of_enemy) {
            reset(0);
        }
    }

    public void reset(int level) {
        playerTank.plusHealth(1);

        gameObjects.removeAll(gameObjects);
        gameObjects.add(playerTank);

        switch (level) {
            case -1:
                playerTank.setHealth(2);
                playerTank.setAlive(true);
            case 0: {
                play_zone = new int[]{10, 0, 26 * SPACE, 20 * SPACE};

                int[][] wall_list = new int[][]{{5, 4, 0, 3}, {5, 13, 0, 3},{0,3,1,9},{0,16,1,9},{16,7,0,6},{9,0,0,4},{9,16,0,4},{16,2,1,4},{16,17,1,4},
                        {-1,0,0,26},{26,0,0,26}};

                for (int[] wall_info : wall_list) {
                    gameObjects.add(new Wall(get_place_x(wall_info[0]), get_place_y(wall_info[1]), wall_info[2] == 1, wall_info[3], brickImage));
                }

                playerTank.tp(2 * SPACE, 14 * SPACE, Direction.RIGHT);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        gameObjects.add(new EnemyTank(get_place_x(18) + i * SPACE * 3, get_place_y(5) + j * SPACE * 3, Direction.LEFT, 1, eTankImage));
                    }
                }
            }
            break;
            case 1:
                break;
        }

    }

    public int get_place_x(int block) {
        return play_zone[0] + HALF_SPACE + block * SPACE;
    }

    public int get_place_y(int block) {
        return play_zone[1] + HALF_SPACE + block * SPACE;
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
                if (!dirs[4] && !playerTank.isAlive()) {
                    reset(-1);
                } else dirs[4] = true;
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
                dirs[4] = false;
                break;
        }
    }
}
