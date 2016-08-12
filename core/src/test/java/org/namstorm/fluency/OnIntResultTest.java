package org.namstorm.fluency;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maxnam-storm on 12/8/2016.
 */
public class OnIntResultTest {
    @Test
    public void cast() throws Exception {
        OnIntResult.cast(10)
                .onPositive(r -> assertTrue("10 is 10", r == 10))
                .on0(r->fail("10 is no 0"))
                .onNegative(r->fail("10 is positive!"));

        OnIntResult.cast(0)
                .on0(r -> assertTrue("0", r == 0))
                .onPositive(r->fail("0 should not be positive"))
                .onNegative(r->fail("0 should not be negative"));

        OnIntResult.cast(-10)
                .onNegative(r -> assertTrue("-10", r == -10))
                .on0(r->fail("-10 should not be 0"))
                .onPositive(r->fail("-10 should not be positive"));

    }

    @Test
    public void on0() throws Exception {
        new OnIntResult(0)
                .on0(r -> assertTrue("0", r == 0))
                .onPositive(r->fail("0 should not be positive"))
                .onNegative(r->fail("0 should not be negative"));

    }

    @Test
    public void onPositive() throws Exception {
        new OnIntResult(10)
                .onPositive(r -> assertTrue("10 is 10", r == 10))
                .on0(r->fail("10 is no 0"))
                .onNegative(r->fail("10 is positive!"));

    }

    @Test
    public void onNegative() throws Exception {
        new OnIntResult(-10)
                .onNegative(r -> assertTrue("-10", r == -10))
                .on0(r->fail("-10 should not be 0"))
                .onPositive(r->fail("-10 should not be positive"));

    }

}