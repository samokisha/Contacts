package contacts.util;

import contacts.entity.Contact;

import java.util.List;

/**
 * Interface, that stores app data.
 */
public interface Storage {

    /**
     * Save contact in storage.
     * @param contact A {@link Contact} to save.
     * @return Return {@code true} if saved correct, else return {@code false}.
     */
    boolean save(Contact contact);

    /**
     * Find the same {@link Contact} in storage.
     * @param contact Instance of {@link Contact}.
     * @return Instance of {@link Contact} or null.
     */
    Contact find(Contact contact);

    /**
     * Find {@link Contact} (-s) in storage by the name.
     * @param name Contact's name.
     * @return List of found contacts.
     */
    List<Contact> findByName(String name);

    /**
     * Find {@link Contact} (-s) in storage by the surname.
     * @param surname Contact's surname.
     * @return List of found contacts.
     */
    List<Contact> findBySurname(String surname);

    /**
     * Find {@link Contact} (-s) in storage by the number.
     * @param number Contact's number.
     * @return List of found contacts.
     */
    List<Contact> findByNumber(String number);

    /**
     * Delete {@link Contact} from storage.
     * @param contact Contact to delete.
     * @return Return {@code true} if deleted, {@code false} if not exist.
     */
    boolean delete(Contact contact);

    /**
     * Is such a contact stored?
     * @param contact Contact to check.
     * @return Returned {@code true} if has, else {@code false}.
     */
    boolean hasContact(Contact contact);

    int count();

    List<Contact> getAll();

    boolean updateContact(int index, Contact contact);
}
