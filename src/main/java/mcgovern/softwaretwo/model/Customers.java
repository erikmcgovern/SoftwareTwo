package mcgovern.softwaretwo.model;

/**
 * Class to describe customers.
 *
 * @author Erik McGovern
 */
public class Customers {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;

    /**
     * Constructor for customers.
     * @param id customers id.
     * @param name customers name.
     * @param address customers address.
     * @param postalCode customers postal code.
     * @param phoneNumber customers phone number.
     * @param divisionId customers division id.
     */
    public Customers(int id, String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }

    /**
     * Getter for customers id.
     * @return customers id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for customers name.
     * @return customers name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for customers address.
     * @return customers address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for customers postal code.
     * @return customers postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter for customers phone number.
     * @return customers phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter for customers division id.
     * @return customers division id.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Overrides toString method.
     * @return customers name.
     */
    public String toString() {
        return name;
    }
}
