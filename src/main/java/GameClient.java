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
    private ArrayList<GameObject> UI_objects = new ArrayList<>();
    private int[] play_zone;
    public static final int SPACE = 30;
    public static final int HALF_SPACE = 15;
    private int ptx;
    private int pty;

    private Image[] brickImage;
    private Image[] eTankImage;
    private Image[] missileImage;
    private Image[] super_fire_image;
    private Image[] explosion_image;
    private Image[] health_ball_image;
    private Image[] baseImage;

    private int level;
    private final int max_level = 4;
    private boolean out_of_enemy;

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
        super_fire_image = new Image[6];
        explosion_image = new Image[10];
        health_ball_image = new Image[4];

        String[] sub = {"R.png", "RD.png", "D.png", "LD.png", "L.png", "LU.png", "U.png", "RU.png"};
        for (int i = 0; i < iTankImage.length; i++) {
            iTankImage[i] = Tools.getImage("itank" + sub[i]);
            eTankImage[i] = Tools.getImage("etank" + sub[i]);
            missileImage[i] = Tools.getImage("missile" + sub[i]);
        }
        sub = new String[]{"super_fire_", "explosion_", "health_ball_"};
        Image[][] target = new Image[][]{super_fire_image, explosion_image, health_ball_image};
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                target[i][j] = Tools.getImage(sub[i] + (j + 1) + ".png");
            }
        }
        baseImage = new Image[]{Tools.getImage("base.png")};


        String[] audios = {"hitting.wav", "hitting_alter.wav", "shoot.wav", "shoot_alter.wav"};
        for (String fileName : audios)
            new File("assets/audios/" + fileName);

        playerTank = new PlayerTank(0, 0, Direction.RIGHT, iTankImage);
        UI_objects.add(new Animation(20, 20, health_ball_image, 1, 1));
        UI_objects.add(new Animation(20, 50, new Image[]{super_fire_image[0], super_fire_image[1]}, 1, 1));
        level = 1;
        reset(0);
    }

    public void addGameObject() {
        gameObjects.addAll(newObjects);
        newObjects.clear();

        gameObjects.remove(playerTank);
        gameObjects.add(playerTank);
    }

    public void addGameObject(GameObject object) {
        newObjects.add(object);
    }

    public int getPtx() {
        return ptx;
    }
    public void setPtx(int ptx) {
        this.ptx = ptx;
    }

    public int getPty() {
        return pty;
    }
    public void setPty(int pty) {
        this.pty = pty;
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

    public Image[] getImage(String target) {
        switch (target) {
            case "eTank":
                return eTankImage;
            case "missile":
                return missileImage;
            case "super_fire":
                return super_fire_image;
            case "explosion":
                return explosion_image;
            case "health_ball":
                return health_ball_image;
        }
        return missileImage;
    }

    @Override
    protected void paintComponent(Graphics g) {                             //=======

        if (playerTank.getX() < screenWide / 2.0 || play_zone[2] - play_zone[0] <= screenWide) {
            ptx = 0;
        } else if (playerTank.getX() > play_zone[2] + play_zone[0] + SPACE - screenWide / 2.0) {
            ptx = -play_zone[2] - play_zone[0] + screenWide - SPACE;
        } else {
        ptx = (int) (screenWide / 2.0 - playerTank.getX());
        }

        if (playerTank.getY() < screenHeight / 2.0 || play_zone[3] - play_zone[1] <= screenHeight) {
            pty = 0;
        } else if (playerTank.getY() > play_zone[3] + play_zone[1] + SPACE - screenHeight / 2.0) {
            pty = -play_zone[3] - play_zone[1] + screenHeight - SPACE;
        } else {
            pty = (int) (screenHeight / 2.0 - playerTank.getY());
        }

//        pty = (int) (screenHeight / 2.0 - playerTank.getY());

        g.setColor(Color.DARK_GRAY);
        g.fillRect(play_zone[0]+ptx, play_zone[1]+pty, play_zone[2], play_zone[3]);

        for (GameObject gameObjects : gameObjects) {
            gameObjects.draw(g);
        }

        for (GameObject objects : UI_objects) {
            objects.draw(g);
        }

        addGameObject();

        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isAlive())
                iterator.remove();
        }

        checkGameStatus(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font(null, Font.BOLD, 30));
        g.drawString("" + playerTank.getHealth(), 45, 30);
        g.drawString("" + playerTank.get_super_fire_amount(), 45, 60);
    }

    private void checkGameStatus(Graphics g) {
        out_of_enemy = true;

        for (GameObject gameObjects : gameObjects) {
            if (gameObjects instanceof EnemyTank || (gameObjects instanceof Building && ((Building) gameObjects).getTeam()!=0)) {
                out_of_enemy = false;
                break;
            }
        }

        if (!playerTank.isAlive()) {
            g.setFont(new Font(null, Font.BOLD, 30));
            g.setColor(Color.BLACK);
            g.drawString("Your tank has been destroy.", 2, 567);
            g.drawString("Press SPACE to reset to the first level.", 2, 597);
            g.setColor(Color.RED);
            g.drawString("Your tank has been destroy.", 0, 565);
            g.setColor(Color.WHITE);
            g.drawString("Press SPACE to reset to the first level.", 0, 595);
        }
        if (out_of_enemy) {
            g.setFont(new Font(null, Font.BOLD, 30));
            g.setColor(Color.BLACK);
            g.drawString("Level Clear.", 2, 567);
            g.drawString("Press SPACE to the next level.", 2, 597);
            g.setColor(Color.WHITE);
            g.drawString("Level Clear.", 0, 565);
            g.drawString("Press SPACE to the next level.", 0, 595);
        }
    }

    public void reset(int target_level) {
        gameObjects.clear();
        gameObjects.add(playerTank);
        if (target_level < 0) target_level = 0;
        else if (target_level > max_level) target_level = 1;
        level = target_level == 0 ? 1 : target_level;
        ptx = 0;
        pty = 0;

        switch (target_level) {
            case 0:
                playerTank.setHealth(2);
                playerTank.set_super_fire_amount(2);
                playerTank.setAlive(true);
            case 1: {
                play_zone = new int[]{10, 0, 26 * SPACE, 20 * SPACE};

                int[][] wall_list = new int[][]{{5, 4, 0, 3}, {5, 13, 0, 3}, {0, 3, 1, 9}, {0, 16, 1, 9}, {16, 7, 0, 6}, {9, 0, 0, 4}, {9, 16, 0, 4}, {16, 2, 1, 4}, {16, 17, 1, 4},
                        {-1, 0, 0, 20}, {26, 0, 0, 20}};

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
            case 2: {
                play_zone = new int[]{10, 0, 26 * SPACE, 20 * SPACE};

                int[][] wall_list = new int[][]{{5, 4, 0, 3}, {5, 13, 0, 3}, {16, 0, 0, 7}, {16, 13, 0, 7}, {9, 0, 0, 4}, {9, 16, 0, 4},
                        {-1, 0, 0, 20}, {26, 0, 0, 20}};

                for (int[] wall_info : wall_list) {
                    gameObjects.add(new Wall(get_place_x(wall_info[0]), get_place_y(wall_info[1]), wall_info[2] == 1, wall_info[3], brickImage));
                }

                playerTank.tp(8 * SPACE, 18 * SPACE, Direction.RIGHT);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gameObjects.add(new EnemyTank(get_place_x(18) + i * SPACE * 3, get_place_y(1) + j * SPACE * 2, Direction.LEFT, 1, eTankImage));
                    }
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gameObjects.add(new EnemyTank(get_place_x(18) + i * SPACE * 3, get_place_y(14) + j * SPACE * 2, Direction.LEFT, 1, eTankImage));
                    }
                }
            }
            break;
            case 3: {
                play_zone = new int[]{10, 30, 26 * SPACE, 18 * SPACE};

                int[][] wall_list = new int[][]{{6, 3, 1, 5}, {6, 14, 1, 5}, {6, 4, 0, 4}, {6, 10, 0, 4}, {15, 3, 1, 5}, {15, 14, 1, 5}, {19, 4, 0, 4}, {19, 10, 0, 4},
                        {-1, -1, 0, 20}, {26, -1, 0, 20}, {0, -1, 1, 26}, {0, 18, 1, 26}};

                for (int[] wall_info : wall_list) {
                    gameObjects.add(new Wall(get_place_x(wall_info[0]), get_place_y(wall_info[1]), wall_info[2] == 1, wall_info[3], brickImage));
                }

                playerTank.tp(9 * SPACE, 14 * SPACE, Direction.RIGHT);
                gameObjects.add(new EnemyTank(get_place_x(2), get_place_y(2), Direction.LEFT, 3, 1, eTankImage, 1));
                gameObjects.add(new EnemyTank(get_place_x(24), get_place_y(2), Direction.LEFT, 3, 1, eTankImage, 1));
                gameObjects.add(new EnemyTank(get_place_x(2), get_place_y(15), Direction.LEFT, 3, 1, eTankImage, 1));
                gameObjects.add(new EnemyTank(get_place_x(24), get_place_y(15), Direction.LEFT, 3, 1, eTankImage, 1));

            }
            break;
            case 4: {
                play_zone = new int[]{SPACE, 2 * SPACE, 39 * SPACE, 16 * SPACE};

                int[][] wall_list = new int[][]{{6, 4, 0, 8}, {12, 2, 1, 6}, {12, 13, 1, 6}, {20, 4, 1, 5}, {20, 11, 1, 5}, {27, 6, 0, 4},
                        {-1, -1, 0, 18}, {39, -1, 0, 18}, {0, -1, 1, 39}, {0, 16, 1, 39}};

                for (int[] wall_info : wall_list) {
                    gameObjects.add(new Wall(get_place_x(wall_info[0]), get_place_y(wall_info[1]), wall_info[2] == 1, wall_info[3], brickImage));
                }
                playerTank.tp(get_place_x(3), get_place_x(6), Direction.RIGHT);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 4; j++) {
                        gameObjects.add(new EnemyTank(get_place_x(31) + i * SPACE * 2, get_place_y(3) + j * SPACE * 3, Direction.LEFT, 1, 1, eTankImage, 1));
                    }
                }
                gameObjects.add(new Building(get_place_x(36), get_place_y(4), 5, baseImage,1,240));
                gameObjects.add(new Building(get_place_x(36), get_place_y(11), 5, baseImage,1,240));
            }
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
            case KeyEvent.VK_Z:
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
            case KeyEvent.VK_Z:
                dirs[4] = false;
                break;
            case KeyEvent.VK_X:
                if (playerTank.isAlive() && playerTank.get_super_fire_amount() > 0) {
                    playerTank.super_fire();
                    playerTank.plus_super_fire_amount(-1);
                }
                break;
            case KeyEvent.VK_SPACE:
                if (!playerTank.isAlive()) {
                    reset(0);
                } else if (out_of_enemy) {
                    playerTank.plusHealth(1);
                    playerTank.plus_super_fire_amount(1);
                    reset(level + 1);
                }
                break;
            case KeyEvent.VK_T:
                playerTank.setHealth(64);
                playerTank.set_super_fire_amount(64);
                reset(4);
                break;

        }
    }
}
