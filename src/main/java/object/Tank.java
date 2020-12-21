package object;

import javax.swing.*;
import java.awt.*;

public class Tank {
    private double x;
    private double y;
    private int speed;
    private Direction direction;
    private boolean[] dirs = new boolean[4];
    private boolean enemy;
    private int pmx;
    private int pmy;

    public Tank(int x, int y, Direction direction) {
        this(x, y, direction, false);
    }

    public Tank(int x, int y, Direction direction, boolean enemy) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 5;
        this.enemy = enemy;
    }

    public Image getImage() {
        String name = enemy ? "etank" : "itank";
        if (direction == Direction.RIGHT) {
            pmx = 18;
            pmy = 24;
            return new ImageIcon("assets/images/" + name + "R.png").getImage();
        }
        if (direction == Direction.RIGHT_DOWN) {
            pmx = 22;
            pmy = 22;
            return new ImageIcon("assets/images/" + name + "RD.png").getImage();
        }
        if (direction == Direction.DOWN) {
            pmx = 24;
            pmy = 18;
            return new ImageIcon("assets/images/" + name + "D.png").getImage();
        }
        if (direction == Direction.LEFT_DOWN) {
            pmx = 26;
            pmy = 22;
            return new ImageIcon("assets/images/" + name + "LD.png").getImage();
        }
        if (direction == Direction.LEFT) {
            pmx = 28;
            pmy = 24;
            return new ImageIcon("assets/images/" + name + "L.png").getImage();
        }
        if (direction == Direction.LEFT_UP) {
            pmx = 26;
            pmy = 26;
            return new ImageIcon("assets/images/" + name + "LU.png").getImage();
        }
        if (direction == Direction.UP) {
            pmx = 24;
            pmy = 28;
            return new ImageIcon("assets/images/" + name + "U.png").getImage();
        }
        if (direction == Direction.RIGHT_UP) {
            pmx = 22;
            pmy = 26;
            return new ImageIcon("assets/images/" + name + "RU.png").getImage();
        }

        return null;
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
        for (boolean b : dirs) {
            if (b)
                return false;
        }
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
        switch (direction) {
            case RIGHT:
                x += speed;
                break;
            case RIGHT_DOWN:
                x += speed / 2;
                y += speed / 2;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT_DOWN:
                x -= speed / 2;
                y += speed / 2;
                break;
            case LEFT:
                x -= speed;
                break;
            case LEFT_UP:
                y -= speed / 2;
                x -= speed / 2;
                break;
            case UP:
                y -= speed;
                break;
            case RIGHT_UP:
                y -= speed / 2;
                x += speed / 2;
                break;
        }
    }

    public void draw(Graphics g) {
        if (!isStop()) {
            determineDirection();
            move();
        }
        g.drawImage(getImage(), (int) (x - pmx), (int) (y - pmy), null);
    }
}
