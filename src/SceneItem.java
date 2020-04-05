public interface SceneItem {

    public boolean mayBeRemovedFromScene();

    public void render(double dt);

    public void draw();
}