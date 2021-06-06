package src.com.elsys.object;

import javax.swing.*;
import java.nio.file.Paths;

public class BulletPlayer extends src.com.elsys.object.Object {

    public BulletPlayer() {
    }

    public BulletPlayer(int x, int y) {

        initBulletPlayer(x, y);
    }

    private void initBulletPlayer(int x, int y) {

        String shotImg = Paths.get(".").toAbsolutePath().normalize().toString() + ("/images/bulletPlayer.png");
        ImageIcon ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        setX(x + 6);
        setY(y - 1);
    }
}
