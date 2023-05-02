package com.bdg.xml_json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class GSON {

    private Gson gson = new Gson();

    public void serialize(Actor actor) {

        gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter("src/main/resources/xml/actor.json")) {
            gson.toJson(actor, writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void serialize(List<Actor> actor) {

        gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter("src/main/resources/xml/actor.json")) {
            gson.toJson(actor, writer);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Actor deSerialize(File file) {

        gson = new Gson();
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Actor.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Actor> deSerializeList(File file) {

        gson = new Gson();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Actor>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
