import java.util.ArrayList;

public class EnemyGroup extends Enemy {

    private static final long serialVersionUID = 1L;

    enum Formation {
        SQUARE;
    }

    ArrayList<Enemy> enemies;

    public EnemyGroup() {
        super();
        enemies = new ArrayList<>();
    }

    public EnemyGroup(Vector2D position, Formation formation, int numEnemies) {
        super();
        this.position = position;
        enemies = new ArrayList<>();

        switch (formation) {
            case SQUARE:
                int N = (int) Math.sqrt(numEnemies);

                int radius = 20;
                int spacing = 10;

                for (int i = 0, x = radius; i < N; i++, x += 2 * radius + spacing) {
                    for (int j = 0, y = radius; j < N; j++, y += 2 * radius + spacing) {
                        Enemy enemy = new Enemy(new Vector2D(x, y), 3 * Math.PI / 2);
                        enemies.add(enemy);
                    }
                }

                break;

            default:
                break;
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
            enemy.draw(position);
        }
    }

    @Override
    public void renderStep(double dt) {

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.renderStep(dt);

            // remove enemy if no longer alive
            if (!enemy.isAlive()) {
                enemies.remove(enemy);
                i--;
            }
        }

        super.renderStep(dt);
    }

    @Override
    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;

        for (Enemy enemy : enemies) {
            for (Missile missile : missiles) {

                Vector2D enemyGlobalPosition = Vector2D.sum(position, enemy.position);

                Vector2D relativePositionVector = new Vector2D(enemyGlobalPosition.x - missile.position.x,
                        enemyGlobalPosition.y - missile.position.y);

                Double distanceBetween = relativePositionVector.magnitude();

                if (distanceBetween <= enemy.collisionRadius + missile.collisionRadius) {
                    points += missile.missileDamage; // Todo Better points system than just missile damage
                    enemy.takeDamage(missile.missileDamage);
                    missile.takeDamage(Integer.MAX_VALUE);
                    break;
                }
            }
        }

        return points;
    }
}