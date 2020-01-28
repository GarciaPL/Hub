package ie.hub.interview.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class Partner {

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private List<DateTime> availableDates;

}
