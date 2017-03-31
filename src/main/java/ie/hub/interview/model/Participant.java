package ie.hub.interview.model;

import java.util.Set;

public class Participant {
    private Integer count;
    private Set<String> emails;

    public Participant(Integer count, Set<String> emails) {
        this.count = count;
        this.emails = emails;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }
}