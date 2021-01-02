import object.Direction;
import object.SuperFire;

import java.awt.*;

public class PlayerTank extends Tank implements SuperFire {
    private int super_fire_amount;

    public PlayerTank(int x, int y, Direction direction, Image[] image) {
        super(x, y, direction, 1, 0, image);

        hitBox = new int[]{30, 30, 15, 15};
    }

    public PlayerTank(int x, int y, Direction direction, int team, Image[] image) {
        super(x, y, direction, 1, team, image);
    }

    public PlayerTank(int x, int y, Direction direction, int health, int team, Image[] image) {
        super(x, y, direction, health, team, image);
    }

    protected void setPicMid() {
            pmx = new int[]{18, 22, 24, 26, 28, 26, 24, 22};
            pmy = new int[]{24, 22, 18, 22, 24, 26, 28, 26};
    }

    @Override
    public void super_fire() {
        state=235;
        ac_delay = 0;
    }

    @Override
    public int get_super_fire_amount() {
        return super_fire_amount;
    }

    @Override
    public void set_super_fire_amount(int v) {
        this.super_fire_amount=v;
    }

    @Override
    public void plus_super_fire_amount(int v) {
        this.super_fire_amount+=v;
    }


    private void fire_alter(){
        BulletAlter bullet = new BulletAlter((int) x, (int) y, direction, team, TankGame.getGameClient().getMissileImage(1),1);
        TankGame.getGameClient().addGameObject(bullet);
        state = 0;
        ac_delay = 20;
        Tools.playAudio("shoot_alter.wav",0.1);
    }

    protected void extra_ac(){
        if (state==235){
            fire_alter();
        }
    }

}
