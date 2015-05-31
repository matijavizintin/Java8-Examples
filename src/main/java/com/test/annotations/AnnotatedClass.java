package com.test.annotations;

/**
 * Created by Matija Vižintin
 * Date: 31. 05. 2015
 * Time: 19.43
 */
@Hint(value = "Hint1")
@Hint(value = "Hint2")
@TypeUseAnnotation
public class AnnotatedClass<@TypeParameterAnnotation ANNOTATED_GENERIC> {
    public @TypeUseAnnotation ANNOTATED_GENERIC value;
}
