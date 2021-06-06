package src.com.elsys.object;

import src.com.elsys.Commons;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;


public class Player extends src.com.elsys.object.Object {

    private int width;

    public Player() {

        initPlayer();
    }

    private void initPlayer() {

        String playerImg = Paths.get(".").toAbsolutePath().normalize().toString() + ("/images/player.png");
        ImageIcon ii = new ImageIcon(playerImg);
        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        setX(585);
        setY(380);
    }

    public void act() {

        x += move;
        if (x <= 2) {
            x = 2;
        }else if (x >= Commons.BOARD_WIDTH - 2 * width) {
            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            move = -2;
        }else if (key == KeyEvent.VK_RIGHT) {
            move = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            move = 0;
        }else if (key == KeyEvent.VK_RIGHT) {
            move = 0;
        }
    }
}
