package FirstPartialExercises.ShoppingTest;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Product implements Comparable<Product> {
    String productID;
    String productName;
    double productPrice;

    public Product(String productID, String productName, double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.calculateTotalPrice(), other.calculateTotalPrice());
    }

    public abstract double calculateTotalPrice();

    public double productPriceWithDiscount() {
        return calculateTotalPrice() * 0.10;
    }
}

class ProductFactory {
    public static Product createProduct(String line) throws InvalidOperationException {
        //WS;101569;Coca Cola;970;64
        //PS;107965;Flour;409;800.78
        String[] parts = line.split(";");
        String productID = parts[1];
        String productName = parts[2];
        double productPrice = Double.parseDouble(parts[3]);
        if (parts[0].equals("WS")) {
            int quantity = Integer.parseInt(parts[4]);
            if (quantity == 0)
                throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.", productID));
            return new WSProduct(productID, productName, productPrice, quantity);
        } else { // PS
            double quantity = Double.parseDouble(parts[4]);
            if (quantity == 0.0)
                throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.", productID));
            return new PSProduct(productID, productName, productPrice, quantity);
        }
    }
}

class WSProduct extends Product {
    int quantity;

    public WSProduct(String productID, String productName, double productPrice, int quantity) {
        super(productID, productName, productPrice);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", productID, calculateTotalPrice());
    }

    @Override
    public double calculateTotalPrice() {
        return quantity * productPrice;
    }
}

class PSProduct extends Product {
    double quantity;

    public PSProduct(String productID, String productName, double productPrice, double quantity) {
        super(productID, productName, productPrice);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", productID, calculateTotalPrice());
    }

    @Override
    public double calculateTotalPrice() {
        return quantity * (productPrice / 1000);
    }
}

class ShoppingCart {

    List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addItem(String s) throws InvalidOperationException {
        this.products.add(ProductFactory.createProduct(s));
    }

    public void printShoppingCart(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        products.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(p -> pw.println(p));

        pw.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, PrintStream out) throws InvalidOperationException {
        if (discountItems.isEmpty())
            throw new InvalidOperationException("There are no products with discount.");

        PrintWriter pw = new PrintWriter(out);

        products.stream()
                .filter(p -> discountItems.contains(Integer.parseInt(p.productID)))
                .forEach(p -> pw.println(String.format("%s - %.2f",p.productID,p.productPriceWithDiscount())));

        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}