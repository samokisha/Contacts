package contacts.app;

import contacts.entity.Contact;
import contacts.util.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryStorage implements Storage {

    private final List<Contact> contacts = new ArrayList<>();

    @Override
    public boolean save(Contact contact) {
        if (!contacts.contains(contact)) {
            return contacts.add(contact);
        }

        return false;
    }

    @Override
    public Contact find(Contact contact) {
        return contacts.stream()
                .filter(c -> c.equals(contact))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Contact> findByName(String name) {
        return contacts.stream()
                .filter(c -> c.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> findBySurname(String surname) {
        return contacts.stream()
                .filter(c -> c.getSurname().equals(surname))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> findByNumber(String number) {
        return contacts.stream()
                .filter(c -> c.getNumber().equals(number))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Contact contact) {
        return contacts.remove(contact);
    }

    @Override
    public boolean hasContact(Contact contact) {
        return contacts.contains(contact);
    }
}
