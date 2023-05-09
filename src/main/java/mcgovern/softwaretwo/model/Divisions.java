package mcgovern.softwaretwo.model;

/**
 * Class to describe divisions.
 *
 * @author Erik McGovern
 */
public class Divisions {
    private int divisionId;
    private String name;
    private int countryId;

    /**
     * Constructor for divisions.
     * @param divisionId divisions id.
     * @param name divisions name.
     * @param countryId divisions country id.
     */
    public Divisions(int divisionId, String name, int countryId) {
        this.divisionId = divisionId;
        this.name = name;
        this.countryId = countryId;
    }

    /**
     * Getter for divisions id.
     * @return division id.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Getter for divisions name.
     * @return divisions name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for divisions country id.
     * @return country id.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Overrides toString method.
     * @return divisions name.
     */
    public String toString() {
        return name;
    }
}
