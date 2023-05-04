package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerTrip;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModTrip;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.CompanyMod;
import com.bdg.airport_management_system.model.TripMod;
import com.bdg.airport_management_system.persistent.CompanyPer;
import com.bdg.airport_management_system.persistent.TripPer;
import com.bdg.airport_management_system.repository.TripRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bdg.airport_management_system.validator.Validator.*;

public class TripService implements TripRepository {

    private final PerToModTrip perToMod = new PerToModTrip();
    private final ModToPerTrip modToPer = new ModToPerTrip();
    private final CompanyService companyService = new CompanyService();


    @Override
    public TripMod getBy(int id) {
        checkId(id);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer trip = session.get(TripPer.class, id);
            if (trip == null) {
                System.out.println("There is no trip with " + id + " id: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return perToMod.getModelFrom(trip);
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<TripMod> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            TypedQuery<TripPer> query = session.createQuery("FROM TripPer ", TripPer.class);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no trip: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<TripMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<TripMod> get(int offset, int perPage, String sort) {
        if (offset <= 0 || perPage <= 0) {
            throw new IllegalArgumentException("Passed non-positive value as 'offset' or 'perPage': ");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Passed null or empty value as 'sort': ");
        }
        if (
                !sort.equals("tripNumber") && !sort.equals("company") && !sort.equals("airplane") &&
                        !sort.equals("townFrom") && !sort.equals("townTo") && !sort.equals("timeOut") && !sort.equals("timeIn")
        ) {
            throw new IllegalArgumentException("Parameter 'sort' must be " +
                    "'tripNumber' or 'company' or 'airplane' or 'townFrom' or 'townTo' or 'timeOut' or 'timeIn': ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<TripPer> query = session.createQuery("FROM TripPer order by " + sort);
            query.setFirstResult(offset);
            query.setMaxResults(perPage);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no trip: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<TripMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public TripMod save(TripMod item) {
        checkNull(item);

        if (exists(item)) {
            System.out.println("[" + item + "] address already exists: ");
            return null;
        }

        int companyId = companyService.getId(item.getCompany());
        if (companyId < 0) {
            CompanyMod newSavedCompany = companyService.save(item.getCompany());
            System.out.println(newSavedCompany + "saved too: ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer tripPer = modToPer.getPersistentFrom(item);
            tripPer.setCompany(session.get(CompanyPer.class, companyId));
            session.save(tripPer);
            item.setId(tripPer.getId());

            transaction.commit();
            return item;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean updateBy(int idToUpdate,
                            String newAirplane,
                            String newTownFrom,
                            String newTownTo,
                            Timestamp newTimeOut,
                            Timestamp newTimeIn) {
        checkId(idToUpdate);

        TripMod tripMod = getBy(idToUpdate);
        if (tripMod == null) {
            System.out.println("Trip with " + idToUpdate + " id not found: ");
            return false;
        }

        if (exists(new TripMod(tripMod.getCompany(), newAirplane, newTownFrom, newTownTo, newTimeOut, newTimeIn))) {
            System.out.println("Trip already exists: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer trip = session.get(TripPer.class, idToUpdate);

            if (!(newAirplane == null || newAirplane.isEmpty())) {
                trip.setAirplane(newAirplane);
            }
            if (!(newTownFrom == null || newTownFrom.isEmpty())) {
                trip.setTownFrom(newTownFrom);
            }
            if (!(newTownTo == null || newTownTo.isEmpty())) {
                trip.setTownTo(newTownTo);
            }
            if (!(newTimeOut == null)) {
                trip.setTimeOut(newTimeOut);
            }
            if (!(newTimeIn == null)) {
                trip.setTimeIn(newTimeIn);
            }

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean deleteBy(int id) {
        checkId(id);

        if (getBy(id) == null) {
            System.out.println("Trip with " + id + " id not found: ");
            return false;
        }

        PassInTripService passInTripService = new PassInTripService();
        if (!passInTripService.getAllByTrip(id).isEmpty()) {
            System.out.println("First remove Trip by " + id + " in PassInTrip table: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(TripPer.class, id));

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean exists(TripMod item) {
        checkNull(item);

        for (TripMod tempTripMod : getAll()) {
            if (item.equals(tempTripMod)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getId(TripMod item) {
        checkNull(item);

        for (TripMod tempTripMod : getAll()) {
            if (item.equals(tempTripMod)) {
                return tempTripMod.getId();
            }
        }
        return -1;
    }


    @Override
    public Set<TripMod> getAllFrom(String city) {
        validateString(city);

        return getAll()
                .stream()
                .filter(tripMod -> city.equals(tripMod.getTownFrom()))
                .collect(Collectors.toSet());
    }


    @Override
    public Set<TripMod> getAllTo(String city) {
        validateString(city);

        return getAll()
                .stream()
                .filter(tripMod -> city.equals(tripMod.getTownTo()))
                .collect(Collectors.toSet());
    }


    public Set<TripMod> getAllBy(int companyId) {
        checkId(companyId);

        return getAll()
                .stream()
                .filter(tripMod -> companyId == tripMod.getCompany().getId())
                .collect(Collectors.toSet());
    }
}