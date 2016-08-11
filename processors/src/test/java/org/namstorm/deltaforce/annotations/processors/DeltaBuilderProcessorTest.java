package org.namstorm.deltaforce.annotations.processors;

import static com.google.common.truth.Truth.assertThat;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.tools.StandardLocation;

import static com.google.common.truth.Truth.assert_;

/**
 * Created by maxnamstorm on 10/8/2016.
 */
public class DeltaBuilderProcessorTest {


    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void matchBuilder() throws Exception {

        assert_().about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("mock/AnnotatedPojo.java"))
                .processedWith(new DeltaBuilderProcessor())
                .compilesWithoutError()
                //.generatesSources(JavaFileObjects.forResource("AnnotatedPojoBuilder.java"));

        ;
    }

}