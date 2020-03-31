/**
 * TestObject2D
 */
public class TestObject2D {

    public static void main(String[] args) {

        Object2D obj = new Object2D(new Vector2D(3, 4));

        obj.lookAt(0, 0);

        System.out.println(obj.lookVector());
        System.out.println(obj.getOrientationInDegrees());

        obj.translateX(1);
        obj.lookAt(0, 0);

        System.out.println(obj.lookVector());
        System.out.println(obj.getOrientationInDegrees());

    }
}