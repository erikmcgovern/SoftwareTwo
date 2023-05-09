package mcgovern.softwaretwo.model;

/**
 * Class to describe countries.
 *
 * @author Erik McGovern
 */
public class Countries {
    private int id;
    private String name;

    /**
     * Constructor for countries.
     * @param id countries id.
     * @param name countries name.
     */
    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for countries id.
     * @return countries id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for countries name.
     * @return countries name.
     */
    public String getName() {
        return name;
    }

    /**
     * Overrides toString method.
     * @return countries name.
     */
    public String toString() {
        return name;
    }
}
