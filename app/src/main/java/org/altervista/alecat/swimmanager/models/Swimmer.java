package org.altervista.alecat.swimmanager.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alessandro Cattapan on 23/08/2017.
 */

public class Swimmer {

    // This class describes a swimmingpool user
    private String name;
    private String surname;
    private String birthday;
    private int gender;
    private int level;

    // Void constructor for firebase database
    public Swimmer(){}

    // Real Object's constructor
    public Swimmer (String name, String surname, String birthday, int gender, int level) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.gender = gender;
        this.level = level;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getBirthday() { return birthday; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public int getGender() { return gender; }

    public void setGender (int gender) { this.gender = gender; }

    public int getLevel() { return level; }

    public void setLevel (int level) { this.level = level; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("surname", surname);
        result.put("birthday", birthday);
        result.put("gender", gender);
        result.put("level", level);

        return result;
    }


}
