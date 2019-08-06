package contacts.app;

import contacts.Contacts;

public class Main {
    public static void main(String[] args) {
        Contacts contacts = new Contacts(new MemoryStorage());
        ConsoleView view = new ConsoleView(contacts);
        view.start();
    }
}
