package com.bdg.airport_management_system.insert_into_db;

import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.persistent.*;
import com.bdg.airport_management_system.validator.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class Inserter {

    private static final String ROOT_PATH = "src/main/resources/ams/";
    private static final Path PATH_COMPANY_TXT = Path.of(ROOT_PATH + "companies.txt");
    private static final Path PATH_ADDRESS_TXT = Path.of(ROOT_PATH + "addresses.txt");
    private static final Path PATH_PASSENGER_TXT = Path.of(ROOT_PATH + "passengers.txt");
    private static final Path PATH_TRIP_TXT = Path.of(ROOT_PATH + "trip.txt");
    private static final Path PATH_PASSINTRIP_TXT = Path.of(ROOT_PATH + "pass_in_trip.txt");


    public void insertCompanyTable() {
        Transaction transaction = null;
        List<String> lines = readLinesOfFileFrom(PATH_COMPANY_TXT);

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < (lines != null ? lines.size() : 0); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");

                String[] dateParts = fields[1].split("/");

                CompanyPer companyPer = new CompanyPer();
                companyPer.setName(fields[1]);
                companyPer.setFoundDate(
                        Date.valueOf(
                                LocalDate.of(
                                        Integer.parseInt(dateParts[2]),
                                        Integer.parseInt(dateParts[0]),
                                        Integer.parseInt(dateParts[1])
                                )));

                session.save(companyPer);
            }

            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public void insertAddressTable() {
        Transaction transaction = null;
        List<String> lines = readLinesOfFileFrom(PATH_ADDRESS_TXT);

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < (lines != null ? lines.size() : 0); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");

                AddressPer addressPer = new AddressPer();
                addressPer.setCountry(fields[0]);
                addressPer.setCity(fields[1]);

                session.save(addressPer);
            }

            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public void insertPassengerTable() {
        Transaction transaction = null;
        List<String> lines = readLinesOfFileFrom(PATH_PASSENGER_TXT);

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < (lines != null ? lines.size() : 0); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");

                AddressPer addressPer = session.get(AddressPer.class, Integer.parseInt(fields[2]));
                if (addressPer == null) {
                    transaction.rollback();
                    return;
                }
                PassengerPer passengerPer = new PassengerPer();
                passengerPer.setName(fields[0]);
                passengerPer.setPhone(fields[1]);
                passengerPer.setAddress(addressPer);

                session.save(passengerPer);
            }

            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public void insertTripTable() {
        Transaction transaction = null;
        List<String> lines = readLinesOfFileFrom(PATH_TRIP_TXT);

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < (lines != null ? lines.size() : 0); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");

                CompanyPer companyPer = session.get(CompanyPer.class, Integer.parseInt(fields[1]));
                if (companyPer == null) {
                    transaction.rollback();
                    return;
                }

                TripPer tripPer = new TripPer();
                tripPer.setTripNumber(Integer.parseInt(fields[0]));
                tripPer.setAirplane(fields[2]);
                tripPer.setTimeIn(Timestamp.valueOf(fields[5]));
                tripPer.setTimeOut(Timestamp.valueOf(fields[6]));
                tripPer.setTownFrom(fields[3]);
                tripPer.setTownTo(fields[4]);
                tripPer.setCompany(companyPer);

                session.save(tripPer);
            }

            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    public void insertPassInTripTable() {
        Transaction transaction = null;
        List<String> lines = readLinesOfFileFrom(PATH_PASSINTRIP_TXT);

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < (lines != null ? lines.size() : 0); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");

                TripPer tripPer = session.get(TripPer.class, Integer.parseInt(fields[0]));
                if (tripPer == null) {
                    transaction.rollback();
                    return;
                }

                PassengerPer passengerPer = session.get(PassengerPer.class, Integer.parseInt(fields[1]));
                if (passengerPer == null) {
                    transaction.rollback();
                    return;
                }

                PassInTripPer passInTripPer = new PassInTripPer();
                passInTripPer.setTrip(tripPer);
                passInTripPer.setPassenger(passengerPer);
                passInTripPer.setTime(Timestamp.valueOf(fields[2]));
                passInTripPer.setPlace(fields[3]);

                session.save(passInTripPer);
            }

            transaction.commit();
        } catch (Exception e) {
            assert transaction != null;
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }


    private List<String> readLinesOfFileFrom(Path path) {
        Validator.checkNull(path);

        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}