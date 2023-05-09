package mcgovern.softwaretwo.model;

/**
 * Class to describe contacts.
 *
 * @author Erik McGovern
 */
public class Contacts {
    private int id;
    private String name;
    private String email;

    /**
     * Constructor for contacts.
     * @param id contacts id.
     * @param name contacts name.
     * @param email contacts email.
     */
    public Contacts(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Getter for contacts id.
     * @return contacts id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for contacts name.
     * @return contacts name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for contacts email.
     * @return contacts email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Overrides toString method.
     * @return contacts name.
     */
    public String toString() {
        return name;
    }
}
