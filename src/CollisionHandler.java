import java.util.ArrayList;

// for reason behind '? extends' in constructor arguments see:
// https://stackoverflow.com/questions/44221882/passing-an-arraylist-of-subclass-to-a-constructor-that-takes-arraylist-of-superc/44221916
public class CollisionHandler {

    private class CollisionPair {

        ArrayList<? extends Collidable> objects1;
        ArrayList<? extends Collidable> objects2;

        public CollisionPair(ArrayList<? extends Collidable> objects1, ArrayList<? extends Collidable> objects2) {
            this.objects1 = objects1;
            this.objects2 = objects2;

        }
    }

    public ArrayList<CollisionPair> collisionPairs;

    CollisionHandler() {
        collisionPairs = new ArrayList<>();
    }

    public void add(Collidable object1, Collidable object2) {
        ArrayList<Collidable> objects1 = new ArrayList<>();
        objects1.add(object1);

        ArrayList<Collidable> objects2 = new ArrayList<>();
        objects2.add(object2);

        collisionPairs.add(new CollisionPair(objects1, objects2));
    }

    public void add(Collidable object1, ArrayList<? extends Collidable> objects2) {
        ArrayList<Collidable> objects1 = new ArrayList<>();
        objects1.add(object1);

        collisionPairs.add(new CollisionPair(objects1, objects2));
    }

    public void add(ArrayList<? extends Collidable> objects1, ArrayList<? extends Collidable> objects2) {
        collisionPairs.add(new CollisionPair(objects1, objects2));
    }

    public void handleCollisions() {
        //// long numberOfCollisionTests = 0;
        for (CollisionPair collisionPair : collisionPairs) {
            for (Collidable obj1 : collisionPair.objects1) {
                for (Collidable obj2 : collisionPair.objects2) {
                    //// numberOfCollisionTests++;
                    obj1.handlePossibleCollisionWith(obj2);
                }
            }
        }
        //// System.out.println(numberOfCollisionTests);
    }

}