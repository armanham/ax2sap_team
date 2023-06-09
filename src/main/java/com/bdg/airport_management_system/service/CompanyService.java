package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.converter.model_to_persistent.ModToPerCompany;
import com.bdg.airport_management_system.converter.persistent_to_model.PerToModCompany;
import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.model.CompanyMod;
import com.bdg.airport_management_system.persistent.CompanyPer;
import com.bdg.airport_management_system.repository.CompanyRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.Set;

import static com.bdg.airport_management_system.validator.Validator.*;


public class CompanyService implements CompanyRepository {

    private final ModToPerCompany modToPer = new ModToPerCompany();
    private final PerToModCompany perToMod = new PerToModCompany();


    @Override
    public CompanyMod getBy(int id) {
        checkId(id);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            CompanyPer companyPer = session.get(CompanyPer.class, id);
            if (companyPer == null) {
                System.out.println("There is no company with " + id + " id: ");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return perToMod.getModelFrom(companyPer);
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<CompanyMod> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<CompanyPer> query = session.createQuery("FROM CompanyPer", CompanyPer.class);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no company:");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<CompanyMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<CompanyMod> get(int offset, int perPage, String sort) {
        if (offset <= 0 || perPage <= 0) {
            throw new IllegalArgumentException("Passed non-positive value as 'offset' or 'perPage': ");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Passed null or empty value as 'sort': ");
        }
        if (!sort.equals("id") && !sort.equals("name") && !sort.equals("found_date")) {
            throw new IllegalArgumentException("Parameter 'sort' must be 'id' or 'name' or 'found_date': ");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            TypedQuery<CompanyPer> query = session.createQuery("FROM CompanyPer order by " + sort);
            query.setFirstResult(offset);
            query.setMaxResults(perPage);

            if (query.getResultList().isEmpty()) {
                System.out.println("There is no company:");
                transaction.rollback();
                return null;
            }

            transaction.commit();
            return (Set<CompanyMod>) perToMod.getModelListFrom(query.getResultList());
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public CompanyMod save(CompanyMod item) {
        checkNull(item);

        if (exists(item)) {
            System.out.println("[" + item + "] company already exists: ");
            return null;
        }

        if (doesExistWith(item.getName())) {
            System.out.println("Company with " + item.getName() + " already exists: ");
            return null;
        }

        CompanyPer companyPer = modToPer.getPersistentFrom(item);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.save(companyPer);
            item.setId(companyPer.getId());

            transaction.commit();
            return item;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean updateBy(int idToUpdate, String newName) {
        checkId(idToUpdate);

        if (getBy(idToUpdate) == null) {
            System.out.println("Company with " + idToUpdate + " id not found: ");
            return false;
        }

        if (doesExistWith(newName)) {
            System.out.println("Company with " + newName + " already exists: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            CompanyPer companyPer = session.get(CompanyPer.class, idToUpdate);

            if (!(newName == null || newName.isEmpty())) {
                companyPer.setName(newName);
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
            System.out.println("Company with " + id + " id not found: ");
            return false;
        }

        TripService tripService = new TripService();
        if (!tripService.getAllBy(id).isEmpty()) {
            System.out.println("First remove company by " + id + " in trip table: ");
            return false;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(CompanyPer.class, id));

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    @Override
    public int getId(CompanyMod item) {
        checkNull(item);

        for (CompanyMod tempCompanyMod : getAll()) {
            if (item.equals(tempCompanyMod)) {
                return tempCompanyMod.getId();
            }
        }
        return -1;
    }


    @Override
    public boolean exists(CompanyMod item) {
        checkNull(item);

        for (CompanyMod tempCompanyMod : getAll()) {
            if (item.equals(tempCompanyMod)) {
                return true;
            }
        }
        return false;
    }


    public boolean doesExistWith(String name) {
        validateString(name);

        for (CompanyMod tempCompanyMod : getAll()) {
            if (tempCompanyMod.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}