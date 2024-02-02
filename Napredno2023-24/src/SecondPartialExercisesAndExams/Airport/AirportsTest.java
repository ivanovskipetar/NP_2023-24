package SecondPartialExercisesAndExams.Airport;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum FlightType {
    ARRIVING,
    DEPARTING
}

class Flight implements Comparable<Flight> {
    private String from;
    private String to;
    private LocalTime time;
    private int duration;

    private FlightType type;

    public Flight(String from, String to, int time, int duration, FlightType type) {
        this.from = from;
        this.to = to;
        this.time = LocalTime.of(0, 0, 0);
        this.time = this.time.plusMinutes(time);
        this.duration = duration;
        this.type = type;
    }

    public String flightTo() {
        return to;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public FlightType getType() {
        return type;
    }

    private String displayTimeDurationFormat(LocalTime time, int duration) {
        StringBuilder sb = new StringBuilder();
        int total = time.getHour() * 60 + time.getMinute() + duration;
        if (total >= 1440) { // +1d 4h16m
            sb.append("+").append(total / 1440).append("d ");
        }
        int hours = duration / 60;
        sb.append(String.format("%dh%02dm", hours, duration - (hours * 60))); // 4h16m
        return sb.toString();
    }

    @Override
    public String toString() {
        //HND-AMS 14:44-19:16 4h32m
        //HND-PVG 21:13-01:29 +1d 4h16m
        return String.format("%s-%s %s-%s %s", from, to, time, time.plusMinutes(duration), displayTimeDurationFormat(time, duration));
    }

    @Override
    public int compareTo(Flight o) {
        Comparator<Flight> comparator = Comparator.comparing(Flight::flightTo)
                .thenComparing(Flight::getTime)
                .thenComparing(Flight::getDuration);

        return comparator.compare(this, o);
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;

    private Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new TreeSet<>();
    }

    public void addFlight(String from, String to, int time, int duration, FlightType type) {
        flights.add(new Flight(from, to, time, duration, type));
    }

    @Override
    public String toString() {
        /*  Tokyo International (HND)
            Japan
            66795178
        */
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }

    public Set<Flight> getFlights() {
        return flights;
    }
}

class Airports {
    private Map<String, Airport> codeToAirport;

    public Airports() {
        this.codeToAirport = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        codeToAirport.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        codeToAirport.get(from).addFlight(from, to, time, duration, FlightType.DEPARTING);
        codeToAirport.get(to).addFlight(from, to, time, duration, FlightType.ARRIVING);
    }

    public void showFlightsFromAirport(String code) {
        Airport fromAirport = codeToAirport.get(code);

        System.out.println(fromAirport);
        List<Flight> flights = fromAirport.getFlights().stream()
                .filter(flight -> flight.getType() == FlightType.DEPARTING)
                .collect(Collectors.toList());

        IntStream.range(0, flights.size())
                .forEach(i -> System.out.println((i + 1) + ". " + flights.get(i)));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport fromAirport = codeToAirport.get(from);

        List<Flight> flightsTo = fromAirport.getFlights().stream()
                .filter(flight -> flight.flightTo().equals(to))
                .collect(Collectors.toList());

        if (flightsTo.isEmpty()) {
            System.out.println("No flights from " + from + " to " + to);
            return;
        }
        flightsTo.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        codeToAirport.get(to).getFlights().stream()
                .filter(f -> f.getType().equals(FlightType.ARRIVING))
                .forEach(System.out::println);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

