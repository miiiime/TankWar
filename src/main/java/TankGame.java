import javax.swing.*;

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



    }
}
