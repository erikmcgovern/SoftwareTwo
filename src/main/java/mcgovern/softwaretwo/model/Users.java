package mcgovern.softwaretwo.model;

/**
 * Class to describe users.
 *
 * @author Erik McGovern
 */
public class Users {
    private int userId;
    private String userName;
    private String password;

    /**
     * Constructor for users.
     * @param userId users id.
     * @param userName users username.
     * @param password users password.
     */
    public Users(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Getter for users id.
     * @return users id.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for users username.
     * @return users username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Overrides toString method.
     * @return users username.
     */
    public String toString() {
        return userName;
    }

}
