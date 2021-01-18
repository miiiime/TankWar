import object.Direction;
import object.GameObject;
import object.MoveObject;

import java.awt.*;

public class Building extends MoveObject {

    public Building(int x, int y, int health, Image[] image) {
        this(x, y, health, image, 1);
    }

    public Building(int x, int y, int health, Image[] image, int team) {
        this(x, y, health, image, team,100);

    }

    public Building(int x, int y, int health, Image[] image, int team ,int delay) {
        super(x, y, health, image);
        ac_delay=delay;
        this.team=team;
    }

    private void ai(){
        if (ai_delay < 0) ai_delay = 0;
        if (ai_delay == 0) {
            if(test_action_space(new Rectangle((int) x - 20, (int) y - 20, 40,40))){
                Tank tank = new EnemyTank((int)x, (int)y, Direction.LEFT, 1, team, TankGame.getGameClient().getImage("eTank"), 0);
                TankGame.getGameClient().addGameObject(tank);
                ai_delay+=ac_delay;
            }
        } else ai_delay--;
    }

    private boolean test_action_space(Rectangle action_space){
        for (GameObject object : TankGame.getGameClient().getGameObjects()) {
            if (object instanceof Animation)continue;
            if (object instanceof Building)continue;
            if (action_space.intersects(object.getRectangle())) {
                return false;
            }
        }
        return true;
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
        ai();
        g.drawImage(image[0], (int) x - pmx[0]+TankGame.getGameClient().getPtx(), (int) y - pmy[0]+TankGame.getGameClient().getPty(), null);
    }
}
