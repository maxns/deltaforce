package org.namstorm.deltaforce.annotations.processors.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by maxnam-storm on 12/8/2016.
 */
public class DFUtilTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void compileAlias() throws Exception {

        assertEquals("null alias given", "Name", DFUtil.compileAlias("name", null));
        assertEquals("empty alias given", "Name", DFUtil.compileAlias("name", ""));

        assertEquals("simple replace", "SurName", DFUtil.compileAlias("lastName", "SurName"));

        assertEquals("suffix alias a given", "NameValue", DFUtil.compileAlias("name", "+Value"));
        assertEquals("char trimmer given", "Name", DFUtil.compileAlias("names", "-s"));
        assertEquals("word trimmer given", "Name", DFUtil.compileAlias("nameValues", "-Values"));
        assertEquals("trimmer and suffix combo given", "MetaSetting", DFUtil.compileAlias("metaValuesMap", "-ValuesMap;+Setting"));
    }

}