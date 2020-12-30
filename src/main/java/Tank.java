import object.Direction;
import object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Tank extends GameObject {
    protected int speed;
    protected Direction direction;
    private boolean[] dirs = new boolean[5];
    protected boolean enemy;

    public Tank(int x, int y, Direction direction, Image[] image) {
        this(x, y, direction, 1, false, image);

        hitBox = new int[]{30, 30, 15, 15};
    }

    public Tank(int x, int y, Direction direction, boolean enemy, Image[] image) {
        this(x, y, direction, 1, enemy, image);
    }

    public Tank(int x, int y, Direction direction, int health, boolean enemy, Image[] image) {
        super(x, y, health, image);
        this.direction = direction;
        this.speed = 6;
        this.enemy = enemy;
    }

    protected void setPicMid() {
        if (enemy || this instanceof Bullet)
            super.setPicMid();
        else {
            pmx = new int[]{18, 22, 24, 26, 28, 26, 24, 22};
            pmy = new int[]{24, 22, 18, 22, 24, 26, 28, 26};
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean[] getDirs() {
        return dirs;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isStop() {
        for (int i = 0; i < 4; i++) {
            if (dirs[i])
                return false;
        }

//      for (boolean b : dirs) {
//            if (b)
//                return false;
//        }
        return true;
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

    public void collision() {
        if (collisionBound()) {
            return;
        }

        for (GameObject object : TankGame.getGameClient().getGameObjects()) {
            if (object != this && !(object instanceof Bullet) && getRectangle().intersects(object.getRectangle())) {
                x = oldX;
                y = oldY;
                return;
            }
        }

        oldX = x;
        oldY = y;
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) x - 15, (int) y - 15, 30, 30);
    }

    public void fire() {
        if (delay == 0) {

            if (state == 0) {
                state = 3;
            } else if (state == 3) {
                Bullet bullet = new Bullet((int) x, (int) y, direction, enemy, TankGame.getGameClient().getMissileImage());
                TankGame.getGameClient().addGameObject(bullet);
                state = 0;
            }
            delay += 10;
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
        if (delay == 0) {
            if (state == 0 && !isStop() && determineDirection())
                move();
            if (state == 3)
                fire();
        } else if (delay > 0) {
            delay--;
        }

        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
    }
}
