package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerTrip;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModTrip;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.TripMod;
import com.bdg.airport_management_system.persistent.CompanyPer;
import com.bdg.airport_management_system.persistent.PassInTripPer;
import com.bdg.airport_management_system.persistent.TripPer;
import com.bdg.airport_management_system.repository.TripRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.bdg.airport_management_system.validator.Validator.*;

public class TripService implements TripRepository {

    private static final PerToModTrip PER_TO_MOD = new PerToModTrip();
    private static final ModToPerTrip MOD_TO_PER = new ModToPerTrip();
    private static final CompanyService COMPANY_SERVICE = new CompanyService();


    @Override
    public Set<TripMod> getAllFrom(String city) {
        validateString(city);

        Set<TripMod> tripModSet = new LinkedHashSet<>();
        for (TripMod trip : getAll()) {
            if (trip.getTownFrom().equals(city)) {
                tripModSet.add(trip);
            }
        }
        return tripModSet;
    }


    @Override
    public Set<TripMod> getAllTo(String city) {
        validateString(city);

        Set<TripMod> tripModSet = new LinkedHashSet<>();
        for (TripMod trip : getAll()) {
            if (trip.getTownTo().equals(city)) {
                tripModSet.add(trip);
            }
        }
        return tripModSet;
    }


    @Override
    public TripMod getBy(int id) {
        checkId(id);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer trip = session.get(TripPer.class, id);
            if (trip == null) {
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return PER_TO_MOD.getModelFrom(trip);
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
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<TripMod>) PER_TO_MOD.getModelListFrom(query.getResultList());
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
                transaction.commit();
                return null;
            }

            transaction.commit();
            return (Set<TripMod>) PER_TO_MOD.getModelListFrom(query.getResultList());
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

        int companyId = COMPANY_SERVICE.getId(item.getCompany());
        if (companyId < 0) {
            COMPANY_SERVICE.save(item.getCompany());
        }


        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer tripPer = MOD_TO_PER.getPersistentFrom(item);
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
                            String newAirplane, String newTownFrom, String newTownTo,
                            Timestamp newTimeOut,
                            Timestamp newTimeIn) {
        checkId(idToUpdate);

        if (getBy(idToUpdate) == null) {
            System.out.println("Trip with " + idToUpdate + " id not found: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TripPer trip = session.get(TripPer.class, idToUpdate);
            if (trip == null) {
                transaction.rollback();
                return false;
            }

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

        if (existsPassInTripBy(id)) {
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


    public boolean exists(TripMod item) {
        checkNull(item);

        for (TripMod tempTripMod : getAll()) {
            if (item.equals(tempTripMod)) {
                return true;
            }
        }
        return false;
    }


    public int getId(TripMod item) {
        checkNull(item);

        for (TripMod tempTripMod : getAll()) {
            if (item.equals(tempTripMod)) {
                return tempTripMod.getId();
            }
        }
        return -1;
    }


    private boolean existsPassInTripBy(int tripId) {
        checkId(tripId);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            String hql = "select pt from PassInTripPer as pt where pt.trip = :tripId";
            TypedQuery<PassInTripPer> passInTripTypedQuery = session.createQuery(hql);
            passInTripTypedQuery.setParameter("tripId", tripId);
            transaction.commit();
            return !passInTripTypedQuery.getResultList().isEmpty();
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}