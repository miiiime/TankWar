import object.Direction;
import object.GameObject;
import object.MoveObject;

import java.awt.*;

public class Bullet extends MoveObject {
    int team;

    public Bullet(int x, int y, Direction direction, int team, Image[] image) {
        super(x, y, 1, image);
        this.team=team;
        super.direction=direction;
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

    public boolean collision(){
        boolean isCollision = collisionBound();
        if (!isCollision)
            isCollision = collisionObject();
        if (isCollision) {
            alive=false;
        }

        return isCollision;
    }

    public boolean collisionBound() {
        int[] play_zone=TankGame.getGameClient().getPlay_zone();
        if (x < play_zone[0]) {
            return true;
        } else if (x > play_zone[0] + play_zone[2]) {
            return true;
        }
        if (y < + play_zone[1]) {
            return true;
        } else if (y > play_zone[1] + play_zone[3]) {
            return true;
        }
        return false;
    }

    public boolean collisionObject() {
        for(GameObject object:TankGame.getGameClient().getGameObjects()){
            if(object instanceof Tank && team==((Tank) object).getTeam())
                continue;
            if(object!=this && getRectangle().intersects(object.getRectangle())){
                if(object.isAlive()) {
                    if(object instanceof Tank)
                        ((Tank) object).hitten(1);
                    else if(object instanceof Bullet)
                        ((Bullet) object).hitten(1);
                }
                return true;
            }
        }
        return false;
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
        if(!alive)return;

        move();
        g.drawImage(image[direction.ordinal()], (int) (x - pmx[direction.ordinal()]), (int) (y - pmy[direction.ordinal()]), null);
    }
}

