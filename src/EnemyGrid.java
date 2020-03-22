import java.io.Serializable;
import java.util.ArrayList;

/**
 * EnemyGroup
 */
public class EnemyGrid implements Serializable {

    private static final long serialVersionUID = 1L;

    int M, N;
    Vector2D center;
    Enemy[][] enemies;

    public EnemyGrid(double centerX, double centerY, int numHorizontal, int numVertical) {
        center = new Vector2D(centerX, centerY);
        M = numHorizontal;
        N = numVertical;
        enemies = new Enemy[M][N];

        double radius = 20;
        double spacing = 10;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double x = centerX - (2 * i - (M - 1)) * (radius + spacing / 2);
                double y = centerY - (2 * j - (N - 1)) * (radius + spacing / 2);
                enemies[i][j] = new Enemy(new Vector2D(x, y), 3 * Math.PI / 2);
            }
        }

    }

    public void draw() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                Enemy enemy = enemies[i][j];
                if (enemy.isAlive()) {
                    enemy.draw();
                }
            }
        }
    }

    public void renderStep(double dt) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                Enemy enemy = enemies[i][j];
                if (enemy.isAlive()) {
                    enemy.renderStep(dt);
                }
            }
        }
    }

    public void handleCollisionsWithMissiles(ArrayList<Missile> missiles) {

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                Enemy enemy = enemies[i][j];
                if (enemy.isAlive()) {

                    for (Missile missile : missiles) {

                        if (missile.hasCollidedWith(enemy)) {
                            int missileDamage = 100; // TODO: hardcoding
                            // ! TODO: Score needs to be updated in invadergamestate:
                            // score += missileDamage;
                            enemy.takeDamage(missileDamage);
                            missile.takeDamage(Integer.MAX_VALUE);
                            break;
                        }

                    }
                }
            }
        }

    }

}