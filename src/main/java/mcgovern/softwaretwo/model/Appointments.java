package mcgovern.softwaretwo.model;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * Class to describe appointments.
 *
 * @author Erik McGovern
 */
public class Appointments {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructor for appointments.
     * @param appointmentId appointments id.
     * @param title appointments title.
     * @param description appointments description.
     * @param location appointments location.
     * @param type appointments type.
     * @param start appointments start.
     * @param end appointments end.
     * @param customerId customers id.
     * @param userId users id.
     * @param contactId contacts id.
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Getter for appointment id.
     * @return appointment id.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Getter for appointment title.
     * @return appointment title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for appointment description.
     * @return appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for appointment location.
     * @return appointment location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for appointment type.
     * @return appointment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for appointment start.
     * @return appointment start.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter for appointment end.
     * @return appointment end.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Getter for appointment customer id.
     * @return customer id.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Getter for appointment user id.
     * @return user id.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for appointment contact id.
     * @return contact id.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Getter for month of start.
     * @return month.
     */
    public Month getMonth() {
        return start.getMonth();
    }

    /**
     * Overrides toString method.
     * @return appointment id as String.
     */
    public String toString() {
        return String.valueOf(appointmentId);
    }
}
