package org.legacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Bridge} annotation is used to mark methods that require special processing.
 * <p>
 * This annotation can be applied to methods and is retained at runtime, making it
 * accessible for reflection. It can be used to provide additional information about
 * the method for the purposes of code generation or other processing.
 * </p>
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/annotations/index.html">Java Annotations</a>
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Bridge("Some value")
 * public void myAnnotatedMethod() {
 *     // method implementation
 * }
 * }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME) // The annotation is available at runtime
@Target(ElementType.METHOD) // This annotation can only be applied to methods
public @interface Bridge {
    /**
     * An optional value associated with the annotation.
     *
     * @return the string value provided with the annotation
     */
    String value() default "";
}
