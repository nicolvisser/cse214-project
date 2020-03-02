/**
 * TestObject2D
 */
public class TestObject2D {

    public static void main(String[] args) {

        Object2D obj = new Object2D(new Vector2D(3, 4));

        obj.lookAt(0, 0);

        Vector2D forwardVector = obj.FWDVector();

        System.out.println("(" + forwardVector.x + "," + forwardVector.y + ")");

        obj.translateX(1);
        obj.lookAt(0, 0);

        forwardVector = obj.FWDVector();

        System.out.println("(" + forwardVector.x + "," + forwardVector.y + ")");

    }
}