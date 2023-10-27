package LaboratoryExercise03;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

interface Item {
    int getPrice();

    String getType();

    boolean isPizza();
}

enum ExtraType {
    Ketchup(0), Coke(1);
    private int value;
    private int cost;

    ExtraType(int value) {
        this.value = value;
        if (value == 0) {
            this.cost = 3;
        } else {
            this.cost = 5;
        }
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return cost;
    }
}

enum PizzaType {
    Standard(0), Pepperoni(1), Vegetarian(2);
    private int value;
    private int cost;

    PizzaType(int value) {
        this.value = value;
        if (value == 0) {
            this.cost = 10;
        } else if (value == 1) {
            this.cost = 12;
        } else {
            this.cost = 8;
        }
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return cost;
    }
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        this("Nepoznat tip");
    }

    public InvalidExtraTypeException(String type) {
        super(type);
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
    }
}

class ExtraItem implements Item {
    private ExtraType type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        try {
            this.type = ExtraType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidExtraTypeException(type);
        }
    }

    public ExtraItem(ExtraType type) {
        this.type = type;
    }

    @Override
    public int getPrice() {
        return type.getCost();
    }

    @Override
    public String getType() {
        return type.name();
    }

    @Override
    public boolean isPizza() {
        return false;
    }
}

class PizzaItem implements Item {
    private PizzaType type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        try {
            this.type = PizzaType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        return type.getCost();
    }

    @Override
    public String getType() {
        return type.name();
    }

    @Override
    public boolean isPizza() {
        return true;
    }
}

class Order {
    private class OrderItem {
        private final Item item;
        private int count;

        public OrderItem(Item item, int count) {
            this.item = item;
            this.count = count;
        }

        public Item getItem() {
            return item;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return getItem().getPrice() * getCount();
        }
    }

    private ArrayList<OrderItem> items;
    private boolean locked;

    public Order() {
        this.items = new ArrayList<>();
        this.locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException , OrderLockedException{
        if (count > 10)
            throw new ItemOutOfStockException(item);
        if(locked)
            throw new OrderLockedException();

        Optional<OrderItem> orderItem = items.stream()
                .filter(each -> each.getItem().getType().equals(item.getType())).findFirst();
        if(orderItem.isPresent()){
            orderItem.ifPresent(i -> i.setCount(count));
            return;
        }
        items.add(new OrderItem(item,count));
    }
    public int getPrice(){
        return items.stream().mapToInt(OrderItem::getPrice).sum();
    }
    public void displayOrder(){
        IntStream.range(0,items.size())
                .forEach(i -> {
                    OrderItem oi = items.get(i);
                    System.out.printf("%3d.%-15sx%2d%5d$\n",i+1,oi.getItem().getType(),oi.getCount(),oi.getPrice());
                });
        System.out.printf("%-22s%5d$\n","Total:",getPrice());
    }
    public void removeItem(int idx) throws OrderLockedException, ArrayIndexOutOfBоundsException {
        if(locked){
            throw new OrderLockedException();
        }
        if(idx > items.size()){
            throw new ArrayIndexOutOfBоundsException(idx);
        }
        items.remove(idx);
    }
    public void lock() throws EmptyOrder {
        if(items.isEmpty()){
            throw new EmptyOrder();
        }
        locked=true;
    }
}
class ArrayIndexOutOfBоundsException extends Exception {
    public ArrayIndexOutOfBоundsException(int idx) {
        super("ArrayIndexOutOfBоundsException");
    }
}
class EmptyOrder extends Exception{
    public EmptyOrder() {
    }
}

class OrderLockedException extends Exception{
    private static final long serialVesion = 1L;

    public OrderLockedException() {
    }
}
class ItemOutOfStockException extends Exception {
    private Item item;

    public Item getItem() {
        return item;
    }

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }

    public ItemOutOfStockException(String message) {
        super(message);
    }

    public ItemOutOfStockException() {
        this(" Unknown item is out of stock ");
    }
}

public class PizzaOrderTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}
