import object.Direction;
import object.GameObject;
import object.MoveObject;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Tank extends MoveObject {
    protected boolean[] dirs = new boolean[5];


    public boolean[] getDirs() {
        return dirs;
    }

    public void tp(int x, int y,Direction d) {
        super.tp(x, y);
        setDirection(d);
    }

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
        int[] play_zone=TankGame.getGameClient().getPlay_zone();
        if (x < play_zone[0] + hitBox[2]) {
            x = play_zone[0] + hitBox[2];
            return true;
        } else if (x > play_zone[0] + play_zone[2] - hitBox[2]) {
            x = play_zone[0] + play_zone[2] - hitBox[2];
            return true;
        }
        if (y < + play_zone[1] + hitBox[3]) {
            y = + play_zone[1] + hitBox[3];
            return true;
        } else if (y > play_zone[1] + play_zone[3] - hitBox[3]) {
            y = play_zone[1] + play_zone[3] - hitBox[3];
            return true;
        }
        return false;
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
            ac_delay += 10;
        } else if (state == 3) {
                Bullet bullet = new Bullet((int) x, (int) y, direction, team, TankGame.getGameClient().getMissileImage(0),1);
                TankGame.getGameClient().addGameObject(bullet);
            state = 0;
            ac_delay = 5;
            Tools.playAudio("shoot.wav",0.2);
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

    protected void extra_ac(){}

    public void draw(Graphics g) {
            int base_state=state;

            if (ac_delay < 0)
                ac_delay = 0;
            if (ac_delay == 0) {
                if (state == 0 || state == 1) {
                    control();
                    if (dirs[4]){
                        fire();
                    }
                }
                if (state == 1 && determineDirection())
                    move();
                if (state == 3 && base_state==state)
                    fire();
                extra_ac();
            } else ac_delay--;
        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
    }
}
