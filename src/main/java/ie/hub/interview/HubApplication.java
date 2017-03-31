package ie.hub.interview;

import ie.hub.interview.config.HubProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = HubProperties.class)
public class HubApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HubApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(true)
                .web(true)
                .run(args);
    }
}
