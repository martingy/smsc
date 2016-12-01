package com.meltwater.smsc.model;

/**
 * Class containing data of subscriber
 */
public class Subscriber {

    /**
     * id of the subscriber: number<int>
     */
    private String id;

    /**
     * phone number of the subscriber:
     */
    private String phoneNumber;

    public Subscriber() {
    }

    public Subscriber(String id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id='" + id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
