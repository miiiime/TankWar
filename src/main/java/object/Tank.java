package object;

import javax.swing.*;
import java.awt.*;

public class Tank {
    private int x;
    private int y;
    private int speed;
    private Direction direction;
    private boolean[] dirs=new boolean[4];

    public Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed=8;
    }

    public Image getImage(){
        if(direction==Direction.RIGHT)
            return new ImageIcon("assets/images/itankR.png").getImage();
        if(direction==Direction.RIGHT_DOWN)
            return new ImageIcon("assets/images/itankRD.png").getImage();
        if(direction==Direction.DOWN)
            return new ImageIcon("assets/images/itankD.png").getImage();
        if(direction==Direction.LEFT_DOWN)
            return new ImageIcon("assets/images/itankLD.png").getImage();
        if(direction==Direction.LEFT)
            return new ImageIcon("assets/images/itankL.png").getImage();
        if(direction==Direction.LEFT_UP)
            return new ImageIcon("assets/images/itankLU.png").getImage();
        if(direction==Direction.UP)
            return new ImageIcon("assets/images/itankU.png").getImage();
        if(direction==Direction.RIGHT_UP)
            return new ImageIcon("assets/images/itankRU.png").getImage();

        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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

    public boolean isStop(){
        for(boolean b:dirs) {
            if (b)
                return false;
        }
        return true;
    }

    public boolean determineDirection(){
        Direction base=direction;
        if(dirs[0]&&!dirs[1]&&!dirs[2]&&!dirs[3]) direction=Direction.RIGHT;
        else if(dirs[0]&&dirs[1]&&!dirs[2]&&!dirs[3]) direction=Direction.RIGHT_DOWN;
        else if(!dirs[0]&&dirs[1]&&!dirs[2]&&!dirs[3]) direction=Direction.DOWN;
        else if(!dirs[0]&&dirs[1]&&dirs[2]&&!dirs[3]) direction=Direction.LEFT_DOWN;
        else if(!dirs[0]&&!dirs[1]&&dirs[2]&&!dirs[3]) direction=Direction.LEFT;
        else if(!dirs[0]&&!dirs[1]&&dirs[2]&&dirs[3]) direction=Direction.LEFT_UP;
        else if(!dirs[0]&&!dirs[1]&&!dirs[2]&&dirs[3]) direction=Direction.UP;
        else if(dirs[0]&&!dirs[1]&&!dirs[2]&&dirs[3]) direction=Direction.RIGHT_UP;
        return base==direction;
    }

    public void move(){
        switch (direction)
        {
            case RIGHT:
                x+=speed;
                break;
            case RIGHT_DOWN:
                x+=speed/2;
                y+=speed/2;
                break;
            case DOWN:
                y+=speed;
                break;
            case LEFT_DOWN:
                x-=speed/2;
                y+=speed/2;
                break;
            case LEFT:
                x-=speed;
                break;
            case LEFT_UP:
                y-=speed/2;
                x-=speed/2;
                break;
            case UP:
                y-=speed;
                break;
            case RIGHT_UP:
                y-=speed/2;
                x+=speed/2;
                break;
        }
    }

    public void draw(Graphics g){
        if(!isStop()&&determineDirection())move();
        g.drawImage(getImage(),x,y,null);
    }
}
