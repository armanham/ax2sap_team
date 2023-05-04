package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerPassInTrip;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModPassInTrip;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.PassInTripMod;
import com.bdg.airport_management_system.model.PassengerMod;
import com.bdg.airport_management_system.persistent.PassInTripPer;
import com.bdg.airport_management_system.persistent.PassengerPer;
import com.bdg.airport_management_system.persistent.TripPer;
import com.bdg.airport_management_system.validator.Validator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.Set;
import java.util.stream.Collectors;

public class PassInTripService {

    private final PerToModPassInTrip perToMod = new PerToModPassInTrip();
    private final ModToPerPassInTrip modToPer = new ModToPerPassInTrip();


    public Set<PassInTripMod> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<PassInTripPer> query = session.createQuery("FROM PassInTripPer ", PassInTripPer.class);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no order:");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<PassInTripMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public PassInTripMod save(int tripId, PassengerMod passenger, String place) {
        TripService tripService = new TripService();
        PassengerService passengerService = new PassengerService();

        if (tripService.getBy(tripId) == null) {
            System.out.println("Trip with " + tripId + " does not exist: ");
            return null;
        }

        int passengerId = passengerService.getId(passenger);
        if (passengerId < 0) {
            passengerId = passengerService.save(passenger).getId();
        }

        if (place == null || place.length() > 3) {
            throw new IllegalArgumentException("Invalid place: ");
        }

        PassInTripMod passInTripMod = new PassInTripMod(tripService.getBy(tripId), passenger, place);
        if (exists(passInTripMod)) {
            System.out.println("Order already exists: ");
            return null;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            PassInTripPer passInTripPer = modToPer.getPersistentFrom(passInTripMod);
            passInTripPer.setTrip(session.get(TripPer.class, tripId));
            passInTripPer.setPassenger(session.get(PassengerPer.class, passengerId));

            session.save(passInTripPer);
            passInTripMod.setId(passInTripPer.getId());

            transaction.commit();
            return passInTripMod;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public PassInTripMod delete(int tripId, int passengerId, String place) {
        Validator.checkId(tripId);
        Validator.checkId(passengerId);

        TripService tripService = new TripService();
        PassengerService passengerService = new PassengerService();

        if (tripService.getBy(tripId) == null) {
            System.out.println("Trip with " + tripId + " does not exist: ");
            return null;
        }

        if (passengerService.getBy(passengerId) == null) {
            System.out.println("Passenger with " + passengerId + " does not exist: ");
            return null;
        }

        if (place == null || place.length() > 3) {
            throw new IllegalArgumentException("Invalid place: ");
        }

        PassInTripMod passInTripMod = new PassInTripMod(tripService.getBy(tripId), passengerService.getBy(passengerId), place);
        int id = getId(passInTripMod);
        if (id < 0) {
            System.out.println("Order does not exist: ");
            return null;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(PassInTripPer.class, id));

            transaction.commit();
            return passInTripMod;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public Set<PassInTripMod> getAllByPassenger(int id) {
        Validator.checkId(id);

        return getAll()
                .stream()
                .filter(passInTripMod -> id == passInTripMod.getPassenger().getId())
                .collect(Collectors.toSet());
    }


    public Set<PassInTripMod> getAllByTrip(int id) {
        Validator.checkId(id);

        return getAll()
                .stream()
                .filter(passInTripMod -> id == passInTripMod.getTrip().getId())
                .collect(Collectors.toSet());
    }


    public int getId(PassInTripMod passInTripMod) {
        Validator.checkNull(passInTripMod);

        for (PassInTripMod tempPassInTripMod : getAll()) {
            if (passInTripMod.equals(tempPassInTripMod)) {
                return tempPassInTripMod.getId();
            }
        }
        return -1;
    }


    public boolean exists(PassInTripMod passInTripMod) {
        Validator.checkNull(passInTripMod);

        for (PassInTripMod tempPassInTripMod : getAll()) {
            if (passInTripMod.equals(tempPassInTripMod)) {
                return true;
            }
        }
        return false;
    }
}