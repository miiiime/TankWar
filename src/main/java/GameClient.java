import javax.swing.*;
import java.awt.*;

public class GameClient extends JComponent
{
    private int screenWide;
    private int screenHeight;

    GameClient(){
        this(800,600);
    }

    GameClient(int screenWide,int screenHeight){
        this.screenWide=screenWide;
        this.screenHeight=screenHeight;
        this.setPreferredSize(new Dimension(screenWide,screenHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(new ImageIcon("assets/images/etankD.png").getImage(),100,100,null);
    }
}
