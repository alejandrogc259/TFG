package com.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PlantillasManager {
    private static JSONArray plantillas;

    public static void cargarPlantillas(){
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            JSONArray list = (JSONArray) parser.parse(reader);
            reader.close();
            plantillas = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getNombresPlantillas(){
        List<String> nombresPlantillas = new ArrayList<>();
        try {
            if(plantillas == null){
                cargarPlantillas();
            }
            for(Object object: plantillas){
                JSONObject jo = (JSONObject) object;
                nombresPlantillas.add((String) jo.get("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nombresPlantillas;
    }

    public static void editarNombrePlantilla(String plantillaSeleccionada, String nuevoNombre){
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            for(Object object: plantillas){
                JSONObject jobject = (JSONObject) object;
                if(jobject.get("name").equals(plantillaSeleccionada)){
                    jobject.put("name", nuevoNombre);
                }
            }

            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void anadirPlantilla(String nombre){
        try {
            JSONObject jo = new JSONObject();
            jo.put("name", nombre);
            JSONObject jo1 = new JSONObject();
            jo.put("metadata", jo1);
            jo1.put("title", "");
            jo1.put("author", "");
            jo1.put("subject", "");
            jo1.put("keywords", "");
            jo1.put("comments", "");
            jo1.put("last author", "");
            jo1.put("edit time", "");
            jo1.put("last printed", "");
            jo1.put("create date time", "");
            jo1.put("last save date time", "");
            jo1.put("application name", "");
            jo.put("custom", new JSONObject());
            plantillas.add(jo);
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void eliminarPlantilla(String plantillaSeleccionada){
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            Object borrar = null;
            for(Object object: plantillas){
                JSONObject jobject = (JSONObject) object;
                if(jobject.get("name").equals(plantillaSeleccionada)){
                    borrar = object;
                }
            }
            plantillas.remove(borrar);

            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void modificarValor(String plantillaSeleccionada, String metadato, String valor){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject metadatos = (JSONObject) jo.get("metadata");
                for (Object nombreMetadato : metadatos.keySet()) {
                    if(((String) nombreMetadato).equals(metadato)){
                        metadatos.put(nombreMetadato, valor);
                    }
                }
            }
        }
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void anadirCustomMetadato(String plantillaSeleccionada, String metadato, String valor){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject custom = (JSONObject) jo.get("custom");
                custom.put(metadato, valor);
            }
        }
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarValorCustom(String plantillaSeleccionada, String metadato, String valor){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject custom = (JSONObject) jo.get("custom");
                for (Object nombreMetadato : custom.keySet()) {
                    if(((String) nombreMetadato).equals(metadato)){
                        custom.put(nombreMetadato, valor);
                    }
                }
            }
        }
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarNombreMeta(String plantillaSeleccionada, String antiguoNombre, String nuevoNombre){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject custom = (JSONObject) jo.get("custom");
                for (Object nombreMetadato : custom.keySet()) {
                    if(((String) nombreMetadato).equals(antiguoNombre)){
                        System.out.println("HOLA25");
                        String old = (String) custom.get(antiguoNombre);
                        custom.remove(antiguoNombre);
                        custom.put(nuevoNombre, old);
                    }
                }
            }
        }
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarValorCustom(String plantillaSeleccionada, String metadato){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject custom = (JSONObject) jo.get("custom");
                for (Object nombreMetadato : custom.keySet()) {
                    if(((String) nombreMetadato).equals(metadato)){
                        custom.remove(metadato);
                    }
                }
            }
        }
        try {
            Writer writer = new FileWriter("C:/Users/aleja/Desktop/pruebafx/demo/src/main/resources/com/example/plantillas.json");
            writer.write(plantillas.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getMetadatosPlantilla(String plantillaSeleccionada){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject metadatos = (JSONObject) jo.get("metadata");
                return metadatos;
            }
        }
        return null;
    }

    public static JSONObject getCustomMetadatosPlantilla(String plantillaSeleccionada){
        for(Object object: plantillas){
            JSONObject jo = (JSONObject) object;
            if(jo.get("name").equals(plantillaSeleccionada)){
                JSONObject custom = (JSONObject) jo.get("custom");
                return custom;
            }
        }
        return null;
    }
}
