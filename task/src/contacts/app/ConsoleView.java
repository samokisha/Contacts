package contacts.app;

import contacts.Contacts;
import contacts.entity.Contact;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleView {

    // todo: replace all string messages to constants

    private final Contacts contacts;
    private final Scanner scanner;
    private ConsoleViewState currentState = ConsoleViewState.MENU;

    public ConsoleView(Contacts contacts) {
        this.contacts = contacts;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        while (currentState != ConsoleViewState.EXIT) {
            switch (currentState) {
                case MENU:
                    showMenu();
                    break;
                case ADD:
                    addContact();
                    break;
                case REMOVE:
                    removeContact();
                    break;
                case EDIT:
                    editContact();
                    break;
                case COUNT:
                    countContacts();
                    break;
                case LIST:
                    listContacts();
                    break;
                default:
                    break;
            }
        }
    }

    private void listContacts() {
        printContactsList(contacts.getAll());

        currentState = ConsoleViewState.MENU;
    }

    private void countContacts() {
        int count = contacts.getCount();
        System.out.printf("The Phone Book has %d records.", count);

        currentState = ConsoleViewState.MENU;
    }

    private void editContact() {
        if (contacts.getCount() == 0) {
            System.out.println("No records to edit!");
            currentState = ConsoleViewState.MENU;
            return;
        }

        final List<Contact> allContacts = this.contacts.getAll();
        final int selectedContactIndex = selectContact(allContacts);

        if (selectedContactIndex < 0) {
            return;
        }

        Contact selectedContact = allContacts.get(selectedContactIndex);

        String editAction = readLine("Select a field (name, surname, number):")
                .trim()
                .toLowerCase();

        boolean status = false;
        switch (editAction) {
            case "name":
                String name = readLine("Enter name:");
                status = contacts.updateContact(selectedContactIndex,
                        new Contact(name, selectedContact.getSurname(), selectedContact.getNumber()));
                break;
            case "surname":
                String surname = readLine("Enter surname:");
                status = contacts.updateContact(selectedContactIndex,
                        new Contact(selectedContact.getName(), surname, selectedContact.getNumber()));
                break;
            case "number":
                String number = readLine("Enter number:");

                if (isValidNumber(number)) {
                    status = contacts.updateContact(selectedContactIndex,
                            new Contact(selectedContact.getName(), selectedContact.getSurname(), number));
                } else {
                    status = contacts.updateContact(selectedContactIndex,
                            new Contact(selectedContact.getName(), selectedContact.getSurname(), ""));
                    System.out.println("Entered number is not valid!");
                }
                break;
            default:
                System.out.println("Field not found!");
                return;
        }

        if (status) {
            System.out.println("The record updated!");
        }

        currentState = ConsoleViewState.MENU;
    }

    private void removeContact() {
        if (contacts.getCount() == 0) {
            System.out.println("No records to remove!");
            currentState = ConsoleViewState.MENU;
            return;
        }

        final List<Contact> allContacts = this.contacts.getAll();
        final int selectedContactIndex = selectContact(allContacts);

        if (selectedContactIndex < 0) {
            return;
        }

        if (contacts.remove(allContacts.get(selectedContactIndex))) {
            System.out.println("The record removed!");
        }

        currentState = ConsoleViewState.MENU;
    }

    private int selectContact(List<Contact> contacts) {
        printContactsList(contacts);

        int selected = -1;
        try {
            selected = Integer.parseInt(readLine("Select a record:").trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Wrong input! Err: " + e.getMessage());
        }

        return selected;
    }

    private void printContactsList(List<Contact> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            final Contact contact = contacts.get(i);
            System.out.printf("%d. %s %s, %s\n", i + 1, contact.getName(), contact.getSurname(), contact.getNumber());
        }
    }

    private void showMenu() {
        switch (readLine("Enter action (add, remove, edit, count, list, exit):").trim().toLowerCase()) {
            case "add":
                currentState = ConsoleViewState.ADD;
                break;
            case "remove":
                currentState = ConsoleViewState.REMOVE;
                break;
            case "edit":
                currentState = ConsoleViewState.EDIT;
                break;
            case "count":
                currentState = ConsoleViewState.COUNT;
                break;
            case "list":
                currentState = ConsoleViewState.LIST;
                break;
            case "exit":
                currentState = ConsoleViewState.EXIT;
                break;
            default:
                System.out.println("Command not found!");
                break;
        }
    }

    private void addContact() {
        String name = readLine("Enter the name:");
        String surname = readLine("Enter the surname:");
        String number = readLine("Enter the number:");

        if (!isValidNumber(number)) {
            System.out.println("Wrong number format!");
            number = "";
        }

        if (contacts.addContact(name, surname, number)) {
            System.out.println("The record added.");
        }

        currentState = ConsoleViewState.MENU;
    }

    private boolean isValidNumber(String number) {
        String groupSplitterPattern = "[\\s-]+";
        Pattern containParenthesesPattern = Pattern.compile(".*\\([\\da-z]+\\).*",
                Pattern.CASE_INSENSITIVE);
        Pattern firstGroupPattern = Pattern.compile("\\+((\\([\\da-z]+\\))|([\\da-z]+))",
                Pattern.CASE_INSENSITIVE);
        Pattern otherGroupsPattern = Pattern.compile("(\\([\\da-z]{2,}\\))|([\\da-z]{2,})",
                Pattern.CASE_INSENSITIVE);

        int parenthesesCount = 0;
        final String[] groups = number.split(groupSplitterPattern);
        for (int i = 0; i < groups.length; i++) {
            if (containParenthesesPattern.matcher(groups[i]).matches()) {
                parenthesesCount += 1;
                if (parenthesesCount > 1) {
                    return false;
                }
            }

            if (i == 0) {
                if (firstGroupPattern.matcher(groups[i]).matches()
                        || otherGroupsPattern.matcher(groups[i]).matches()) {
                    continue;
                }
                return false;
            }

            if (!otherGroupsPattern.matcher(groups[i]).matches()) {
                return false;
            }
        }

        return true;
    }

    private String readLine() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    private String readLine(String msg) {
        System.out.println(msg);
        System.out.print("> ");
        return scanner.nextLine();
    }
}
