package com.bdg.xml_json.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JAXB {

    public File marshal(Student fromObj, File toXml) {
        if (fromObj == null || toXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Student.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fromObj, toXml);

            return toXml;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public File marshalList(StudentList fromObjects, File toXml) {
        if (fromObjects == null || toXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(StudentList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fromObjects, toXml);

            return toXml;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public Student unmarshal(File fromXml) {
        if (fromXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Student.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return (Student) unmarshaller.unmarshal(fromXml);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public StudentList unmarshalList(File fromXml) {
        if (fromXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(StudentList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return (StudentList) unmarshaller.unmarshal(fromXml);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}