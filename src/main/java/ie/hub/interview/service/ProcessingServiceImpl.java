package ie.hub.interview.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import ie.hub.interview.model.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.base.AbstractInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessingServiceImpl implements ProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Countries process(Partners partners) {

        Countries countries = new Countries();
        ArrayList<Country> countriesList = Lists.newArrayList();

        Map<String, List<Partner>> partnersByCountry = partners.getPartners().stream().collect(Collectors.groupingBy(Partner::getCountry));
        for (Map.Entry<String, List<Partner>> countryEntry : partnersByCountry.entrySet()) {
            String country = countryEntry.getKey();
            List<Partner> partnersOfCountry = countryEntry.getValue();

            List<DateTime> availableDatesOfCountry = partnersOfCountry.stream()
                    .flatMap(p -> p.getAvailableDates().stream())
                    .collect(Collectors.toList());
            if (!availableDatesOfCountry.isEmpty()) {
                DateTime minDate = getMinAvailableDate(availableDatesOfCountry);
                DateTime maxDate = getMaxAvailableDate(availableDatesOfCountry);
                LOGGER.info("Available date range for {0} partners - min : {1} | max : {2}", country, minDate, maxDate);

                TreeMap<Interval, Participant> datesRangeOfCountry = new TreeMap<>(new IntervalStartComparator());
                for (int i = 0; i < getDays(minDate, maxDate); i++) {
                    DateTime minLocal = (i == 0) ? minDate : minDate.plusDays(i);
                    final Interval interval = new Interval(minLocal, minLocal.plusDays(1));
                    Set<String> collectTwoDaysInRow = collectEmailsWithTwoDaysRange(partnersOfCountry, interval);

                    if (datesRangeOfCountry.containsKey(interval)) {
                        Participant participant = datesRangeOfCountry.get(interval);
                        datesRangeOfCountry.put(interval, new Participant(participant.getCount() + collectTwoDaysInRow.size(),
                                Sets.newHashSet(Iterables.concat(participant.getEmails(), collectTwoDaysInRow))));
                    } else {
                        datesRangeOfCountry.put(interval, new Participant(collectTwoDaysInRow.size(), collectTwoDaysInRow));
                    }
                }

                Map<Interval, Participant> sortedDateRangesByParticipantsCount = sortDateRangesByParticipantsCount(datesRangeOfCountry);
                for (Map.Entry<Interval, Participant> sortedParticipantsEntry : sortedDateRangesByParticipantsCount.entrySet()) {
                    Participant firstParticipant = sortedParticipantsEntry.getValue();

                    Country countryItem = new Country();
                    countryItem.setName(country);
                    if (firstParticipant.getCount() == 0) {
                        enrichCountryWithNoParticipants(countryItem);
                    } else {
                        List<Map.Entry<Interval, Participant>> sortByDatesWithEqualParticipantsCount =
                                sortDateRangesByFirstParticipantCount(sortedDateRangesByParticipantsCount, firstParticipant);
                        if (!sortByDatesWithEqualParticipantsCount.isEmpty()) {
                            countryItem.setAttendeeCount(sortByDatesWithEqualParticipantsCount.get(0).getValue().getCount());
                            countryItem.setAttendees(Lists.newArrayList(sortByDatesWithEqualParticipantsCount.get(0).getValue().getEmails()));
                            countryItem.setStartDate(sortByDatesWithEqualParticipantsCount.get(0).getKey().getStart());
                        } else {
                            enrichCountryWithNoParticipants(countryItem);
                        }
                    }
                    countriesList.add(countryItem);
                    break;
                }
            } else {
                LOGGER.info("{0} does not have partners who do not have any available dates", country);
            }
        }

        countries.setCountries(countriesList);

        return countries;
    }

    public class IntervalStartComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval x, Interval y) {
            return x.getStart().compareTo(y.getStart());
        }
    }

    private List<Map.Entry<Interval, Participant>> sortDateRangesByFirstParticipantCount(Map<Interval, Participant> sortedDateRanges,
                                                                                         final Participant firstParticipant) {
        return sortedDateRanges.entrySet().stream()
                .filter(entry -> entry.getValue().getCount().equals(firstParticipant.getCount()))
                .sorted(Comparator.comparing(o -> o.getKey().getStart())).collect(Collectors.toList());
    }

    private Map<Interval, Participant> sortDateRangesByParticipantsCount(TreeMap<Interval, Participant> datesRangeOfCountry) {
        return datesRangeOfCountry.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().getCount().compareTo(e1.getValue().getCount()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Set<String> collectEmailsWithTwoDaysRange(List<Partner> partnersOfCountry, final Interval interval) {
        return partnersOfCountry.stream().filter(partner -> {
            int counter = 0;
            for (DateTime dateTime : partner.getAvailableDates()) {
                if (interval.withEndMillis(interval.getEndMillis() + 1).contains(dateTime)) {
                    counter++;
                }
            }
            return counter >= 2;
        }).map(Partner::getEmail).collect(Collectors.toSet());
    }

    private int getDays(DateTime minDate, DateTime maxDate) {
        return Days.daysBetween(minDate.toLocalDate(), maxDate.toLocalDate()).getDays();
    }

    private DateTime getMinAvailableDate(List<DateTime> availableDatesOfCountry) {
        return availableDatesOfCountry.stream().min(AbstractInstant::compareTo).get();
    }

    private DateTime getMaxAvailableDate(List<DateTime> availableDatesOfCountry) {
        return availableDatesOfCountry.stream().max(AbstractInstant::compareTo).get();
    }

    private void enrichCountryWithNoParticipants(Country countryItem) {
        countryItem.setAttendeeCount(0);
        countryItem.setAttendees(Lists.newArrayList());
        countryItem.setStartDate(null);
    }

}
