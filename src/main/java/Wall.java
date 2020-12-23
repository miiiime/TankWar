import object.GameObject;

import javax.swing.*;
import java.awt.*;

public class Wall extends GameObject {

    private boolean horizontal;
    private int bricks;


    public Wall(int x, int y, boolean horizontal, int bricks, Image[] image) {
        super(x, y, image);
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    public void draw(Graphics g) {
        if (horizontal) {
            for (int i = 0; i < bricks; i++)
                g.drawImage(image[0], (int) x-pmx[0] + i * width, (int) y-pmy[0], null);
        } else {
            for (int i = 0; i < bricks; i++)
                g.drawImage(image[0], (int) x-pmx[0], (int) y-pmy[0] + i * height, null);
        }

    }
}
