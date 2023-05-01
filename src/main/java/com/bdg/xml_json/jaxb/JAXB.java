package com.bdg.xml_json.jaxb;

import javax.xml.bind.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JAXB {

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>(
                List.of(
                        new Student(33, "Arman", "Hakhveryan", "arm", 80),
                        new Student(53, "Perch", "Harutyunyan", "perch", 80)));


        JAXB jaxb = new JAXB();
//        jaxb.marshal(
//                new Student(21, "Ani", "Hakobyan", "ani-hyan", 100),
//                new File("src/main/resources/xj/students.xml"));
//        jaxb.marshal(
//                new Student(33, "Arman", "Hakhveryan", "arm", 80),
//                new File("src/main/resources/xj/students.xml")
//        );
//
//        jaxb.marshalList(new StudentList(students), new File("src/main/resources/xj/students_without_marks.xml"));

        jaxb.unmarshal(new File("src/main/resources/xj/students.xml"));
    }

    public void marshal(Student fromObj, File toXml) {
        if (fromObj == null || toXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Student.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fromObj, toXml);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public void marshalList(StudentList fromObjects, File toXml) {
        if (fromObjects == null || toXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(StudentList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fromObjects, toXml);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public void unmarshal(File fromXml) {
        if (fromXml == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(StudentList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            JAXBElement<StudentList> root = (JAXBElement<StudentList>) unmarshaller.unmarshal(fromXml);
            List<StudentMod> students = root.getValue().getStudents();
            System.out.println(students);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}