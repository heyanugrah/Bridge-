package org.legacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define the annotation
@Retention(RetentionPolicy.RUNTIME) // The annotation is available at runtime
@Target(ElementType.METHOD) // This annotation can only be applied to methods
public @interface Bridge {
    String value() default "";
}
