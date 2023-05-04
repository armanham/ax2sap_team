package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerPassenger;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModPassenger;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.model.PassInTripMod;
import com.bdg.airport_management_system.model.PassengerMod;
import com.bdg.airport_management_system.persistent.AddressPer;
import com.bdg.airport_management_system.persistent.PassengerPer;
import com.bdg.airport_management_system.repository.PassengerRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bdg.airport_management_system.validator.Validator.*;

public class PassengerService implements PassengerRepository {

    private final ModToPerPassenger modToPer = new ModToPerPassenger();
    private final PerToModPassenger perToMod = new PerToModPassenger();
    private final AddressService addressService = new AddressService();
    private final PassInTripService passInTripService = new PassInTripService();


    @Override
    public PassengerMod getBy(int id) {
        checkId(id);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            PassengerPer passengerPer = session.get(PassengerPer.class, id);
            if (passengerPer == null) {
                System.out.println("Passenger with " + id + " id does not exists: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return perToMod.getModelFrom(passengerPer);
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<PassengerMod> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            TypedQuery<PassengerPer> query = session.createQuery("FROM PassengerPer ", PassengerPer.class);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no passenger: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<PassengerMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<PassengerMod> get(int offset, int perPage, String sort) {
        if (offset <= 0 || perPage <= 0) {
            throw new IllegalArgumentException("Passed non-positive value as 'offset' or 'perPage': ");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Passed null or empty value as 'sort': ");
        }
        if (!sort.equals("id") && !sort.equals("phone") && !sort.equals("address") && !sort.equals("name")) {
            throw new IllegalArgumentException("Parameter 'sort' must be " +
                    "'id' or 'name' or 'phone' or 'address': ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<PassengerPer> query = session.createQuery("FROM PassengerPer order by " + sort);
            query.setFirstResult(offset);
            query.setMaxResults(perPage);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no passenger: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<PassengerMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public PassengerMod save(PassengerMod item) {
        checkNull(item);

        if (exists(item)) {
            System.out.println("[" + item + "] passenger already exists: ");
            return item;
        }

        if (doesExistWith(item.getPhone())) {
            System.out.println("Passenger with " + item.getPhone() + " phone number already exists: ");
            return null;
        }

        int addressId = addressService.getId(item.getAddress());
        if (addressId < 0) {
            AddressMod newSavedAddress = addressService.save(item.getAddress());
            addressId = newSavedAddress.getId();
            System.out.println(newSavedAddress + "saved too: ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            PassengerPer passengerPer = modToPer.getPersistentFrom(item);
            passengerPer.setAddress(session.get(AddressPer.class, addressId));
            session.save(passengerPer);
            item.setId(passengerPer.getId());

            transaction.commit();
            return item;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean updateBy(int id, String newName, String newPhone, AddressMod newAddress) {
        checkId(id);

        if (getBy(id) == null) {
            System.out.println("Passenger with " + id + " not found: ");
            return false;
        }

        if (newPhone != null) {
            if (doesExistWith(newPhone)) {
                System.out.println("Passenger with " + newPhone + " phone number already exists: ");
                return false;
            }
        }

        int addressId = addressService.getId(newAddress);
        if (addressId < 0) {
            AddressMod newSavedAddress = addressService.save(newAddress);
            addressId = newSavedAddress.getId();
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            PassengerPer passengerPer = session.get(PassengerPer.class, id);
            if (!(newName == null || newName.isEmpty())) {
                passengerPer.setName(newName);
            }
            if (!(newPhone == null || newPhone.isEmpty())) {
                passengerPer.setPhone(newPhone);
            }
            if (newAddress != null) {
                passengerPer.setAddress(session.get(AddressPer.class, addressId));
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
            System.out.println("Passenger with " + id + " id not found: ");
            return false;
        }

        if (!passInTripService.getAllByPassenger(id).isEmpty()) {
            System.out.println("First remove passenger by " + id + " in PassInTrip table: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(PassengerPer.class, id));

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public int getId(PassengerMod item) {
        checkNull(item);

        for (PassengerMod tempPassengerMod : getAll()) {
            if (item.equals(tempPassengerMod)) {
                return tempPassengerMod.getId();
            }
        }
        return -1;
    }


    @Override
    public boolean exists(PassengerMod item) {
        checkNull(item);

        for (PassengerMod tempPassengerMod : getAll()) {
            if (item.equals(tempPassengerMod)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<PassengerMod> getAllOf(int tripId) {
        checkId(tripId);

        return passInTripService.getAll()
                .stream()
                .filter(passInTripMod -> tripId == passInTripMod.getTrip().getId())
                .map(PassInTripMod::getPassenger)
                .collect(Collectors.toList());
    }


    @Override
    public boolean registerTrip(int tripId, PassengerMod passenger, String place) {
        return passInTripService.save(tripId, passenger, place) != null;
    }


    @Override
    public boolean cancelTrip(int tripId, int passengerId, String place) {
        return passInTripService.delete(tripId, passengerId, place) != null;
    }


    public boolean doesExistWith(String phone) {
        validateString(phone);

        for (PassengerMod tempPassengerMod : getAll()) {
            if (tempPassengerMod.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }


    public Set<PassengerMod> getAllBy(int addressId) {
        checkId(addressId);

        return getAll()
                .stream()
                .filter(passengerMod -> addressId == passengerMod.getAddress().getId())
                .collect(Collectors.toSet());
    }
}