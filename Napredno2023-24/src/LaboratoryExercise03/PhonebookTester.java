package LaboratoryExercise03;

import java.io.*;
import java.util.*;

class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }
}

class InvalidNameException extends Exception {

    public String name;

    public InvalidNameException() {
        super();
    }

    public InvalidNameException(String name) {
        super();
        this.name = name;
    }
}

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}

class Contact {
    private String name;
    private List<String> phoneNumbers;

    public Contact(String name, String... phonenumber) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        checkName(name);
        this.name = name;
        checkNumber(phonenumber);
        this.phoneNumbers = new ArrayList<>();
        phoneNumbers.addAll(Arrays.asList(phonenumber));
    }

    private void checkNumber(String[] phonenumber) throws MaximumSizeExceddedException, InvalidNumberException {
        if (phonenumber.length > 5)
            throw new MaximumSizeExceddedException();
        for (String s : phonenumber) {
            if (s.length() != 9)
                throw new InvalidNumberException();
            checkFirstThreeDigits(s);
        }
    }

    private void checkFirstThreeDigits(String s) throws InvalidNumberException {
        String firstThree = s.substring(0, 3);
        ArrayList<String> acceptableDigits = new ArrayList<String>(Arrays.asList("070", "071", "072", "075", "076", "077", "078"));
        if (!acceptableDigits.contains(firstThree))
            throw new InvalidNumberException();

        if (!s.matches("\\d+"))//check to see if the string 's' contains only numbers
            throw new InvalidNumberException();
    }

    private void checkName(String name) throws InvalidNameException {
        if (name.length() < 5 || name.length() > 10)
            throw new InvalidNameException(name);
        char[] array = name.toCharArray();

        for (char c : array) {
            if (!Character.isLetterOrDigit(c)) {
                throw new InvalidNameException(name);
            }
        }
    }

    public void addNumber(String phonenumber) throws InvalidNumberException {
        if (phoneNumbers.size() == 5)
            return;
        checkFirstThreeDigits(phonenumber);
        phoneNumbers.add(phonenumber);
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        List<String> copy = new ArrayList<>(phoneNumbers);
        Collections.sort(copy);
        return copy.stream().toArray(String[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append("\n").append(phoneNumbers.size()).append("\n");
        for (String s : getNumbers()) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}

class PhoneBook {
    private List<Contact> contacts;

    public PhoneBook() {
        this.contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.size() > 250)
            throw new MaximumSizeExceddedException();

        Optional<Contact> exist = contacts.stream().filter(c -> c.getName().equals(contact.getName())).findFirst();
        if (exist.isPresent())
            throw new InvalidNameException(contact.getName());
        contacts.add(contact);
    }

    public Contact getContactForName(String name) {
        return contacts.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContacts() {
        return contacts.stream().sorted(Comparator.comparing(Contact::getName)).toArray(Contact[]::new);
    }

    public boolean removeContact(String name) {
        Contact needed = getContactForName(name);
        if (needed == null)
            return false;
        contacts.remove(needed);
        return true;
    }

    public static boolean saveAsTextFile(PhoneBook phonebook, String path) throws IOException {
        File f = new File(path);
        if (phonebook == null)
            return false;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(phonebook);
        }
        return false;
    }

    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidFormatException {
        File f = new File(path);
        if (!f.exists())
            throw new IOException();
        if (!f.canRead())
            throw new InvalidFormatException();
        PhoneBook pb = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            pb = (PhoneBook) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pb;
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        List<Contact> list = new ArrayList<>();
        for (Contact c : contacts) {
            for (String s : c.getNumbers()) {
                if (s.startsWith(number_prefix) && !list.contains(c)) {
                    list.add(c);
                    continue;
                }
            }
        }
        return list.stream().toArray(Contact[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getContacts().length; i++) {
            if (getContacts()[i] != null) {
                sb.append(getContacts()[i]).append("\n");
            }
        }
        return sb.toString();
    }
}

public class PhonebookTester {
    public static void main(String[] args) throws Exception {

        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }


    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}

