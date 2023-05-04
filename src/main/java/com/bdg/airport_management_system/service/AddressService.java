package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerAddress;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModAddress;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.persistent.AddressPer;
import com.bdg.airport_management_system.repository.AddressRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.Set;

import static com.bdg.airport_management_system.validator.Validator.checkId;
import static com.bdg.airport_management_system.validator.Validator.checkNull;


public class AddressService implements AddressRepository {

    private final ModToPerAddress modToPer = new ModToPerAddress();
    private final PerToModAddress perToMod = new PerToModAddress();


    @Override
    public AddressMod getBy(int id) {
        checkId(id);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            AddressPer addressPer = session.get(AddressPer.class, id);
            if (addressPer == null) {
                System.out.println("There is no address with " + id + " id: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return perToMod.getModelFrom(addressPer);
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<AddressMod> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<AddressPer> query = session.createQuery("FROM AddressPer", AddressPer.class);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no address:");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<AddressMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<AddressMod> get(int offset, int perPage, String sort) {
        if (offset <= 0 || perPage <= 0) {
            throw new IllegalArgumentException("Passed non-positive value as 'offset' or 'perPage': ");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Passed null or empty value as 'sort': ");
        }
        if (!sort.equals("id") && !sort.equals("country") && !sort.equals("city")) {
            throw new IllegalArgumentException("Parameter 'sort' must be 'id' or 'country' or 'city': ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<AddressPer> query = session.createQuery("FROM AddressPer order by " + sort);
            query.setFirstResult(offset);
            query.setMaxResults(perPage);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no address to show:");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<AddressMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public AddressMod save(AddressMod item) {
        checkNull(item);
        if (exists(item)) {
            System.out.println("[" + item + "] already exists: ");
            return null;
        }

        AddressPer addressPer = modToPer.getPersistentFrom(item);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.save(addressPer);
            item.setId(addressPer.getId());

            transaction.commit();
            return item;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean updateBy(int idToUpdate, String newCountry, String newCity) {
        checkId(idToUpdate);

        if (getBy(idToUpdate) == null) {
            System.out.println("Address with " + idToUpdate + " id not found: ");
            return false;
        }

        if (exists(new AddressMod(newCountry, newCity))) {
            System.out.println("Address with " + newCountry + ", " + newCity + " already exists: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            AddressPer addressPer = session.get(AddressPer.class, idToUpdate);

            if (!(newCountry == null || newCountry.isEmpty())) {
                addressPer.setCountry(newCountry);
            }
            if (!(newCity == null || newCity.isEmpty())) {
                addressPer.setCity(newCity);
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
            System.out.println("Address with " + id + " id not found: ");
            return false;
        }

        PassengerService passengerService = new PassengerService();
        if (!passengerService.getAllBy(id).isEmpty()) {
            System.out.println("First remove address by " + id + " in passenger table: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(AddressPer.class, id));

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public int getId(AddressMod item) {
        checkNull(item);

        for (AddressMod addressModTemp : getAll()) {
            if (addressModTemp.equals(item)) {
                return addressModTemp.getId();
            }
        }
        return -1;
    }


    @Override
    public boolean exists(AddressMod item) {
        checkNull(item);

        for (AddressMod addressModTemp : getAll()) {
            if (addressModTemp.equals(item)) {
                return true;
            }
        }
        return false;
    }
}