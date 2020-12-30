package object;

import java.awt.*;

public abstract class MoveObject extends GameObject {

    protected int speed;
    protected Direction direction;
    protected int ac_delay;
    protected int ai_delay;

    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public MoveObject(int x, int y, int health, Image[] image) {
        super(x, y, health, image);
    }

    protected void setBase(int health) {
        alive = true;
        this.health = health;
        ac_delay = 0;
        ai_delay = 0;
        state = 0;
    }

    public void move() {
        oldX = x;
        oldY = y;
        switch (direction) {
            case RIGHT:
                x += speed / 2f;
                x += speed / 2f;
                break;
            case RIGHT_DOWN:
                x += speed / 2f;
                y += speed / 2f;
                break;
            case DOWN:
                y += speed / 2f;
                y += speed / 2f;
                break;
            case LEFT_DOWN:
                x -= speed / 2f;
                y += speed / 2f;
                break;
            case LEFT:
                x -= speed / 2f;
                x -= speed / 2f;
                break;
            case LEFT_UP:
                y -= speed / 2f;
                x -= speed / 2f;
                break;
            case UP:
                y -= speed / 2f;
                y -= speed / 2f;
                break;
            case RIGHT_UP:
                y -= speed / 2f;
                x += speed / 2f;
                break;
        }
    }
}
