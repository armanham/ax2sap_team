package com.bdg.xml_json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONValidator {

    public static void of(Path jsonFile, File schemaFile) {
        try {
            String json = new String(Files.readAllBytes(jsonFile));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode schemaNode = mapper.readTree(schemaFile);

            JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
            JsonSchema schema = schemaFactory.getJsonSchema(schemaNode);

            schema.validate(mapper.readTree(json));
            System.out.println("Validation passed successfully: ");
        } catch (IOException | ProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}