import javax.swing.*;
import java.awt.*;

public class Tools {
    public static Image getImage(String fileName){
        return new ImageIcon("assets/images/"+fileName).getImage();
    }

}
