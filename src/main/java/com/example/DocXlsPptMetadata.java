package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.hpsf.CustomProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.simple.JSONObject;

public class DocXlsPptMetadata{
    public void deleteMetadata(String path) throws Exception{
        System.out.println(path);
        File summaryFile = new File(path);
            
        try (POIFSFileSystem poifs = new POIFSFileSystem(summaryFile, false)) {

            DirectoryEntry dir = poifs.getRoot();
            SummaryInformation si;
            try {
                si = (SummaryInformation) PropertySetFactory.create(
                        dir, SummaryInformation.DEFAULT_STREAM_NAME);
            } catch (FileNotFoundException ex) {
                si = PropertySetFactory.newSummaryInformation();
            }
            assert(si != null);

            
            si.setTitle("");
            si.setAuthor("");
            si.setSubject("");
            si.setKeywords("");
            si.setComments("");
            si.setTemplate("");
            si.setLastAuthor("");
            si.setEditTime(0);
            si.setLastPrinted(null);
            si.setCreateDateTime(null);
            si.setLastSaveDateTime(null);
            si.setApplicationName("");
            System.out.println(si.getTitle() + "HOLA");


            DocumentSummaryInformation dsi;
            try {
                dsi = (DocumentSummaryInformation) PropertySetFactory.create(
                        dir, DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            } catch (FileNotFoundException ex) {
                dsi = PropertySetFactory.newDocumentSummaryInformation();
            }
            assert(dsi != null);

            dsi.setCategory("");
            dsi.setPresentationFormat("");
            dsi.setManager("");
            dsi.setCompany("");
            dsi.setApplicationVersion(0);
            dsi.setContentType("");
            dsi.setContentStatus("");
            dsi.setLanguage("");
            dsi.setDocumentVersion("");

            CustomProperties customProperties = dsi.getCustomProperties();
            if (customProperties == null)
                customProperties = new CustomProperties();

            
            if(dsi.getCustomProperties() != null)dsi.removeCustomProperties();
            si.write(dir, SummaryInformation.DEFAULT_STREAM_NAME);
            dsi.write(dir, DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            
            poifs.writeFilesystem();
        }
    }

    public void aplicarPlantilla(String path, String plantillaSeleccionada){
        try {
            deleteMetadata(path); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
        File summaryFile = new File(path);
            
        try (POIFSFileSystem poifs = new POIFSFileSystem(summaryFile, false)) {
            DirectoryEntry dir = poifs.getRoot();
            SummaryInformation si;
            try {
                si = (SummaryInformation) PropertySetFactory.create(
                        dir, SummaryInformation.DEFAULT_STREAM_NAME);
            } catch (FileNotFoundException ex) {
                si = PropertySetFactory.newSummaryInformation();
            } catch (NoPropertySetStreamException ex) {
                si = PropertySetFactory.newSummaryInformation();
            } catch (UnsupportedEncodingException ex) {
                si = PropertySetFactory.newSummaryInformation();
            }
            assert(si != null);
            if(!metadatos.get("author").toString().equals("")){
                si.setAuthor(metadatos.get("author").toString());
            }
            if(!metadatos.get("title").toString().equals("")){
                si.setTitle(metadatos.get("title").toString());
            }
            if(!metadatos.get("subject").toString().equals("")){
                si.setSubject(metadatos.get("subject").toString());
            }
            if(!metadatos.get("keywords").toString().equals("")){
                si.setKeywords(metadatos.get("keywords").toString());
            }
            if(!metadatos.get("comments").toString().equals("")){
                si.setComments(metadatos.get("comments").toString());
            }
            if(!metadatos.get("last author").toString().equals("")){
                si.setLastAuthor(metadatos.get("last author").toString());
            }
            if(!metadatos.get("edit time").toString().equals("")){
                si.setEditTime(Long.parseLong(metadatos.get("edit time").toString()));
            }
            if(!metadatos.get("last printed").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                si.setLastPrinted(formatter.parse(metadatos.get("last printed").toString()));
            }
            if(!metadatos.get("create date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                si.setCreateDateTime(formatter.parse(metadatos.get("create date time").toString()));
            }
            if(!metadatos.get("last save date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                si.setLastSaveDateTime(formatter.parse(metadatos.get("last save date time").toString()));
            }
            if(!metadatos.get("application name").toString().equals("")){
                si.setApplicationName(metadatos.get("application name").toString());
            }
            
            

            
            DocumentSummaryInformation dsi;
            try {
                dsi = (DocumentSummaryInformation) PropertySetFactory.create(
                        dir, DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            } catch (FileNotFoundException ex) {
                dsi = PropertySetFactory.newDocumentSummaryInformation();
            } catch (NoPropertySetStreamException ex) {
                dsi = PropertySetFactory.newDocumentSummaryInformation();
            } catch (UnsupportedEncodingException ex) {
                dsi = PropertySetFactory.newDocumentSummaryInformation();
            }
            assert(dsi != null);
            JSONObject custom = PlantillasManager.getCustomMetadatosPlantilla(plantillaSeleccionada);
            CustomProperties customProperties = dsi.getCustomProperties();
            if(customProperties == null){customProperties = new CustomProperties();}
            for (Object nombreMetadato : custom.keySet()) {
                customProperties.put((String)nombreMetadato, custom.get(nombreMetadato));
            }
            dsi.setCustomProperties(customProperties);
            si.write(dir, SummaryInformation.DEFAULT_STREAM_NAME);
            dsi.write(dir, DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            poifs.writeFilesystem();
        } catch (IOException ex) {
        } catch (WritingNotSupportedException ex) {
        } catch (ParseException ex) {
        }
            
        
    }
}
