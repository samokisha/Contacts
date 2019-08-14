package contacts.app;

import contacts.Contacts;
import contacts.entity.Contact;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleView {

    private static final String COUNT_CONTACT_MSG_PATTERN = "The Phone Book has %d records.";
    private static final String EDIT_NO_RECORDS_MSG = "No records to edit!";
    private static final String EDIT_SELECT_CONTACT_MSG = "Select a field (name, surname, number):";
    private static final String EDIT_ENTER_NAME_MSG = "Enter name:";
    private static final String EDIT_ENTER_SURNAME_MSG = "Enter surname:";
    private static final String EDIT_ENTER_NUMBER_MSG = "Enter number:";
    private static final String EDIT_NOT_VALID_NUMBER_MSG = "Entered number is not valid!";
    private static final String EDIT_UPDATE_MSG = "The record updated!";
    private static final String REMOVE_NO_RECORDS_MSG = "No records to remove!";
    private static final String REMOVE_RECORD_REMOVED_MSG = "The record removed!";
    private static final String SELECT_RECORD_MSG = "Select a record:";
    private static final String SHOW_MENU_SELECT_MSG = "Enter action (add, remove, edit, count, list, exit):";
    private static final String ADD_ENTER_NAME_MSG = "Enter the name:";
    private static final String ADD_ENTER_SURNAME_MSG = "Enter the surname:";
    private static final String ADD_ENTER_NUMBER_MSG = "Enter the number:";
    private static final String ADD_WRONG_NUMBER_MSG = "Wrong number format!";
    private static final String ADD_RECORD_ADDED_MSG = "The record added.";

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
        System.out.printf(COUNT_CONTACT_MSG_PATTERN, count);

        currentState = ConsoleViewState.MENU;
    }

    private void editContact() {
        if (contacts.getCount() == 0) {
            System.out.println(EDIT_NO_RECORDS_MSG);
            currentState = ConsoleViewState.MENU;
            return;
        }

        final List<Contact> allContacts = this.contacts.getAll();
        final int selectedContactIndex = selectContact(allContacts);

        if (selectedContactIndex < 0) {
            return;
        }

        Contact selectedContact = allContacts.get(selectedContactIndex);

        String editAction = readLine(EDIT_SELECT_CONTACT_MSG)
                .trim()
                .toLowerCase();

        boolean status;
        switch (editAction) {
            case "name":
                String name = readLine(EDIT_ENTER_NAME_MSG);
                status = contacts.updateContact(selectedContactIndex,
                        new Contact(name, selectedContact.getSurname(), selectedContact.getNumber()));
                break;
            case "surname":
                String surname = readLine(EDIT_ENTER_SURNAME_MSG);
                status = contacts.updateContact(selectedContactIndex,
                        new Contact(selectedContact.getName(), surname, selectedContact.getNumber()));
                break;
            case "number":
                String number = readLine(EDIT_ENTER_NUMBER_MSG);

                if (isValidNumber(number)) {
                    status = contacts.updateContact(selectedContactIndex,
                            new Contact(selectedContact.getName(), selectedContact.getSurname(), number));
                } else {
                    status = contacts.updateContact(selectedContactIndex,
                            new Contact(selectedContact.getName(), selectedContact.getSurname(), ""));
                    System.out.println(EDIT_NOT_VALID_NUMBER_MSG);
                }
                break;
            default:
                System.out.println("Field not found!");
                return;
        }

        if (status) {
            System.out.println(EDIT_UPDATE_MSG);
        }

        currentState = ConsoleViewState.MENU;
    }

    private void removeContact() {
        if (contacts.getCount() == 0) {
            System.out.println(REMOVE_NO_RECORDS_MSG);
            currentState = ConsoleViewState.MENU;
            return;
        }

        final List<Contact> allContacts = this.contacts.getAll();
        final int selectedContactIndex = selectContact(allContacts);

        if (selectedContactIndex < 0) {
            return;
        }

        if (contacts.remove(allContacts.get(selectedContactIndex))) {
            System.out.println(REMOVE_RECORD_REMOVED_MSG);
        }

        currentState = ConsoleViewState.MENU;
    }

    private int selectContact(List<Contact> contacts) {
        printContactsList(contacts);

        int selected = -1;
        try {
            selected = Integer.parseInt(readLine(SELECT_RECORD_MSG).trim()) - 1;
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
        switch (readLine(SHOW_MENU_SELECT_MSG).trim().toLowerCase()) {
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
        String name = readLine(ADD_ENTER_NAME_MSG);
        String surname = readLine(ADD_ENTER_SURNAME_MSG);
        String number = readLine(ADD_ENTER_NUMBER_MSG);

        if (!isValidNumber(number)) {
            System.out.println(ADD_WRONG_NUMBER_MSG);
            number = "";
        }

        if (contacts.addContact(name, surname, number)) {
            System.out.println(ADD_RECORD_ADDED_MSG);
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

    private String readLine(String msg) {
        System.out.println(msg);
        System.out.print("> ");
        return scanner.nextLine();
    }
}
