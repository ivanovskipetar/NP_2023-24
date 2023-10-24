package LaboratoryExercise02;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

abstract class Contact {

    protected final int day;
    protected final int month;
    protected final int year;

    public Contact(String date) {
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(5,7));
        day = Integer.parseInt(date.substring(8,10));
    }

    public Contact(Contact c) {
        day = c.day;
        month = c.month;
        year = c.year;
    }

    public boolean isNewerThan(Contact c) {
        if (year > c.year)
            return true;
        if (year == c.year && month > c.month)
            return true;
        if (year == c.year && month == c.month && day > c.day)
            return true;
        return false;
    }

    public abstract String getType();
    public abstract String getContact();
}

class EmailContact extends Contact {

    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public EmailContact(EmailContact contact) {
        super(contact);
        email=contact.email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String getContact() {
        return getEmail();
    }

    @Override
    public String toString() {
        return email;
    }
}

class PhoneContact extends Contact {

    private String phone;

    enum Operator {
        VIP,
        ONE,
        TMOBILE
    }

    Operator operatorType;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        switch (phone.charAt(2)) {
            case '0':
            case '1':
            case '2':
                operatorType = Operator.TMOBILE;
                break;
            case '5':
            case '6':
                operatorType = Operator.ONE;
                break;
            case '7':
            case '8':
                operatorType = Operator.VIP;
                break;
        }
    }

    public PhoneContact(PhoneContact pc) {
        super(pc);
        phone = pc.phone;
        operatorType = pc.operatorType;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operatorType;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String getContact() {
        return getPhone();
    }

    @Override
    public String toString() {
        return phone;
    }
}
class Student{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private Contact [] contacts;
    private int numEmailContacts;
    private int numPhoneContacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[0];
        this.numEmailContacts = 0;
        this.numPhoneContacts = 0;
    }
    public Student(Student s) {
        firstName = s.firstName;
        lastName = s.lastName;
        city = s.city;
        age = s.age;
        index = s.index;
        numEmailContacts = s.numEmailContacts;
        numPhoneContacts = s.numPhoneContacts;
        contacts = Arrays.copyOf(s.contacts, s.contacts.length);
    }

    public void addEmailContact(String date, String email){
        Contact [] tmp = new Contact[contacts.length+1];
        int i;
        for (i=0; i<contacts.length; ++i)
            tmp[i] = contacts[i];
        tmp[i]= new EmailContact(date,email);
        contacts = tmp;
        ++numEmailContacts;
    }
    public void addPhoneContact(String date, String phone){
        Contact [] tmp = new Contact[contacts.length+1];
        int i;
        for (i=0; i<contacts.length; ++i)
            tmp[i] = contacts[i];
        tmp[i]= new PhoneContact(date,phone);
        contacts = tmp;
        ++numPhoneContacts;
    }
    public Contact[] getEmailContacts(){
        Contact [] tmp = new Contact[numEmailContacts];
        int j=0;
        for(int i=0;i<contacts.length;i++){
            if(contacts[i].getType().equals("Email")){
                tmp[j]=new EmailContact((EmailContact)contacts[i]);
                j++;
            }
        }
        return tmp;
    }
    public Contact[] getPhoneContacts(){
        Contact [] tmp = new Contact[numPhoneContacts];
        int j=0;
        for(int i=0;i<contacts.length;i++){
            if(contacts[i].getType().equals("Phone")){
                tmp[j]=new PhoneContact((PhoneContact)contacts[i]);
                j++;
            }
        }
        return tmp;
    }

    public String getCity() {
        return city;
    }

    public String getFullName(){
        return String.format("%s %s",firstName,lastName);
    }

    public long getIndex() {
        return index;
    }
    public Contact getLatestContact() {
        Contact latest = contacts[0];
        for(int i=1;i<contacts.length;i++){
            if(contacts[i].isNewerThan(latest)){
                latest = contacts[i];
            }
        }
        if(latest.getType().equals("Email"))
            latest = new EmailContact((EmailContact) latest);
        else
            latest = new PhoneContact((PhoneContact) latest);

        return latest;
    }

    private static String withSign(String a) {
        return "\"" + a + "\"";
    }
    public double numberOfContacts() {
        return numEmailContacts + numPhoneContacts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(withSign("ime")).append(":").append(withSign(firstName)).append(", ").append(withSign("prezime"));
        sb.append(":").append(withSign(lastName)).append(", ").append(withSign("vozrast")).append(":").append(age);
        sb.append(", ").append(withSign("grad")).append(":").append(withSign(city)).append(", ").append(withSign("indeks"));
        sb.append(":").append(index).append(", ").append(withSign("telefonskiKontakti")).append(":[");
        int counter =0;
        for (int i=0; i < contacts.length; ++i) {
            if (contacts[i].getType().equals("Phone")) {
                sb.append(withSign(contacts[i].getContact()));
                ++counter;
                if (counter < numPhoneContacts)
                    sb.append(", ");
            }
            if (counter == numPhoneContacts){
                sb.append("]");
                break;
            }
        }
        sb.append(", ");
        sb.append(withSign("emailKontakti")).append(":[");
        counter =0;
        for (int i=0; i < contacts.length; ++i) {
            if (contacts[i].getType().equals("Email")) {
                sb.append(withSign(contacts[i].getContact()));
                ++counter;
                if (counter < numEmailContacts)
                    sb.append(", ");
            }
            if (counter == numEmailContacts) {
                sb.append("]}");
                break;
            }

        }
        return sb.toString();
    }
}

class Faculty{
    private String name;
    private Student [] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students,students.length);
    }

    public int countStudentsFromCity(String cityName){
        long cnt=Arrays.stream(students)
                .filter(s -> s.getCity().equals(cityName))
                .count();
        return (int)cnt;
    }
    public Student getStudent(long index){
        for (int i=0; i<students.length; ++i) {
            if (students[i].getIndex()==index)
                //return new Student(students[i]);
                return students[i];
        }
        return null;
    }
    public double getAverageNumberOfContacts() {
        double sum =0;
        for (int i=0; i<students.length; ++i) {
            sum += students[i].numberOfContacts();
        }
        return sum / students.length;
    }

    public Student getStudentWithMostContacts() {
        int index = 0;
        for (int i=1; i<students.length; ++i) {
            if (students[i].numberOfContacts() > students[index].numberOfContacts())
                index = i;
            else if (students[i].numberOfContacts() == students[index].numberOfContacts()) {
                if (students[i].getIndex() > students[index].getIndex())
                    index = i;
            }
        }
        return new Student(students[index]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"fakultet\":\"").append(name).append("\", \"studenti\":[");
        for (int i=0; i<students.length; ++i){
            sb.append(students[i].toString());
            if (i+1 != students.length)
                sb.append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
