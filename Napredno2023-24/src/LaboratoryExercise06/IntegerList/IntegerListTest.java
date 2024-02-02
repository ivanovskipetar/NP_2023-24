package LaboratoryExercise06.IntegerList;
import java.util.*;
class IntegerList {
    private final List<Integer> listOfIntegers;

    public IntegerList() {
        this.listOfIntegers = new ArrayList<>();
    }

    public IntegerList(Integer... listOfIntegers) {
        this.listOfIntegers = new ArrayList<>(Arrays.asList(listOfIntegers));
    }

    public void add(int el, int idx) {
        if (idx > listOfIntegers.size()) {
            int len = idx - listOfIntegers.size();
            for (int i = 0; i < len; i++) {
                listOfIntegers.add(0);
            }
            listOfIntegers.add(el);
            return;
        }
        listOfIntegers.add(idx, el);
    }

    public int remove(int idx) {
        if (idx < 0 || idx > listOfIntegers.size()) throw new ArrayIndexOutOfBoundsException();
        return listOfIntegers.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx < 0 || idx > listOfIntegers.size()) throw new ArrayIndexOutOfBoundsException();
        listOfIntegers.set(idx, el);
    }

    public int get(int idx) {
        if (idx < 0 || idx > listOfIntegers.size()) throw new ArrayIndexOutOfBoundsException();
        return listOfIntegers.get(idx);
    }

    public int size() {
        return listOfIntegers.size();
    }

    public int count(int el) {
        return (int) listOfIntegers.stream().filter(i -> i.equals(el)).count();
    }

    public void removeDuplicates() {
        for (int i = listOfIntegers.size() - 1; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Objects.equals(listOfIntegers.get(i), listOfIntegers.get(j))) {
                    listOfIntegers.remove(j);
                    j++;
                    break;
                }
            }
        }
    }

    public int sumFirst(int k) {
        return listOfIntegers.stream().mapToInt(Integer::intValue).limit(k).sum();
    }

    public int sumLast(int k) {
        return listOfIntegers.stream().mapToInt(Integer::intValue).skip(listOfIntegers.size() - k).sum();
    }

    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx > listOfIntegers.size()) throw new ArrayIndexOutOfBoundsException();

        int targetIdx = (idx + k) % listOfIntegers.size();
        Integer el = listOfIntegers.remove(idx);
        listOfIntegers.add(targetIdx, el);
    }

    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx > listOfIntegers.size()) throw new ArrayIndexOutOfBoundsException();

        int targetIdx = (idx - k) % listOfIntegers.size();
        if (targetIdx < 0) targetIdx = listOfIntegers.size() + targetIdx;
        Integer el = listOfIntegers.remove(idx);
        listOfIntegers.add(targetIdx, el);
    }

    public IntegerList addValue(int value) {
        IntegerList newList = new IntegerList();
        listOfIntegers.forEach(el -> newList.listOfIntegers.add(el + value));
        return newList;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}