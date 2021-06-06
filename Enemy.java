package src.com.elsys.object;

import javax.swing.*;
import java.nio.file.Paths;

public class Enemy extends src.com.elsys.object.Object {

    private BulletEnemy bulletEnemy;

    public Enemy(int x, int y) {

        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {

        this.x = x;
        this.y = y;

        bulletEnemy = new BulletEnemy(x, y);
        String enemyImg = Paths.get(".").toAbsolutePath().normalize().toString() + ("/images/enemy.png");
        ImageIcon ii = new ImageIcon(enemyImg);
        setImage(ii.getImage());
    }

    public void act(int direction) {
        this.x += direction;
    }

    public BulletEnemy getBullet() {

        return bulletEnemy;
    }

    public class BulletEnemy extends src.com.elsys.object.Object {

        private boolean destroyed;

        public BulletEnemy(int x, int y) {

            initBulletEnemy(x, y);
        }

        private void initBulletEnemy(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            String bulletEnemyImg = Paths.get(".").toAbsolutePath().normalize().toString() + ("/images/bulletEnemy.png");
            ImageIcon ii = new ImageIcon(bulletEnemyImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}
