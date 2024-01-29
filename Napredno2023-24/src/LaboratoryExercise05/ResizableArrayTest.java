package LaboratoryExercise05;

import java.util.*;

class ResizableArray<T> {
    private T[] elements;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        this.elements = (T[]) new Object[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        T[] array = (T[]) Arrays.copyOf(src.toArray(), src.count());
        Arrays.stream(array).forEach(elem -> dest.addElement(elem));
    }

    public void addElement(T element) {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[newArray.length - 1] = element;
        elements = newArray;
    }

    public int count() {
        return this.elements.length;
    }

    public boolean contains(T element) {
        return Arrays.stream(this.elements)
                .anyMatch(e -> e.equals(element));
    }

    @SuppressWarnings("unchecked")
    public boolean removeElement(T element) {
        if (contains(element)) {
            int i;
            for (i = 0; i < elements.length; i++) {
                if (element.equals(elements[i])) {
                    break;
                }
            }
            elements[i] = elements[count() - 1];
            T[] newElements = (T[]) new Object[elements.length - 1];
            newElements = Arrays.copyOf(elements, elements.length - 1);
            elements = newElements;
            return true;
        } else return false;
    }

    public boolean isEmpty() {
        return this.elements.length == 0;
    }

    public T elementAt(int idx) {
        if (idx < 0 || idx >= elements.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.elements[idx];
    }

    public Object[] toArray() {
        return Arrays.stream(elements).toArray();
    }

}

class IntegerArray extends ResizableArray<Integer> {
    public double sum() {
        double sum = 0;
        for (int i = 0; i < count(); i++) {
            sum += elementAt(i);
        }
        return sum;
    }

    public double mean() {
        return sum() / count() * 1.0;
    }

    public int countNonZero() {
        int cnt = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0)
                cnt++;
        }
        return cnt;
    }

    public IntegerArray distinct() {
        IntegerArray arr = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            if (!arr.contains(elementAt(i)))
                arr.addElement(elementAt(i));
        }
        return arr;
    }

    public IntegerArray increment(int offset) {
        IntegerArray arr = new IntegerArray();
        for (int i = 0; i < count(); i++)
            arr.addElement(elementAt(i) + offset);

        return arr;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
