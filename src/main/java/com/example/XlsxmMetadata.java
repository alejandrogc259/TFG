package com.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.ooxml.POIXMLProperties.CustomProperties;
import org.apache.poi.ooxml.POIXMLProperties.ExtendedProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

public class XlsxmMetadata {
    public void getMetadata(String path) throws Exception{
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xlsx = new XSSFWorkbook(is);
        xlsx.getProperties().getCoreProperties();
    }

    public void deleteMetadata(String path) throws Exception{
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xlsx = new XSSFWorkbook(is);
        CoreProperties coreProperties = xlsx.getProperties().getCoreProperties();
        CustomProperties customProperties = xlsx.getProperties().getCustomProperties();
        coreProperties.setCategory("");
        coreProperties.setContentStatus("");
        coreProperties.setContentType("");
        coreProperties.setCreated("");
        coreProperties.setCreator("");
        coreProperties.setDescription("");
        coreProperties.setIdentifier("");
        coreProperties.setKeywords("");
        coreProperties.setLastModifiedByUser("");
        coreProperties.setLastPrinted("");
        coreProperties.setModified("");
        coreProperties.setRevision("");
        coreProperties.setSubjectProperty("");
        coreProperties.setTitle("");
        coreProperties.setVersion("");
        coreProperties.setSubjectProperty("");
        customProperties = null;

        FileOutputStream xlsxToWrite = new FileOutputStream(path);
        xlsx.write(xlsxToWrite);
    }

    public void aplicarPlantilla(String path, String plantillaSeleccionada){
        try {
            JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
            InputStream is = new FileInputStream (path);
            XSSFWorkbook xlsx = new XSSFWorkbook(is);

            CoreProperties coreProperties = xlsx.getProperties().getCoreProperties();
            CustomProperties customProperties = xlsx.getProperties().getCustomProperties();
            ExtendedProperties extendedProperties = xlsx.getProperties().getExtendedProperties();
            if(!metadatos.get("author").toString().equals("")){
                coreProperties.setCreator(metadatos.get("author").toString());
            }
            if(!metadatos.get("title").toString().equals("")){
                coreProperties.setTitle(metadatos.get("title").toString());
            }
            if(!metadatos.get("comments").toString().equals("")){
                coreProperties.setDescription(metadatos.get("comments").toString());
            }
            if(!metadatos.get("keywords").toString().equals("")){
                coreProperties.setKeywords(metadatos.get("keywords").toString());
            }
            if(!metadatos.get("subject").toString().equals("")){
                coreProperties.setSubjectProperty(metadatos.get("subject").toString());
            }
            if(!metadatos.get("last author").toString().equals("")){
                coreProperties.setLastModifiedByUser(metadatos.get("last author").toString());
            }
            if(!metadatos.get("create date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Optional<Date> o = Optional.ofNullable(formatter.parse(metadatos.get("create date time").toString()));
                coreProperties.setCreated(o);
            }
            if(!metadatos.get("last printed").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Optional<Date> o = Optional.ofNullable(formatter.parse(metadatos.get("last printed").toString()));
                coreProperties.setLastPrinted(o);
            }
            if(!metadatos.get("last save date time").toString().equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Optional<Date> o = Optional.ofNullable(formatter.parse(metadatos.get("last save date time").toString()));
                coreProperties.setModified(o);
            }
            if(!metadatos.get("application name").toString().equals("")){
                extendedProperties.setApplication(metadatos.get("application name").toString());
            }
            if(!metadatos.get("edit time").toString().equals("")){
                extendedProperties.setTotalTime(Integer.parseInt(metadatos.get("edit time").toString()));
            }
            JSONObject custom = PlantillasManager.getCustomMetadatosPlantilla(plantillaSeleccionada);
            for (Object nombreMetadato : custom.keySet()) {
                customProperties.addProperty((String)nombreMetadato, (String)custom.get(nombreMetadato));
            }


            FileOutputStream docxToWrite = new FileOutputStream(path);

            xlsx.write(docxToWrite);
            xlsx.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
