package object;

import java.awt.*;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected Image[] image;
    protected int[] pmx;
    protected int[] pmy;

    protected int width;
    protected int height;


    public GameObject(int x, int y, Image[] image) {
        this.x = x;
        this.y = y;
        this.image = image;

        width = image[0].getWidth(null);
        height = image[0].getHeight(null);

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

    abstract public void draw(Graphics g);
}
