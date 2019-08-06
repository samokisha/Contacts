package contacts.app;

import contacts.Contacts;

import java.util.Scanner;

public class ConsoleView {

    private final Contacts contacts;
    private final Scanner scanner;

    public ConsoleView(Contacts contacts) {
        this.contacts = contacts;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        println("Enter the name of the person:");
        String name = readLine();
        println("Enter the surname of the person:");
        String surname = readLine();
        println("Enter the number:");
        String number = readLine();

        final boolean contactAdded = contacts.addContact(name, surname, number);

        if (contactAdded) {
            println("A record created!");
            println("A Phone Book with a single record created!");
        }
    }

    private void print(String msg) {
        System.out.print(msg);
    }

    private void println(String msg) {
        System.out.println(msg);
    }

    private String readLine() {
        System.out.print("> ");
        return scanner.nextLine();
    }
}
