import object.Direction;
import object.GameObject;
import object.MoveObject;

import java.awt.*;
import java.util.ArrayList;

public class BulletAlter extends Bullet {
    private int frying_frame_end;
    private ArrayList<GameObject> hitten_target = new ArrayList<>();;

    public BulletAlter(int x, int y, Direction direction, int team, Image[] image, int damage) {
        this(x, y, direction, team, image, damage,17,25,1);
    }

    public BulletAlter(int x, int y, Direction direction, int team, Image[] image, int damage ,int pmx,int pmy,int ffe) {
        super(x, y, direction, team, image, damage);

        hitBox = new int[]{25, 25, 13, 13};
        this.pmx = new int[]{pmx};
        this.pmy = new int[]{pmy};
        frame=0;
        frame_delay=0;
        frying_frame_end=ffe;
    }
    public boolean collision(){
        boolean isCollision = collisionBound();
        if (!isCollision)
            isCollision = collisionObject();
        if (isCollision) {
            health=0;
            speed=0;
            state=3001;
            hitBox = new int[]{0, 0, 0, 0};
        }

        return isCollision;
    }

    public boolean collisionObject() {
        for(GameObject object:TankGame.getGameClient().getGameObjects()){
            if (object instanceof Animation)continue;
            if((object instanceof Tank || object instanceof Bullet) && team==((MoveObject) object).getTeam())
                continue;
            if(hitten_target.contains(object))
                continue;
            if(object instanceof Wall?new Rectangle((int) x - 3, (int) y - 3, 5, 5).intersects(object.getRectangle()):getRectangle().intersects(object.getRectangle())){
                if(object.isAlive()) {
                    hitten_target.add(object);
                    if(object instanceof Tank) {
                        ((Tank) object).hitten(damage);
                        Tools.playAudio("hitting_alter.wav",0.2);
                        Animation hitting = new Animation((int)object.getX(),(int)object.getY(),TankGame.getGameClient().getImage("super_fire"),0,1);
                        TankGame.getGameClient().addGameObject(hitting);
                    }else if(object instanceof Bullet)
                        ((Bullet) object).hitten(damage);
                }
                if(object instanceof Wall)return true;
            }
        }
        return false;
    }
    private void frying(){
        if(frame_delay<0)frame_delay=0;
        if(frame_delay==0) {
            frame++;
            frame_delay=2;
        }else frame_delay--;
        if (frame<0||frame>frying_frame_end)frame=0;
    }

    private void hitting(){
        if(frame<frying_frame_end){
            frame=frying_frame_end;
            frame_delay=0;
        }

        if(frame_delay<0)frame_delay=0;
        if(frame_delay==0) {
            frame++;
            frame_delay=1;
            if (frame>image.length-1)alive=false;
        }else frame_delay--;
    }

    public void hitten(int damage) {
        health -= damage;
        if (health < 0)
            health = 0;
    }
    public void draw(Graphics g) {
        if(!alive)return;
        if (state==3000) {
            move();
            frying();
        }
        if (state==3001)
            hitting();
        if(alive)g.drawImage(image[frame], (int) (x - pmx[0]+TankGame.getGameClient().getPtx()), (int) (y - pmy[0]+TankGame.getGameClient().getPty()), null);
    }
}
