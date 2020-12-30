import object.Direction;
import object.GameObject;

import java.awt.*;

public class Bullet extends Tank {


    public Bullet(int x, int y, Direction direction, int team, Image[] image) {
        super(x, y, direction, team, image);
        speed=15;
        hitBox=new int[]{15,15,7,7};
    }

    public void move() {
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

    public boolean collisionBound(){
        boolean collision=false;
        if(x<0){
            collision=true;
        }else if (x>TankGame.getGameClient().getScreenWide()){
            collision=true;
        }
        if(y<-5){
            collision=true;
        }else if (y>TankGame.getGameClient().getScreenHeight()+5){
            collision=true;
        }
        return collision;
    }

    public boolean collision(){
        boolean isCollision = collisionBound();
        if (!isCollision)
            isCollision = collisionObject();
        if (isCollision) {
            alive=false;
        }

        return isCollision;
    }

    public boolean collisionObject() {
        for(GameObject object:TankGame.getGameClient().getGameObjects()){
            if(object instanceof Tank && team==((Tank) object).getTeam())
                continue;
            if(object!=this && getRectangle().intersects(object.getRectangle())){
                if(object instanceof Tank &&object.isAlive()) {
                    ((Tank) object).hitten(1);
                }
                return true;
            }
        }
        return false;
    }
    public void draw(Graphics g) {
        if(!alive)return;

        move();
        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
    }
}

