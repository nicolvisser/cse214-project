import java.util.ArrayList;

public class EnemyGroup extends Enemy {

    private static final long serialVersionUID = 1L;

    ArrayList<Enemy> enemies;

    public EnemyGroup() {
        super();
        enemies = new ArrayList<>();
    }

    public void populateInSquareFormation(Vector2D position, int numEnemiesOnASide) {
        this.position = position;

        int radius = Enemy.DEFAULT_COLLISION_RADIUS;
        int spacing = 10;

        double x = position.x + radius;
        for (int i = 0; i < numEnemiesOnASide; i++) {
            double y = position.y + radius;
            for (int j = 0; j < numEnemiesOnASide; j++) {
                Enemy enemy = new Enemy(new Vector2D(x, y), 3 * Math.PI / 2);
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
            Enemy enemy = new Enemy(new Vector2D(x, y), 3 * Math.PI / 2);
            enemies.add(enemy);
        }

    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void remove(Enemy enemy) {
        enemies.remove(enemy);
    }

    @Override
    public boolean isAlive() {
        return enemies.size() > 0;
    }

    @Override
    public void draw() {
        for (Enemy enemy : enemies) {
            enemy.draw();
        }
    }

    @Override
    public void renderStep(double dt) {
        super.renderStep(dt);

        // calculate change in position of group object
        double dx = velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        double dy = velocity.y * dt + 0.5 * acceleration.y * dt * dt;

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.renderStep(dt); // render new position of enemy independent of group

            // add group movement effect to enemy
            enemy.translateX(dx);
            enemy.translateY(dy);

            // remove enemy if no longer alive
            if (!enemy.isAlive()) {
                enemies.remove(enemy);
                i--;
            }
        }
    }

    @Override
    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;

        for (Enemy enemy : enemies) {
            for (Missile missile : missiles) {
                if (enemy.isCollidingWith(missile)) {
                    points += missile.missileDamage; // Todo Better points system than just missile damage
                    enemy.takeDamage(missile.missileDamage);
                    missile.takeDamage(Integer.MAX_VALUE);
                    break;
                }
            }
        }

        return points;
    }

    public int handleMissileBurst(Vector2D origin, double burstRadius) {
        int points = 0;

        for (Enemy enemy : enemies) {
            if (distanceBetween(enemy.position, origin) < burstRadius) {
                points += 100; // Todo Better points system than just missile damage
                enemy.takeDamage(100); // Todo fix hardcoding of missile damage
                break;
            }
        }

        return points;
    }

    @Override
    public boolean isTouchingBottomOrShooter(Shooter shooter) {
        for (Enemy enemy : enemies) {
            if (enemy.isTouchingBottomOrShooter(shooter)) {
                return true;
            }
        }
        return false;
    }

}