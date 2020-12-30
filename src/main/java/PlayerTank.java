import object.Direction;

import java.awt.*;

public class PlayerTank extends Tank{
    public PlayerTank(int x, int y, Direction direction, Image[] image) {
        super(x, y, direction, 3, 0, image);

        hitBox = new int[]{30, 30, 15, 15};
    }

    public PlayerTank(int x, int y, Direction direction, int team, Image[] image) {
        super(x, y, direction, 1, team, image);
    }

    public PlayerTank(int x, int y, Direction direction, int health, int team, Image[] image) {
        super(x, y, direction, health, team, image);
    }

    protected void setPicMid() {
            pmx = new int[]{18, 22, 24, 26, 28, 26, 24, 22};
            pmy = new int[]{24, 22, 18, 22, 24, 26, 28, 26};
    }
}
