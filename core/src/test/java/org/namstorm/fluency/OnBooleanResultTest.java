package org.namstorm.fluency;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maxnam-storm on 12/8/2016.
 */
public class OnBooleanResultTest {

    @Test
    public void testBooleanLogic() {
        boolean res;

        OnBooleanResult.FALSE
                .onFalse(result -> assertTrue("false should be false", !result))
                .onTrue(result -> fail("onTrue cannot be called from false"));

        OnBooleanResult.TRUE
                .onTrue(result -> assertTrue("false should be false", result))
                .onFalse(result -> fail("onTrue cannot be called from false"));


    }

}