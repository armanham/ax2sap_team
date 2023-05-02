package com.bdg.xml_json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ActorGson {
    public void serializeGson(List<Actor> actor) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter("src/main/resources/xml/actor.json")) {
            gson.toJson(actor, writer);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Actor desSerializeGson() {

        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/main/resources/xml/actor.json")) {
            return gson.fromJson(reader, Actor.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Actor> deSerializeListGson() {

        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/main/resources/xml/actor.json")) {
            Type listType = new TypeToken<List<Actor>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
