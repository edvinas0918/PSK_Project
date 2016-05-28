package interceptors;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Edvinas.Barickis on 5/2/2016.
 */

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Authentication {
    String[] role() default {"Member", "Admin"};
}
