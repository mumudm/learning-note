package top.mumudm.annotation;

import java.lang.annotation.*;

/**
 * @author mumu
 * @date 2020/4/23 22:33
 */
@Target({
        ElementType.METHOD,
        ElementType.FIELD
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestAnnotation {

    int type() default 0;

    String level() default "info";

    String value() default "";

}