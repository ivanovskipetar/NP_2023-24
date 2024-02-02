package SecondPartialExercisesAndExams.DeliveryApp;

import java.util.*;

interface Location {
    int getX();
    int getY();
    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}
class LocationCreator {
    public static Location create(int x, int y) {
        return new Location() {
            @Override
            public int getX() {
                return x;
            }
            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class Address {
    private final String name;
    private final Location location;
    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }
}

class User {
    private final String id;
    private final String name;
    private final Map<String, Address> addresses;
    private final List<Float> moneySpent;
    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.addresses = new HashMap<>();
        this.moneySpent = new ArrayList<>();
    }
    public void addAddress(String name, Location location) {
        addresses.put(name, new Address(name, location));
    }
    public double totalMoneySpent() {
        return moneySpent.stream().mapToDouble(i -> i).sum();
    }
    public double averageMoneySpent() {
        if (moneySpent.isEmpty()) return 0;
        return totalMoneySpent() / moneySpent.size();
    }
    public void processOrder(float cost) {
        moneySpent.add(cost);
    }
    public String getId() {
        return id;
    }
    public Map<String, Address> getAddresses() {
        return addresses;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, moneySpent.size(), totalMoneySpent(), averageMoneySpent());
    }
}

class DeliveryPerson {
    private final String id;
    private final String name;
    private Location currentLocation;
    private final List<Float> moneyEarned;
    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.moneyEarned = new ArrayList<>();
    }
    public double totalMoneyEarned() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }
    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) return 0;
        return totalMoneyEarned() / moneyEarned.size();
    }
    public int compareDistanceToRestaurant(DeliveryPerson other, Location restaurantLocation) {
        int myCurrentDistanceToRestaurant = currentLocation.distance(restaurantLocation);
        int otherCurrentDistanceToRestaurant = other.currentLocation.distance(restaurantLocation);
        if (myCurrentDistanceToRestaurant == otherCurrentDistanceToRestaurant)
            return Integer.compare(moneyEarned.size(), other.moneyEarned.size());
        else
            return myCurrentDistanceToRestaurant - otherCurrentDistanceToRestaurant;
    }
    public void processOrder(int distance, Location location) {
        currentLocation = location;
        moneyEarned.add((float) (90 + 10 * (distance / 10)));
    }
    public String getId() {
        return id;
    }
    public Location getCurrentLocation() {
        return currentLocation;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, moneyEarned.size(), totalMoneyEarned(), averageMoneyEarned());
    }
}

class Restaurant {
    private final String id;
    private final String name;
    private final Location location;
    private final List<Float> moneyEarned;
    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.moneyEarned = new ArrayList<>();
    }
    public double totalMoneyEarned() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }
    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) return 0;
        return totalMoneyEarned() / moneyEarned.size();
    }
    public void processOrder(float cost) {
        moneyEarned.add(cost);
    }
    public String getId() {
        return id;
    }
    public Location getLocation() {
        return location;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, moneyEarned.size(), totalMoneyEarned(), averageMoneyEarned());
    }
}

class DeliveryApp {
    private final String appName;
    private final Map<String, User> users;
    private final Map<String, DeliveryPerson> deliveryPeople;
    private final Map<String, Restaurant> restaurants;
    public DeliveryApp(String name) {
        this.appName = name;
        this.users = new HashMap<>();
        this.deliveryPeople = new HashMap<>();
        this.restaurants = new HashMap<>();
    }
    public void addAddress(String id, String name, Location location) {
        users.get(id).addAddress(name, location);
    }
    public void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }
    public void addRestaurant(String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }
    public void registerDeliveryPerson(String id, String name, Location location) {
        deliveryPeople.put(id, new DeliveryPerson(id, name, location));
    }
    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User client = users.get(userId);
        Address userAddress = client.getAddresses().get(userAddressName);
        Restaurant restaurant = restaurants.get(restaurantId);

        DeliveryPerson closestDeliveryPersonToRestaurant = deliveryPeople.values().stream()
                .min((l, r) -> l.compareDistanceToRestaurant(r, restaurant.getLocation())).get();

        int distance = closestDeliveryPersonToRestaurant.getCurrentLocation().distance(restaurant.getLocation());

        closestDeliveryPersonToRestaurant.processOrder(distance, userAddress.getLocation());
        client.processOrder(cost);
        restaurant.processOrder(cost);
    }
    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::totalMoneySpent).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }
    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::averageMoneyEarned).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }
    public void printDeliveryPeople() {
        deliveryPeople.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::totalMoneyEarned).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(System.out::println);
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
