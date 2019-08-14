package contacts;

import contacts.Contacts;
import contacts.app.ConsoleView;
import contacts.app.MemoryStorage;

public class Main {
    public static void main(String[] args) {
        Contacts contacts = new Contacts(new MemoryStorage());
        ConsoleView view = new ConsoleView(contacts);
        view.start();
    }
}
