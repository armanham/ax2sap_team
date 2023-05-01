package com.bdg.xml_json;


import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XMLValidator {

    public static void of(File xmlFile, File schemaFile) {
        if (xmlFile == null || schemaFile == null) {
            throw new NullPointerException("Passed null value for one or both of parameters: ");
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));

            System.out.println("Validation passed successfully: ");
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        XMLValidator.of(
                new File("src/main/resources/xj/students.xml"),
                new File("src/main/resources/xj/students.xsd"));
    }
}