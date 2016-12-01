package com.meltwater.smsc.model;

import java.util.List;

/**
 * Class containing Group data
 */
public class Group {

    /**
     * id of the subscriber: group<int>
     */
    private String id;

    /**
     * the phone number pattern for the group
     */
    private List<String> phoneNumberPattern;

    public Group() {
    }

    public Group(String id, List<String> phoneNumberPattern) {
        this.id = id;
        this.phoneNumberPattern = phoneNumberPattern;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPhoneNumberPattern() {
        return phoneNumberPattern;
    }

    public void setPhoneNumberPattern(List<String> phoneNumberPattern) {
        this.phoneNumberPattern = phoneNumberPattern;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", phoneNumberPattern='" + phoneNumberPattern + '\'' +
                '}';
    }
}
