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

    protected int width;
    protected int height;


    public GameObject(int x, int y,int health, Image[] image) {
        this.x = x;
        this.y = y;
        this.image = image;

        alive=true;
        this.health=health;

        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
        hitBox= new int[]{width, height, width/2, height/2};
        setPicMid();
    }

    public boolean isAlive() {
        return alive;
    }

    protected void setPicMid(){
        pmx = new int[image.length];
        for (int i = 0; i < pmx.length; i++) {
            if (pmx[i] == 0)
                pmx[i] = width / 2;
        }
        pmy = new int[image.length];
        for (int i = 0; i < pmy.length; i++) {
            if (pmy[i] == 0)
                pmy[i] = width / 2;
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) x - pmx[0], (int) y - pmy[0], width, height);
    }

    abstract public void draw(Graphics g);
}
