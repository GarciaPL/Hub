package ie.hub.interview.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.hub.interview.config.HubProperties;
import ie.hub.interview.model.Countries;
import ie.hub.interview.model.Partners;
import ie.hub.interview.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class StartController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProcessingService processingService;

    @Autowired
    private HubProperties hubProperties;

    @RequestMapping("/start")
    public String sendRequest() throws JsonProcessingException {

        ResponseEntity<Partners> partnersResponse = restTemplate.getForEntity(createUrl(hubProperties.getInbound(), hubProperties.getKey()), Partners.class);

        Countries countries = processingService.process(partnersResponse.getBody());

        return restTemplate.postForObject(createUrl(hubProperties.getOutbound(), hubProperties.getKey()), createOutboundRequest(countries), String.class);
    }

    private String createUrl(String path, String key) {
        return UriComponentsBuilder.fromHttpUrl(path + key).build().toUriString();
    }

    private HttpEntity<Object> createOutboundRequest(Countries countries) {
        return new HttpEntity<Object>(countries, createHttpHeaders());
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return httpHeaders;
    }
}
