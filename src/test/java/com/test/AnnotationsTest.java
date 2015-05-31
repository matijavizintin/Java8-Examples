package com.test;

import com.test.annotations.AnnotatedClass;
import com.test.annotations.Hint;
import com.test.annotations.Hints;
import com.test.annotations.TypeUseAnnotation;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matija Vižintin
 * Date: 31. 05. 2015
 * Time: 19.46
 */
public class AnnotationsTest extends LoggingTimedTest {

    /**
     * This test demonstrates the usage of repeatable annotations that are now possible with JDK 8.
     */
    @Test
    public void repeatable() {
        // accessing this way will return null because multiple @Hint are wrapped in @Hints
        Hint hint = AnnotatedClass.class.getAnnotation(Hint.class);
        Assert.assertNull(hint);

        // accessing @Hints
        Hints hints = AnnotatedClass.class.getAnnotation(Hints.class);
        Assert.assertNotNull(hints);
        Assert.assertEquals(2, hints.value().length);

        // accessing by type
        Hint[] hintArray = AnnotatedClass.class.getAnnotationsByType(Hint.class);
        Assert.assertEquals(2, hintArray.length);
    }

    /**
     * This annotations does nothing. This test is here to show that now annotation can be put anywhere.
     */
    @Test
    public void typeAnnotations() {
        // annotation in new instance
        @TypeUseAnnotation AnnotatedClass<String> clazz = new @TypeUseAnnotation AnnotatedClass<>();

        // annotated type parameter
        List<@TypeUseAnnotation String> emptyList = new ArrayList<>();

        // annotated nested type
        Map.@TypeUseAnnotation Entry entry = null;

    }
}
