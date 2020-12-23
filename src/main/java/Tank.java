import object.Direction;
import object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Tank extends GameObject{
    private int speed;
    private Direction direction;
    private boolean[] dirs = new boolean[4];
    private boolean enemy;

    public Tank(int x, int y, Direction direction,Image[] image) {
        this(x, y, direction, false,image);

        hitBox=new int[]{30,30,15,15};
        pmx=new int[]{18,22,24,26,28,26,24,22};
        pmy=new int[]{24,22,18,22,24,26,28,26};
    }

    public Tank(int x, int y, Direction direction, boolean enemy,Image[] image) {
        super(x,y,image);
        this.direction = direction;
        this.speed = 6;
        this.enemy = enemy;
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
        oldX=x;
        oldY=y;
        switch (direction) {
            case RIGHT:
                x += speed;
                break;
            case RIGHT_DOWN:
                x += speed / 2f;
                y += speed / 2f;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT_DOWN:
                x -= speed / 2f;
                y += speed / 2f;
                break;
            case LEFT:
                x -= speed;
                break;
            case LEFT_UP:
                y -= speed / 2f;
                x -= speed / 2f;
                break;
            case UP:
                y -= speed;
                break;
            case RIGHT_UP:
                y -= speed / 2f;
                x += speed / 2f;
                break;
        }

        collision();
    }
    //碰撞
    public void collision(){
        if(x<0+hitBox[2]){
            x=hitBox[2];
        }else if (x>TankGame.getGameClient().getScreenWide()-hitBox[2]){
            x=TankGame.getGameClient().getScreenWide()-hitBox[2];
        }
        if(y<-5+hitBox[3]){
            y=hitBox[3];
        }else if (y>TankGame.getGameClient().getScreenHeight()+5-hitBox[3]){
            y=TankGame.getGameClient().getScreenHeight()-hitBox[3];
        }

        for(Wall wall:TankGame.getGameClient().getWalls()){
            if(getRectangle().intersects(wall.getRectangle())){
                x=oldX;
                y=oldY;
                return;
            }
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) x - 15, (int) y - 15, 30, 30);
    }

    public void draw(Graphics g) {
        if (!isStop()&&determineDirection()) {
            move();
        }
//        if(!enemy)
//        System.out.print(direction.ordinal());
        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
//        g.drawImage(getImage(), (int) x, (int) y, null);
    }
}
