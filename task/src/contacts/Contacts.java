package contacts;

import contacts.entity.Contact;
import contacts.util.Storage;

import java.util.List;

public class Contacts {

    private final Storage storage;

    /**
     * @param storage where the app will be stores contact instances {@link Contact}.
     */
    public Contacts(Storage storage) {
        this.storage = storage;
    }

    /**
     * Add new {@link Contact} to storage.
     * @param name Name of contact.
     * @param surname Surname of contact.
     * @param number Phone number of contact.
     * @return Returned {@code true} if successful added, else {@code false}.
     */
    public boolean addContact(String name, String surname, String number) {
        Contact contact = new Contact(name, surname, number);
        if (!storage.hasContact(contact)) {
            storage.save(contact);
            return true;
        } else {
            return false;
        }
    }

    public int getCount() {
        return storage.count();
    }

    public List<Contact> getAll() {
        return storage.getAll();
    }

    public boolean remove(Contact contact) {
        return storage.delete(contact);
    }

    public boolean updateContact(int index, Contact contact) {
        return storage.updateContact(index, contact);
    }
}
