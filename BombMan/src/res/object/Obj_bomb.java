package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_bomb extends SuperObject {
    public Obj_bomb() {
        name = "bomb";
        try {
            img_path = "/object/bomb.png";
            image = ImageIO.read(getClass().getResourceAsStream(img_path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
