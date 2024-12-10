package com.example;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import jep.Interpreter;
import jep.SharedInterpreter;

public class MetadataManager {
    private Map metadata;

    public Map getMetadata(String path) throws Exception{
        String[] partespath = path.split("\\.");
        //System.out.println("Archivo con extensión "+partespath[partespath.length-1]);
        if(partespath[partespath.length-1].equals("doc")){
            
        }
        if(partespath[partespath.length-1].equals("docx")){

        }
        if(partespath[partespath.length-1].equals("odt")){
            
        }
        if(partespath[partespath.length-1].equals("pdf")){
            
        }
        if(partespath[partespath.length-1].equals("mp3")){

        }
        if(partespath[partespath.length-1].equals("jpeg")){

        }
        return null;    
    }

    public void deleteAll(File directory, Boolean incluir) throws Exception{
        File[] files = directory.listFiles();
        for(int i = 0; i < files.length; i++){
            //System.out.println(files[i].getAbsolutePath());
            String[] partespath = files[i].getAbsolutePath().split("\\.");
            if(partespath.length == 1 && incluir){deleteAll(files[i], incluir);}
            if(partespath[partespath.length-1].equals("doc")){new DocXlsPptMetadata().deleteMetadata(files[i].getAbsolutePath());}
            if(partespath[partespath.length-1].equals("docx")){new DocxmMetadata().deleteMetadata(files[i].getAbsolutePath());}
            if(partespath[partespath.length-1].equals("odt")){new OpenDocumentMetadata().deleteMetadata(files[i].getAbsolutePath());}
            if(partespath[partespath.length-1].equals("pdf")){new PdfMetadata().deleteMetadata(files[i].getAbsolutePath());}
            if(partespath[partespath.length-1].equals("jpg")){new ImageMetadata().deleteMetadata(files[i].getAbsolutePath());}
        }  
    }

    public void aplicarPlantilla(File directory, String plantillaSeleccionada, Boolean incluir){
        try {
            File[] files = directory.listFiles();
            for(int i = 0; i < files.length; i++){
                //System.out.println(files[i].getAbsolutePath());
                String[] partespath = files[i].getAbsolutePath().split("\\.");
                if(partespath.length == 1 && incluir){aplicarPlantilla(files[i], plantillaSeleccionada, incluir);}
                if(Arrays.asList("doc", "xls", "ppt").contains(partespath[partespath.length-1])){new DocXlsPptMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("docx", "docm").contains(partespath[partespath.length-1])){new DocxmMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("pptx", "pptm").contains(partespath[partespath.length-1])){new PptxmMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("xlsx", "xlsm").contains(partespath[partespath.length-1])){new XlsxmMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("jpg", "jpeg", "tiff", "tif").contains(partespath[partespath.length-1])){new ImageMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada, partespath[partespath.length-1]);}
                if(Arrays.asList("pdf").contains(partespath[partespath.length-1])){new PdfMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("odt", "ods", "odp", "odg").contains(partespath[partespath.length-1])){new OpenDocumentMetadata().aplicarPlantilla(files[i].getAbsolutePath(), plantillaSeleccionada);}
                if(Arrays.asList("mp3", "aif").contains(partespath[partespath.length-1])){
                    Interpreter interp = new SharedInterpreter();interp.set("plant", plantillaSeleccionada);interp.set("path", files[i].getAbsolutePath());
                    interp.runScript("C:\\Users\\aleja\\Desktop\\pruebafx\\demo\\src\\main\\java\\com\\example\\ID3APEMetadata.py");
                    interp.exec("aplicarPlantillaID3(path, plant)");}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
