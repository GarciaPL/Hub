package ie.hub.interview.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class HubProperties {

    private String key;
    private String inbound;
    private String outbound;

}
