import object.Direction;
import object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Tank extends GameObject {
    protected int speed;
    protected Direction direction;
    protected boolean[] dirs = new boolean[5];
    protected int team;

    public Tank(int x, int y, Direction direction, Image[] image) {
        this(x, y, direction, 1, 0, image);

        hitBox = new int[]{30, 30, 15, 15};
    }

    public Tank(int x, int y, Direction direction, int team, Image[] image) {
        this(x, y, direction, 1, team, image);
    }

    public Tank(int x, int y, Direction direction, int health, int team, Image[] image) {
        super(x, y, health, image);
        this.direction = direction;
        this.speed = 6;
        this.team = team;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean[] getDirs() {
        return dirs;
    }

    public int getTeam() {
        return team;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean control() {
        for (int i = 0; i < 4; i++) {
            if (dirs[i]) {
                if (state == 0)
                    state = 1;
                return true;
            }
        }
        if (state == 1)
            state = 0;
        return false;
    }

    public boolean determineDirection() {
        Direction base = direction;
        if (dirs[0] && !dirs[1] && !dirs[2] && !dirs[3]) direction = Direction.RIGHT;
        else if (dirs[0] && dirs[1] && !dirs[2] && !dirs[3]) direction = Direction.RIGHT_DOWN;
        else if (!dirs[0] && dirs[1] && !dirs[2] && !dirs[3]) direction = Direction.DOWN;
        else if (!dirs[0] && dirs[1] && dirs[2] && !dirs[3]) direction = Direction.LEFT_DOWN;
        else if (!dirs[0] && !dirs[1] && dirs[2] && !dirs[3]) direction = Direction.LEFT;
        else if (!dirs[0] && !dirs[1] && dirs[2] && dirs[3]) direction = Direction.LEFT_UP;
        else if (!dirs[0] && !dirs[1] && !dirs[2] && dirs[3]) direction = Direction.UP;
        else if (dirs[0] && !dirs[1] && !dirs[2] && dirs[3]) direction = Direction.RIGHT_UP;
        return base == direction;
    }

    public void move() {
        oldX = x;
        oldY = y;
        switch (direction) {
            case RIGHT:
                x += speed / 2f;
                collision();
                x += speed / 2f;
                break;
            case RIGHT_DOWN:
                x += speed / 2f;
                collision();
                y += speed / 2f;
                break;
            case DOWN:
                y += speed / 2f;
                collision();
                y += speed / 2f;
                break;
            case LEFT_DOWN:
                x -= speed / 2f;
                collision();
                y += speed / 2f;
                break;
            case LEFT:
                x -= speed / 2f;
                collision();
                x -= speed / 2f;
                break;
            case LEFT_UP:
                y -= speed / 2f;
                collision();
                x -= speed / 2f;
                break;
            case UP:
                y -= speed / 2f;
                collision();
                y -= speed / 2f;
                break;
            case RIGHT_UP:
                y -= speed / 2f;
                collision();
                x += speed / 2f;
                break;
        }

        collision();
    }

    //碰撞
    public boolean collision() {
        boolean isCollision = collisionBound();
        if (!isCollision)
            isCollision = collisionObject();
        if (isCollision) {
            x = oldX;
            y = oldY;
        }
        oldX = x;
        oldY = y;

        return isCollision;
    }

    public boolean collisionBound() {
        boolean collision = false;
        if (x < 0 + hitBox[2]) {
            x = hitBox[2];
            collision = true;
        } else if (x > TankGame.getGameClient().getScreenWide() - hitBox[2]) {
            x = TankGame.getGameClient().getScreenWide() - hitBox[2];
            collision = true;
        }
        if (y < -5 + hitBox[3]) {
            y = -5 + hitBox[3];
            collision = true;
        } else if (y > TankGame.getGameClient().getScreenHeight() + 5 - hitBox[3]) {
            y = TankGame.getGameClient().getScreenHeight() + 5 - hitBox[3];
            collision = true;
        }
        return collision;
    }

    public boolean collisionObject() {
        for (GameObject object : TankGame.getGameClient().getGameObjects()) {
            if (object != this && !(object instanceof Bullet) && getRectangle().intersects(object.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) x - hitBox[2], (int) y - hitBox[3], hitBox[0], hitBox[1]);
    }

    public void fire() {
        if (state == 0 || state == 1) {
            state = 3;
            ac_delay += 5;
        } else if (state == 3) {
            Bullet bullet = new Bullet((int) x, (int) y, direction, team, TankGame.getGameClient().getMissileImage());
            TankGame.getGameClient().addGameObject(bullet);
            state = 0;
            ac_delay = 5;
        }
    }

    public void hitten() {
        hitten(1);
    }

    public void hitten(int damage) {
        health -= damage;
        if (health < 0)
            health = 0;
        if (health == 0)
            alive = false;
    }

    public void draw(Graphics g) {
        if (ac_delay < 0) ac_delay = 0;
        if (ac_delay == 0) {
            if (state == 0 || state == 1) {
                control();
                if (dirs[4])
                    fire();
            }
            if (state == 1 && determineDirection())
                move();
            if (state == 3)
                fire();
        } else ac_delay--;
        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
    }
}
