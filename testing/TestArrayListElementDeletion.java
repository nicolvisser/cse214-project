import java.util.ArrayList;

/**
 * TestArrayListElementDeletion
 */
public class TestArrayListElementDeletion {

    public static void main(String[] args) {
        ArrayList<Integer> myList = new ArrayList<>();

        myList.add(1);
        myList.add(2);
        myList.add(3);
        myList.add(4);
        myList.add(5);

        for (int i : myList) {
            System.out.println(i);
        }

        for (int i : myList) {
            System.out.println(i);
            if (i == 2) {
                myList.remove(i);
            }

        }

    }
}