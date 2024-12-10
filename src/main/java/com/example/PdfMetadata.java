package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.json.simple.JSONObject;

import com.google.common.collect.Lists;

public class PdfMetadata {
    public void getMetadata(String path) throws Exception{
        File file = new File(path);
        PDDocument pdf = Loader.loadPDF(file);
        String newpath = path + "2" + ".pdf";
        File file2 = new File(newpath);
        pdf.save(file2);
        pdf.close();
        file.delete();
        file2.renameTo(new File(path));
    }

    public void deleteMetadata(String path) throws Exception{
        File file = new File(path);
        PDDocument pdf = Loader.loadPDF(file);
        pdf.setDocumentInformation(new PDDocumentInformation());
        pdf.getDocumentCatalog().setMetadata(null);
        Set<String> keys = pdf.getDocumentInformation().getMetadataKeys();
        List<String> defaults = Lists.newArrayList("Author", "CreationDate", "Creator", "Keywords", "ModificationDate", "Producer", "Subject", "Title", "Trapped");
        for(String key: keys){
            if(!defaults.contains(key)){
                pdf.getDocumentInformation().setCustomMetadataValue(key, "");
            }
        }
        String newpath = path + "2" + ".pdf";
        File file2 = new File(newpath);
        pdf.save(file2);
        pdf.close();
        file.delete();
        file2.renameTo(new File(path));
    }

    public void aplicarPlantilla(String path, String plantillaSeleccionada){
        try {
            deleteMetadata(path);
            JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
            File file = new File(path);
            PDDocument pdf = Loader.loadPDF(file);
            PDDocumentInformation info = pdf.getDocumentInformation();
            if(!metadatos.get("author").toString().equals("")){
                info.setAuthor(metadatos.get("author").toString());
                info.setCreator(metadatos.get("author").toString());
            }
            if(!metadatos.get("title").toString().equals("")){
                info.setTitle(metadatos.get("title").toString());
            }
            if(!metadatos.get("subject").toString().equals("")){
                info.setSubject(metadatos.get("subject").toString());
            }
            if(!metadatos.get("keywords").toString().equals("")){
                info.setKeywords(metadatos.get("keywords").toString());
            }
            if(!metadatos.get("create date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                cal.setTime(formatter.parse(metadatos.get("create date time").toString()));
                info.setCreationDate(cal);
            }
            if(!metadatos.get("last save date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(metadatos.get("last save date time").toString()));
                info.setModificationDate(cal);
            }
            JSONObject custom = PlantillasManager.getCustomMetadatosPlantilla(plantillaSeleccionada);
            for (Object nombreMetadato : custom.keySet()) {
                info.setCustomMetadataValue((String)nombreMetadato, (String)custom.get(nombreMetadato));
            }
            String newpath = path + "2" + ".pdf";
            File file2 = new File(newpath);
            pdf.save(file2);
            pdf.close();
            file.delete();
            file2.renameTo(new File(path));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
