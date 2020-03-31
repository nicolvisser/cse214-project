import java.util.ArrayList;
import java.util.Iterator;

public class EnemyGroup extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    ArrayList<Enemy> enemies;
    Rectangle canvas;

    public EnemyGroup(Rectangle canvas) {
        super();
        this.canvas = canvas;
        enemies = new ArrayList<>();
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void remove(Enemy enemy) {
        enemies.remove(enemy);
    }

    public boolean hasEnemies() {
        return enemies.size() > 0;
    }

    public void populateInSquareFormation(Vector2D position, int numEnemiesOnASide) {
        this.position = position;

        int radius = Enemy.DEFAULT_COLLISION_RADIUS;
        int spacing = 3;

        double x = position.x + radius;
        for (int i = 0; i < numEnemiesOnASide; i++) {
            double y = position.y + radius;
            for (int j = 0; j < numEnemiesOnASide; j++) {
                Enemy enemy = new Enemy(canvas, new Vector2D(x, y), 3 * Math.PI / 2);
                enemies.add(enemy);
                y += 2 * radius + spacing;
            }
            x += 2 * radius + spacing;
        }
    }

    public void populateInCircleFormation(Vector2D position, int numEnemies, int radius) {
        this.position = position;

        for (double theta = 0; theta < 2 * Math.PI; theta += 2 * Math.PI / numEnemies) {
            double x = position.x + radius * Math.cos(theta);
            double y = position.y + radius * Math.sin(theta);
            Enemy enemy = new Enemy(canvas, new Vector2D(x, y), 3 * Math.PI / 2);
            enemies.add(enemy);
        }

    }

    @Override
    public void draw() {
        for (Enemy enemy : enemies) {
            enemy.draw();
        }
    }

    @Override
    public void render(double dt) {
        super.render(dt);

        // calculate change in position of group object
        double dx = velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        double dy = velocity.y * dt + 0.5 * acceleration.y * dt * dt;

        Iterator<Enemy> itr = enemies.iterator();
        while (itr.hasNext()) {
            Enemy enemy = itr.next();

            // render new position of enemy, independent of group
            enemy.render(dt);

            // add group movement effect to enemy
            enemy.translateX(dx);
            enemy.translateY(dy);

            // remove enemy if no longer alive
            if (enemy.state == Enemy.EnemyState.DEAD) {
                itr.remove();
            }
        }
    }

    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;
        for (Enemy enemy : enemies) {
            for (Missile missile : missiles) {
                points += enemy.handleCollisionWithMissile(missile);
            }
        }
        return points;
    }

    public boolean isTouchingBottomOrShooter(Shooter shooter) {
        for (Enemy enemy : enemies) {
            if (enemy.isTouchingBottomOrShooter(shooter)) {
                return true;
            }
        }
        return false;
    }

    public Enemy getRandomEnemy() {
        if (enemies.size() == 0) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemies.size());
            return enemies.get(randomIndex);
        }
    }

}