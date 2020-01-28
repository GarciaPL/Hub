package ie.hub.interview.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Participant {

    private Integer count;
    private Set<String> emails;

}