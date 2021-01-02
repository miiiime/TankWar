import object.GameObject;

import java.awt.*;

public class Animation extends GameObject {
    int an_delay;

    public Animation(int x, int y, Image[] image,int type) {
        this(x, y, image,type,0);
    }
    public Animation(int x, int y, Image[] image,int type,int delay) {
        super(x, y, -1, image,type);
        this.an_delay=delay;
        frame=0;
        frame_delay=0;
    }

    public Rectangle getRectangle() {
        return new Rectangle(0,0,0,0);
    }

    @Override
    public void draw(Graphics g) {
        if(frame_delay<0)frame_delay=0;
        if(frame_delay==0){
            frame++;
            frame_delay+=an_delay;
        }else frame_delay--;
        if (frame>image.length) {
            switch (type){
                case 0:
                    alive = false;
                    break;
                case 1:
                    frame = 1;
                    break;
            }
        }
        if(alive)g.drawImage(image[frame-1], (int) (x - pmx[frame-1]), (int) (y - pmy[frame-1]), null);
    }
}
