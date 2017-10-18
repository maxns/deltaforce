package org.namstorm.deltaforce.samples;


import org.namstorm.deltaforce.annotations.DeltaForceBuilder;

/**
 * This JavaBean represents an Person in the On-line Store application.
 *
 * @author deors
 * @version 1.0
 */
@DeltaForceBuilder
public class Person {
    enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    private String firstName;
    private String lastName;
    private int age;
    private Gender gender;


    /**
     * Default constructor.
     */
    public Person() {
        super();
    }


}
