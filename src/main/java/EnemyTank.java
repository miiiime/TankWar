import object.Direction;

import java.awt.*;
import java.util.Random;

public class EnemyTank extends Tank {


    public EnemyTank(int x, int y, Direction direction, int team, Image[] image) {
        this(x, y, direction, 1, team, image, 0);
    }

    public EnemyTank(int x, int y, Direction direction, int health, int team, Image[] image) {
        this(x, y, direction, health, team, image, 0);
    }

    public EnemyTank(int x, int y, Direction direction, int health, int team, Image[] image, int type) {
        super(x, y, direction, health, team, image);
        this.type = type;
    }

    public boolean control() {

        return true;
    }

    public void control(int v) {
        dirs = new boolean[5];
        switch (v) {
            case 0:
                dirs[0] = true;
                break;
            case 1:
                dirs[0] = true;
                dirs[1] = true;
                break;
            case 2:
                dirs[1] = true;
                break;
            case 3:
                dirs[1] = true;
                dirs[2] = true;
                break;
            case 4:
                dirs[2] = true;
                break;
            case 5:
                dirs[2] = true;
                dirs[3] = true;
                break;
            case 6:
                dirs[3] = true;
                break;
            case 7:
                dirs[3] = true;
                dirs[0] = true;
                break;
            case 8:
                dirs[4] = true;
                break;
        }
    }

    private void ai() {
        if (state == 3) dirs[4] = false;
        if (ai_delay < 0) ai_delay = 0;
        if (ai_delay == 0) {    //==============
            Random random = new Random();

            if (state == 0) {
                state = 1;
                switch (type) {
                    case 0:
                        wander(random.nextInt(8), random.nextInt(10) + 11);
                        break;
                    case 1:
                        wander(random.nextInt(8), random.nextInt(8) + random.nextInt(8) + 4);
                        break;
                }
            } else if (state == 1) {
                state = 0;
                switch (type) {
                    case 0:
                        ai_delay += random.nextInt(10) + 1;
                        break;
                    case 1:
                        ai_delay += 2;
                        break;
                }
                if (search()) {
                    control(8);
                    ai_delay += 15;
                }
            }
        } else ai_delay--;      //==============
    }

    private void wander(int delay) {
        Random random = new Random();
        control(random.nextInt(8));
        ai_delay += delay;
    }

    private void wander(int v, int delay) {
        control(v);
        ai_delay += delay;
    }

    private boolean search() {
        double target_x = TankGame.getGameClient().getPlayerTank().getX();
        double target_y = TankGame.getGameClient().getPlayerTank().getY();
        double x_gap = target_x > x ? target_x - x : x - target_x;
        double y_gap = target_y > y ? target_x - y : y - target_y;
        switch (direction) {
            case RIGHT:
                if (target_x > x && y_gap < x_gap)
                    return true;
                break;
            case RIGHT_DOWN:
                if (target_x > x && target_y > y)
                    return true;
                break;
            case DOWN:
                if (target_y > y && x_gap < y_gap)
                    return true;
                break;
            case LEFT_DOWN:
                if (target_x < x && target_y > y)
                    return true;
                break;
            case LEFT:
                if (target_x < x && y_gap < x_gap)
                    return true;
                break;
            case LEFT_UP:
                if (target_x < x && target_y < y)
                    return true;
                break;
            case UP:
                if (target_y < y && x_gap < y_gap)
                    return true;
                break;
            case RIGHT_UP:
                if (target_x > x && target_y < y)
                    return true;
                break;
        }

        return false;
    }

    public boolean collision() {
        if (super.collision()) {
            wander(0);
            return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        ai();

        super.draw(g);
    }
}
