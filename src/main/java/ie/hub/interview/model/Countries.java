package ie.hub.interview.model;

import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class Countries {

    @Singular
    private List<Country> countries;

}
