package EJBComponents;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import java.util.Date;

/**
 * Created by Mindaugas on 28/03/16.
 */

@RequestScoped // @SessionScoped
@Stateful
public class TestComponent {

    public String sakykLabas() {
        return "Labas " + new Date() + " " + toString();
    }

    @PostConstruct
    public void init() {
        System.out.println(toString() + " constructed.");
    }

    @PreDestroy
    public void aboutToDie() {
        System.out.println(toString() + " ready to die.");
    }
}


