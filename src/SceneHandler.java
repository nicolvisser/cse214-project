import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

// for reason behind '? extends' in constructor arguments see:
// https://stackoverflow.com/questions/44221882/passing-an-arraylist-of-subclass-to-a-constructor-that-takes-arraylist-of-superc/44221916

public class SceneHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<ArrayList<? extends SceneItem>> listOfListOfItems;
    //// private long frameNumber = 0;

    public SceneHandler() {
        listOfListOfItems = new ArrayList<>();
    }

    public void add(SceneItem item) {
        ArrayList<SceneItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        listOfListOfItems.add(listOfItems);
    }

    public void add(ArrayList<? extends SceneItem> listOfItems) {
        listOfListOfItems.add(listOfItems);
    }

    public void render(double dt) {

        //// frameNumber++;

        for (ArrayList<? extends SceneItem> listOfItems : listOfListOfItems) {

            Iterator<? extends SceneItem> sceneItemIterator = listOfItems.iterator();

            while (sceneItemIterator.hasNext()) {
                SceneItem item = sceneItemIterator.next();

                //// System.out.println("Frame " + frameNumber + ": Rendering " +
                //// item.getClass());
                item.render(dt);

                if (item.mayBeRemovedFromScene()) {
                    sceneItemIterator.remove();
                }
            }
        }
    }

    public void draw() {
        for (ArrayList<? extends SceneItem> listOfItems : listOfListOfItems) {
            for (SceneItem item : listOfItems) {
                item.draw();
            }
        }
    }

}