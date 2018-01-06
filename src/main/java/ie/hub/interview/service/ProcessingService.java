package ie.hub.interview.service;

import ie.hub.interview.model.Countries;
import ie.hub.interview.model.Partners;

public interface ProcessingService {

    Countries process(Partners partners);
}
