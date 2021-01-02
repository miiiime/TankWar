import object.Direction;
import object.GameObject;
import object.MoveObject;

import java.awt.*;

public class Bullet extends MoveObject {
    protected int damage;

    public int getTeam() {
        return team;
    }
    public void setTeam(int team) {
        this.team = team;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Bullet(int x, int y, Direction direction, int team, Image[] image) {
        this(x,y,direction,team,image,1);
    }

    public Bullet(int x, int y, Direction direction, int team, Image[] image ,int damage) {
        super(x, y, 1, image);
        this.team = team;
        this.direction = direction;
        this.damage = damage;
        state=3000;

        speed = 15;
        hitBox = new int[]{15, 15, 7, 7};
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
            health=0;
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
            if (object instanceof Animation)continue;
            if((object instanceof Tank || object instanceof Bullet) && team==((MoveObject) object).getTeam())
                continue;
            if(object!=this && getRectangle().intersects(object.getRectangle())){
                if(object.isAlive()) {
                    if(object instanceof Tank) {
                        ((Tank) object).hitten(damage);
                    Tools.playAudio("hitting.wav",0.2);
                        Animation hitting = new Animation((int)x,(int)y,TankGame.getGameClient().getImage("explosion"),0,0);
                        TankGame.getGameClient().addGameObject(hitting);
                    }else if(object instanceof Bullet)
                        ((Bullet) object).hitten(damage);
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

