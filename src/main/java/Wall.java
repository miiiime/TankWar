import object.GameObject;

import javax.swing.*;
import java.awt.*;

public class Wall extends GameObject {

    private boolean horizontal;
    private int bricks;


    public Wall(int x, int y, boolean horizontal, int bricks, Image[] image) {
        super(x, y,-1, image);
        hitBox=new int[]{30,30,15,15};
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    public Rectangle getRectangle() {
        return horizontal ? new Rectangle((int) x - hitBox[2], (int) y - hitBox[3], (hitBox[0] * bricks), hitBox[1]) :
                new Rectangle((int) x - hitBox[2], (int) y - hitBox[3], hitBox[0], (hitBox[1] * bricks));
    }

    public void draw(Graphics g) {
        int ptx=TankGame.getGameClient().getPtx();
        int pty=TankGame.getGameClient().getPty();


        if (horizontal) {
            for (int i = 0; i < bricks; i++)
                g.drawImage(image[0], (int) x - pmx[0]+ptx + i * hitBox[0], (int) y - pmy[0]+pty, null);
        } else {
            for (int i = 0; i < bricks; i++)
                g.drawImage(image[0], (int) x - pmx[0]+ptx, (int) y - pmy[0] + i * hitBox[1]+pty, null);
        }

    }
}
