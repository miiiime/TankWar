package object;

import java.awt.*;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double oldX;
    protected double oldY;
    protected Image[] image;
    protected int[] pmx;
    protected int[] pmy;
    protected int[] hitBox;
    protected boolean alive;
    protected int health;
    protected int state;
    protected int type;
    protected int frame;
    protected int frame_delay;

    protected int width;
    protected int height;

    public double getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void plusHealth(int health) {
        this.health += health;
    }

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void tp(int x, int y) {
        setX(x);
        setY(y);
    }

    public GameObject(int x, int y, int health, Image[] image) {
        this(x,y,health,image,0);
    }

    public GameObject(int x, int y, int health, Image[] image,int type) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.type = type;

        setBase(health);

        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
        hitBox = new int[]{width, height, width / 2, height / 2};
        setPicMid();
    }

    protected void setPicMid() {
        pmx = new int[image.length];
        pmy = new int[image.length];
        for (int i = 0; i < pmx.length; i++) {
                pmx[i] = image[i].getWidth(null) / 2;
                pmy[i] = image[i].getHeight(null) / 2;
        }
    }

    protected void setBase(int health) {
        alive = true;
        this.health = health;
        state = 0;
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) x - pmx[0], (int) y - pmy[0], width, height);
    }

    abstract public void draw(Graphics g);
}
