package src.com.elsys;

import src.com.elsys.object.BulletPlayer;
import src.com.elsys.object.Enemy;
import src.com.elsys.object.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private int stage = 1, direction = 1, deaths = 0, gridX, gridY;
    private List<Enemy> enemies;

    private Player player;
    private BulletPlayer bulletPlayer;

    private boolean inGame = true;
    private String finalMessage = "Game Over";
    private Timer timer;

    public Board() {

        initBoard();
        gameInit();
    }

    private void initBoard() {

        gridX = Commons.BOARD_WIDTH;
        gridY = Commons.BOARD_HEIGHT;
        addKeyListener(new TAdapter());
        setFocusable(true);

        setBackground(Color.yellow);

        timer = new Timer(1, new GameCycle());
        timer.start();

        gameInit();

    }

    private void gameInit() {
        player = new Player();
        bulletPlayer = new BulletPlayer();
        enemies = new ArrayList<>();

        for (int i = 0; i < stage ; i++) {
            for (int j = 0; j < 8; j++) {

                Enemy enemy = new Enemy(500 + getRandomNumberInts(20,30) * j, 10 + 20 * i);
                enemies.add(enemy);
            }
        }
    }

    private void drawing(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, gridX, gridY);

        if (inGame) {
            g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawEnemy(g);
            drawPlayer(g);
            drawBulletPlayer(g);
            drawBulletEnemy(g);

        }
        if(!inGame) {
            if (timer.isRunning()) {
                timer.stop();
            }
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    private void drawEnemy(Graphics g) {

        for (Iterator<Enemy> i1 = enemies.iterator(); i1.hasNext();) {

            Enemy enemy = i1.next();
            if (enemy.isVisible()) {
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {
                enemy.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }

    private void drawBulletPlayer(Graphics g) {

        if (bulletPlayer.isVisible()) {
            g.drawImage(bulletPlayer.getImage(), bulletPlayer.getX(), bulletPlayer.getY(), this);
        }
    }

    private void drawBulletEnemy(Graphics g) {

        for (Iterator<Enemy> i1 = enemies.iterator(); i1.hasNext();) {
            Enemy enemy = i1.next();

            Enemy.BulletEnemy b = enemy.getBullet();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 35, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 35, Commons.BOARD_WIDTH - 100, 50);

        Font small = new Font("Ariel", Font.BOLD, 36);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(finalMessage, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(finalMessage)) / 2,
                Commons.BOARD_WIDTH / 2);
    }

    private void update() {

        if(deaths == 8 && stage == 1){
            stage++;
            gameInit();
        }else if(deaths == 24 && stage == 2){
            stage++;
            gameInit();
        }else if(deaths == 48 && stage == 3){
            inGame = false;
            timer.stop();
            finalMessage = "Game won!";
        }

        player.act();

        if (bulletPlayer.isVisible()) {

            int shotX = bulletPlayer.getX();
            int shotY = bulletPlayer.getY();

            for (Iterator<Enemy> i1 = enemies.iterator(); i1.hasNext();) {
                Enemy enemy = i1.next();
                int enemyX = enemy.getX();
                int enemyY = enemy.getY();

                if (enemy.isVisible() && bulletPlayer.isVisible()) {
                    if (shotX >= (enemyX) && shotX <= (enemyX + 12) && shotY >= (enemyY) && shotY <= (enemyY + 10)) {
                        enemy.setDying(true);
                        bulletPlayer.die();
                        deaths++;
                    }
                }
            }

            if(bulletPlayer.getY() - 4 < 0) bulletPlayer.die();
            if(bulletPlayer.getY() - 4 >= 0) bulletPlayer.setY( bulletPlayer.getY() - 4);
        }
        Random t = new Random();

        for (Enemy enemy : enemies) {

            int shot = t.nextInt(20);
            Enemy.BulletEnemy bulletEnemy = enemy.getBullet();
            if (shot == 1 && enemy.isVisible() && bulletEnemy.isDestroyed()) {
                bulletEnemy.setDestroyed(false);
                bulletEnemy.setX(enemy.getX());
                bulletEnemy.setY(enemy.getY());
            }

            int bulletX = bulletEnemy.getX();
            int bulletY = bulletEnemy.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bulletEnemy.isDestroyed()) {
                if (bulletX >= (playerX) && bulletX <= (playerX + Commons.PLAYER_WIDTH) && bulletY >= (playerY) && bulletY <= (playerY + Commons.PLAYER_HEIGHT)) {
                    player.setDying(true);
                    bulletEnemy.setDestroyed(true);
                }
            }
            if (!bulletEnemy.isDestroyed()) {
                bulletEnemy.setY(bulletEnemy.getY() + 1);
                if (bulletEnemy.getY() >= Commons.GROUND - 5) {
                    bulletEnemy.setDestroyed(true);
                }
            }
        }

        for (Iterator<Enemy> i0 = enemies.iterator(); i0.hasNext();) {
            Enemy enemy = i0.next();
            int x = enemy.getX();

            if (x >= Commons.BOARD_WIDTH - 20 && direction != -1) {
                direction = -1;
                Iterator<Enemy> i1 = enemies.iterator();

                while (i1.hasNext()) {
                    Enemy a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= 10 && direction != 1) {
                direction = 1;
                Iterator<Enemy> i2 = enemies.iterator();

                while (i2.hasNext()) {
                    Enemy a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();){

            Enemy enemy = it.next();

            if (enemy.isVisible()) {
                int y = enemy.getY();
                if (y > Commons.GROUND) {
                    inGame = false;
                    finalMessage = "Invasion!";
                }
                enemy.act(direction);
            }
        }
    }
    public static int getRandomNumberInts(int min, int max){
        Random random = new Random();
        return random.ints(min,(max+1)).findFirst().getAsInt();
    }

    private void doGameCycle() {
        update();
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawing(g);
    }
    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame && !bulletPlayer.isVisible()) {
                bulletPlayer = new BulletPlayer(x, y);
            }
        }
    }
}
