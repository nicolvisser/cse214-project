/**
 * Critter
 */
public interface Critter {
    public boolean isAlive();

    public void renderStep(double dt);

    public void draw();
}