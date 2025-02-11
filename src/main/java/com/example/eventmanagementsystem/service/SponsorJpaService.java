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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.example.eventmanagementsystem.model.*;
import com.example.eventmanagementsystem.repository.SponsorJpaRepository;

import java.util.*;
import com.example.eventmanagementsystem.repository.*;

@Service
public class SponsorJpaService implements SponsorRepository {
    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;
    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Override
    public ArrayList<Sponsor> getSponsors() {
        List<Sponsor> sponsorsList = sponsorJpaRepository.findAll();
        ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorsList);
        return sponsors;
    }

    @Override
    public Sponsor getSponsorById(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            return sponsor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Sponsor addSponsor(Sponsor sponsor) {
        try {
            List<Integer> eventIds = new ArrayList<>();
            for (Event event : sponsor.getEvents()) {
                eventIds.add(event.getEventId());
            }
            List<Event> events = eventJpaRepository.findAllById(eventIds);
            if (eventIds.size() != events.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            sponsor.setEvents(events);
            return sponsorJpaRepository.save(sponsor);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Sponsor updateSponsor(int sponsorId, Sponsor sponsor) {
        try {
            Sponsor newSponsor = sponsorJpaRepository.findById(sponsorId).get();
            if (sponsor.getSponsorName() != null)
                newSponsor.setSponsorName(sponsor.getSponsorName());
            if (sponsor.getIndustry() != null)
                newSponsor.setIndustry(sponsor.getIndustry());
            if (sponsor.getEvents() != null) {
                List<Integer> eventIds = new ArrayList<>();
                for (Event event : sponsor.getEvents())
                    eventIds.add(event.getEventId());
                List<Event> events = eventJpaRepository.findAllById(eventIds);
                if (eventIds.size() != events.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newSponsor.setEvents(events);
            }
            return sponsorJpaRepository.save(sponsor);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteSponsor(int sponsorId) {
        try {
            sponsorJpaRepository.deleteById(sponsorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Event> getSponsorEvent(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            return sponsor.getEvents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
