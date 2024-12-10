package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.attribute.meta.MetaValueTypeAttribute.Value;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.type.Duration;

public class OpenDocumentMetadata {
    public void deleteMetadata(String path) throws Exception{
        File document = new File(path);
        OdfDocument doc = (OdfDocument) OdfDocument.loadDocument(document);
        OdfOfficeMeta metadata = doc.getOfficeMetadata();
        metadata.setCreationDate(null);
        metadata.setCreator("");
        metadata.setDate(null);
        metadata.setDescription("");
        metadata.setEditingDuration(null);
        metadata.setInitialCreator("");
        List<String> keywords = metadata.getKeywords();
        if(keywords != null){keywords.clear(); metadata.setKeywords(keywords);}
        metadata.setLanguage("");
        metadata.setPrintDate(null);
        metadata.setPrintedBy("");
        metadata.setSubject("");
        metadata.setTitle("");
        List<String> names = metadata.getUserDefinedDataNames();
        if(names != null){for(String name: names){metadata.removeUserDefinedDataByName(name);}}
        //SE ACTUALIZA LA METADATA DE FECHA DE EDICION
        metadata.setAutomaticUpdate(false);
        doc.save(path);
    }

    public void aplicarPlantilla(String path, String plantillaSeleccionada){
        try {
            deleteMetadata(path);
            JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
            File document = new File(path);
            OdfDocument doc = (OdfDocument) OdfDocument.loadDocument(document);
            OdfOfficeMeta metadata = doc.getOfficeMetadata();

            if(!metadatos.get("author").toString().equals("")){
                metadata.setInitialCreator(metadatos.get("author").toString());
            }
            if(!metadatos.get("title").toString().equals("")){
                metadata.setTitle(metadatos.get("title").toString());
            }
            if(!metadatos.get("subject").toString().equals("")){
                metadata.setSubject(metadatos.get("subject").toString());
            }
            if(!metadatos.get("keywords").toString().equals("")){
                List<String> list = new ArrayList<>();
                list.add(metadatos.get("keywords").toString());
                metadata.setKeywords(list);
            }
            if(!metadatos.get("comments").toString().equals("")){
                metadata.setDescription(metadatos.get("comments").toString());
            }
            if(!metadatos.get("last author").toString().equals("")){
                metadata.setCreator(metadatos.get("last author").toString());
            }
            if(!metadatos.get("edit time").toString().equals("")){
                Duration res = Duration.valueOf(metadatos.get("edit time").toString());
                metadata.setEditingDuration(res);
            }
            if(!metadatos.get("last printed").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(metadatos.get("last printed").toString()));
                metadata.setPrintDate(cal);
            }
            if(!metadatos.get("create date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                cal.setTime(formatter.parse(metadatos.get("create date time").toString()));
                System.out.println(cal.getTime());
                metadata.setCreationDate(cal);
            }
            if(!metadatos.get("last save date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(metadatos.get("last save date time").toString()));
                metadata.setDate(cal);
            }
            if(!metadatos.get("application name").toString().equals("")){
                metadata.setGenerator(metadatos.get("application name").toString());
            }

            JSONObject custom = PlantillasManager.getCustomMetadatosPlantilla(plantillaSeleccionada);
            for (Object nombreMetadato : custom.keySet()) {
                metadata.setUserDefinedData((String)nombreMetadato, Value.STRING.toString(), (String)custom.get(nombreMetadato));
            }
            metadata.setAutomaticUpdate(false);
            doc.save(path);
        } catch (Exception e) {
        }
            
        
    }
}
