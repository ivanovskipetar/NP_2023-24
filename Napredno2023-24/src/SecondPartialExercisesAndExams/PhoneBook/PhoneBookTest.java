package SecondPartialExercisesAndExams.PhoneBook;

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }

    @Override
    public int compareTo(Contact o) {
        int res = this.name.compareTo(o.name);

        if (res == 0)
            res = this.number.compareTo(o.number);

        return res;
    }
}

@SuppressWarnings("unchecked")
class PhoneBook {
    //to prevent duplicates
    Set<String> allNumbers;
    //to group contacts by subnumber
    Map<String, Set<Contact>> contactsBySubstring;
    //to group contacts by name
    Map<String, Set<Contact>> contactsByName;

    public PhoneBook() {
        allNumbers = new HashSet<>();
        contactsBySubstring = new HashMap<>();
        contactsByName = new HashMap<>();
    }

    private List<String> getSubstring(String number) {
        List<String> result = new ArrayList<>();
        for (int len = 3; len <= number.length(); len++) {
            for (int i = 0; i <= number.length() - len; i++) {
                result.add(number.substring(i, i + len));
            }
        }
        return result;
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allNumbers.contains(number))
            throw new DuplicateNumberException(String.format("Duplicate number: %s", number));
        else {
            allNumbers.add(number);
            Contact contact = new Contact(name, number);
            List<String> subnumbers = getSubstring(number);
            for (String subnumber : subnumbers) {
                contactsBySubstring.putIfAbsent(subnumber, new TreeSet<>());
                contactsBySubstring.get(subnumber).add(contact);
            }

            contactsByName.putIfAbsent(name, new TreeSet<>());
            contactsByName.get(name).add(contact);
        }
    }

    public void contactsByNumber(String number) {
        Set<Contact> contacts = contactsBySubstring.get(number);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }

    public void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }
}
