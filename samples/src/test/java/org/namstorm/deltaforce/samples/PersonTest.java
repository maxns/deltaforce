package org.namstorm.deltaforce.samples;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by maxnam-storm on 5/8/2016.
 */
public class PersonTest extends TestCase {
    final int AGE = 42;
    final String FIRST_NAME = "Max";
    final String LAST_NAME = "NS";
    final Person.Gender GENDER = Person.Gender.MALE;


    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetters() throws Exception {

        Person person = new org.namstorm.deltaforce.samples.PersonBuilder(new Person())
                .setAge(AGE)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setGender(GENDER)
                .build();

        assertEquals(person.getAge(), AGE);
        assertEquals(person.getFirstName(), FIRST_NAME);
        assertEquals(person.getLastName(), LAST_NAME);
        assertEquals(person.getGender(), GENDER);
    }

    @Test
    public void testApply() throws Exception {
        org.namstorm.deltaforce.samples.PersonBuilder builder = new org.namstorm.deltaforce.samples.PersonBuilder(null);

        Person person = builder.from(new Person())
                .setAge(AGE)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setGender(GENDER)
                .build();

        assertEquals(person.getAge(), AGE);
        assertEquals(person.getFirstName(), FIRST_NAME);
        assertEquals(person.getLastName(), LAST_NAME);
        assertEquals(person.getGender(), GENDER);


        Person to = builder.apply(new Person());

        assertEquals(person.getAge(), to.getAge());
        assertEquals(person.getFirstName(), to.getFirstName());
        assertEquals(person.getLastName(), to.getLastName());
        assertEquals(person.getGender(), to.getGender());
    }

}