/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * 
 * import java.util.*;
 *
 */

// Write your code here
package com.example.eventmanagementsystem.service;

import org.springframework.stereotype.Service;

import com.example.eventmanagementsystem.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.eventmanagementsystem.repository.*;
import java.util.*;
import com.example.eventmanagementsystem.model.*;

@Service
public class EventJpaService implements EventRepository {
    @Autowired
    private EventJpaRepository eventJpaRepository;
    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;

    @Override
    public ArrayList<Event> getEvents() {
        List<Event> eventList = eventJpaRepository.findAll();
        ArrayList<Event> events = new ArrayList<>(eventList);
        return events;
    }

    @Override
    public Event getEventById(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            return event;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event addEvent(Event event) {
        List<Integer> sponsorIds = new ArrayList<>();
        for (Sponsor sponsor : event.getSponsors()) {
            sponsorIds.add(sponsor.getSponsorId());
        }
        List<Sponsor> sponsors = sponsorJpaRepository.findAllById(sponsorIds);

        event.setSponsors(sponsors);
        for (Sponsor sponsor : sponsors) {
            sponsor.getEvents().add(event);
        }

        Event savedEvent = eventJpaRepository.save(event);
        sponsorJpaRepository.saveAll(sponsors);
        return savedEvent;
    }

    @Override
    public Event updateEvent(int eventId, Event event) {
        try {
            Event newEvent = eventJpaRepository.findById(eventId).get();
            if (event.getEventName() != null)
                newEvent.setEventName(event.getEventName());
            if (event.getDate() != null)
                newEvent.setDate(event.getDate());
            if (event.getSponsors() != null) {
                List<Integer> sponsorIds = new ArrayList<>();
                for (Sponsor sponsor : event.getSponsors()) {
                    sponsorIds.add(sponsor.getSponsorId());
                }

                List<Sponsor> sponsors = sponsorJpaRepository.findAllById(sponsorIds);

                for (Sponsor sponsor : sponsors) {
                    sponsor.getEvents().add(newEvent);
                }

                sponsorJpaRepository.saveAll(sponsors);
                newEvent.setSponsors(sponsors);

            }

            return eventJpaRepository.save(event);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            List<Sponsor> sponsors = event.getSponsors();
            for (Sponsor sponser : sponsors)
                sponser.getEvents().remove(event);

            sponsorJpaRepository.saveAll(sponsors);
            eventJpaRepository.deleteById(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Sponsor> getEventSponsor(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            return event.getSponsors();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}