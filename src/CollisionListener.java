import java.util.ArrayList;

public class CollisionListener {

    private class CollisionPair {

        ArrayList<? extends Collidable> objects1;
        ArrayList<? extends Collidable> objects2;

        // see
        // https://stackoverflow.com/questions/44221882/passing-an-arraylist-of-subclass-to-a-constructor-that-takes-arraylist-of-superc/44221916
        public CollisionPair(ArrayList<? extends Collidable> objects1, ArrayList<? extends Collidable> objects2) {
            this.objects1 = objects1;
            this.objects2 = objects2;

        }
    }

    public ArrayList<CollisionPair> collisionPairs;

    CollisionListener() {
        collisionPairs = new ArrayList<>();
    }

    // see
    // https://stackoverflow.com/questions/44221882/passing-an-arraylist-of-subclass-to-a-constructor-that-takes-arraylist-of-superc/44221916
    public void add(ArrayList<? extends Collidable> objects1, ArrayList<? extends Collidable> objects2) {
        collisionPairs.add(new CollisionPair(objects1, objects2));
    }

    public void handleCollisions() {
        for (CollisionPair collisionPair : collisionPairs) {
            for (Collidable obj1 : collisionPair.objects1) {
                for (Collidable obj2 : collisionPair.objects2) {
                    obj1.handlePossibleCollisionWith(obj2);
                }
            }
        }
    }

}