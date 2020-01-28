package ie.hub.interview.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class Country {

    private Integer attendeeCount;
    private List<String> attendees;
    private String name;
    private DateTime startDate;

}
