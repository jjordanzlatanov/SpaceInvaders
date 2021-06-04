package src.com.elsys;

import javax.swing.*;
import java.awt.*;

public class SpaceInvaders extends JFrame  {

    public SpaceInvaders() {}

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            SpaceInvaders game = new SpaceInvaders();

            game.add(new Board());
            game.setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
            game.setTitle("Space Invaders");

            game.setLocationRelativeTo(null);
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });
    }
}
