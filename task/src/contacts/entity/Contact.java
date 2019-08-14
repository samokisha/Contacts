package contacts.entity;

import java.util.Objects;

public class Contact {
    private String name;
    private String surname;
    private String number;

    public Contact(String name, String surname, String number) {
        this.name = name;
        this.surname = surname;

        if (number == null) {
            this.number = "";
        } else {
            this.number = number;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        if (number.isEmpty()) {
            return "[no number]";
        }
        return number;
    }

    public void setNumber(String number) {
        if (number == null) {
            this.number = "";
        } else {
            this.number = number;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(surname, contact.surname) &&
                Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, number);
    }
}
