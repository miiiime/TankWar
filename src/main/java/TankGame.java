import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankGame {

    public static void main(String[] args) {
        JFrame frame=new JFrame();
        GameClient gameClient=new GameClient();
        frame.add(gameClient);
        frame.setTitle("Tank War");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        gameClient.repaint();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                gameClient.keyPressed(e);
//                System.out.print(e.getKeyCode());

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }
}
