package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import pixy.image.tiff.FieldType;
import pixy.image.tiff.TiffTag;
import pixy.meta.Metadata;
import pixy.meta.MetadataType;
import pixy.meta.exif.Exif;
import pixy.meta.exif.ExifTag;
import pixy.meta.iptc.IPTC;
import pixy.meta.iptc.IPTCApplicationTag;
import pixy.meta.iptc.IPTCDataSet;
import pixy.meta.jpeg.JPGMeta;
import pixy.meta.jpeg.JpegExif;
import pixy.meta.tiff.TiffExif;
import pixy.meta.xmp.XMP;
import pixy.string.XMLUtils;

public class ImageMetadata {
    public void deleteMetadata(String path) throws Exception{
        try {
            for(MetadataType a: MetadataType.values()){
                String newpath = path.split("\\.")[0] + 1 + "." + path.split("\\.")[1];
                FileInputStream fin = new FileInputStream(path);
                FileOutputStream fos = new FileOutputStream(newpath);
                Metadata.removeMetadata(fin, fos, MetadataType.EXIF, MetadataType.IPTC, MetadataType.XMP, MetadataType.ICC_PROFILE, MetadataType.PHOTOSHOP_IRB);
                fin.close();
                fos.close();
                File f = new File(path);
                f.delete();
                File f2 = new File(newpath);
                f2.renameTo(new File(path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void aplicarPlantilla(String path, String plantillaSeleccionada, String type){
        try {
            deleteMetadata(path);
            JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
            String newpath = path.split("\\.")[0] + 1 + "." + path.split("\\.")[1];
            FileInputStream fin = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(newpath);
            Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(path);
            Exif e = null;
            XMP xmp = null;
            Document xmpDoc = null;
            IPTC iptc = null;
            List<IPTCDataSet> iptcs = new ArrayList<>();
            if(Arrays.asList("jpg", "jpeg", "tiff", "tif").contains(type)){
                if(metadataMap.get(MetadataType.EXIF) != null) {
                    e = (Exif)metadataMap.get(MetadataType.EXIF);
                }
                else{
                    if(type.equals("jpg") || type.equals("jpeg"))e = new JpegExif();
                    else if(type.equals("tiff") || type.equals("tif"))e = new TiffExif();
                }
                if(metadataMap.get(MetadataType.IPTC) != null) {
                    iptc = (IPTC)metadataMap.get(MetadataType.IPTC);
                }
                else{iptc = new IPTC();}
            }
            if(metadataMap.get(MetadataType.XMP) != null) {
                xmp = (XMP)metadataMap.get(MetadataType.XMP);
            }
            else{
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                xmpDoc = document;
                Element asd = xmpDoc.createElement("x:xmpmeta");
                asd.setAttribute("xmlns:x", "adobe:ns:meta/");
                asd.setAttribute("x:xmptk", "Adobe XMP Core 4.0-c316 44.253921, Sun Oct 01 2006 17:14:39");
                xmpDoc.appendChild(asd);
            }
            if(xmp!=null){
                xmpDoc = xmp.getXmpDocument();
            }
            if(e != null){
                if(!metadatos.get("author").toString().equals("")){
                    e.addExifField(ExifTag.WINDOWS_XP_AUTHOR, FieldType.WINDOWSXP, metadatos.get("author").toString());
                    e.addImageField(TiffTag.ARTIST, FieldType.ASCII, metadatos.get("author").toString());
                    e.addExifField(ExifTag.OWNER_NAME, FieldType.ASCII, metadatos.get("author").toString());
                    iptcs.add(new IPTCDataSet(IPTCApplicationTag.WRITER_EDITOR, metadatos.get("author").toString()));
                    iptcs.add(new IPTCDataSet(IPTCApplicationTag.BY_LINE, metadatos.get("author").toString()));
                }
                if(!metadatos.get("last save date time").toString().equals("")){
                    e.addImageField(TiffTag.DATETIME, FieldType.ASCII, metadatos.get("last save date time").toString());
                    e.addExifField(ExifTag.DATE_TIME_ORIGINAL, FieldType.ASCII, metadatos.get("last save date time").toString());
                    e.addExifField(ExifTag.DATE_TIME_DIGITIZED, FieldType.ASCII, metadatos.get("last save date time").toString());
                }
                if(!metadatos.get("create date time").toString().equals("")){
                    DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime fecha = LocalDateTime.parse(metadatos.get("create date time").toString(), formatoOriginal);
                    DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
                    String fechaFormateada = fecha.format(formatoDeseado);

                    iptcs.add(new IPTCDataSet(IPTCApplicationTag.DATE_CREATED, fechaFormateada));
                    e.addExifField(ExifTag.DATE_TIME_ORIGINAL, FieldType.ASCII, fechaFormateada);
                    e.addImageField(TiffTag.DATETIME, FieldType.ASCII, fechaFormateada);
                }
                if(!metadatos.get("title").toString().equals("")){
                    e.addImageField(TiffTag.IMAGE_DESCRIPTION, FieldType.ASCII, metadatos.get("title").toString());
                    e.addExifField(ExifTag.WINDOWS_XP_TITLE, FieldType.WINDOWSXP, metadatos.get("title").toString());
                }
                if(!metadatos.get("comments").toString().equals("")){
                    e.addExifField(ExifTag.USER_COMMENT, FieldType.UNDEFINED, metadatos.get("comments").toString().getBytes());
                    e.addExifField(ExifTag.WINDOWS_XP_COMMENT, FieldType.WINDOWSXP, metadatos.get("comments").toString());
                }
                if(!metadatos.get("keywords").toString().equals("")){
                    e.addExifField(ExifTag.WINDOWS_XP_KEYWORDS, FieldType.WINDOWSXP, metadatos.get("keywords").toString());
                    iptcs.add(new IPTCDataSet(IPTCApplicationTag.KEY_WORDS, metadatos.get("keywords").toString()));
                }
                if(!metadatos.get("subject").toString().equals("")){
                    e.addExifField(ExifTag.WINDOWS_XP_SUBJECT, FieldType.WINDOWSXP, metadatos.get("subject").toString());
                    iptcs.add(new IPTCDataSet(IPTCApplicationTag.SUBJECT_REF, metadatos.get("subject").toString()));
                }
                e.addImageField(TiffTag.SOFTWARE, FieldType.ASCII, "");
                
                Metadata.insertExif(fin, fos, e, false);
                
            }
            fin.close();
            fos.close();
            fin = new FileInputStream(newpath);
            String newpath1 = newpath.split("\\.")[0] + 1 + "." + path.split("\\.")[1];
            fos = new FileOutputStream(newpath1);
            if(xmp != null){
                if(!xmp.hasExtendedXmp()){
                    //System.out.println(xmpDoc.getChildNodes().item(0).getNodeName());
                    //System.out.println(xmpDoc.getChildNodes().item(1).getNodeName());
                    //System.out.println(xmpDoc.getChildNodes().item(2).getNodeName());
                    Node n = null;
                    for(int i=0; i < xmpDoc.getChildNodes().getLength(); i++){
                        Node n1 = null;
                        if(xmpDoc.getChildNodes().item(i).getNodeName().equals("x:xmpmeta"))n1 = xmpDoc.getChildNodes().item(i);
                        for(int j=0; j < n1.getChildNodes().getLength(); j++){
                            if(n1.getChildNodes().item(i).getNodeName().equals("rdf:RDF"))n = n1.getChildNodes().item(i);   
                        }
                    }
                    //System.out.println(n.getAttributes().item(0));
                    Element newRDF = xmpDoc.createElement("rdf:RDF");
                    newRDF.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
                    newRDF.appendChild(createNewDC(xmpDoc, metadatos));
                    newRDF.appendChild(createNewPS(xmpDoc, metadatos));
                    xmpDoc.getChildNodes().item(1).replaceChild(newRDF, n);
                    Metadata.insertXMP(fin, fos, XMLUtils.serializeToStringLS(xmpDoc, xmpDoc.getDocumentElement()));
                }
                else {
                    Node n = null;
                    for(int i=0; i < xmpDoc.getChildNodes().getLength(); i++){
                        Node n1 = null;
                        if(xmpDoc.getChildNodes().item(i).getNodeName().equals("x:xmpmeta"))n1 = xmpDoc.getChildNodes().item(i);
                        for(int j=0; j < n1.getChildNodes().getLength(); j++){
                            if(n1.getChildNodes().item(i).getNodeName().equals("rdf:RDF"))n = n1.getChildNodes().item(i);   
                        }
                    }
                    //System.out.println(n.getAttributes().item(0));
                    Element newRDF = xmpDoc.createElement("rdf:RDF");
                    newRDF.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
                    newRDF.appendChild(createNewDC(xmpDoc, metadatos));
                    newRDF.appendChild(createNewPS(xmpDoc, metadatos));
                    xmpDoc.getChildNodes().item(1).replaceChild(newRDF, n);
                    Document extendedXmpDoc = xmp.getExtendedXmpDocument();
                    JPGMeta.insertXMP(fin, fos, XMLUtils.serializeToStringLS(xmpDoc, xmpDoc.getDocumentElement()), XMLUtils.serializeToStringLS(extendedXmpDoc));
                }
            }
            else{
                Element newRDF = xmpDoc.createElement("rdf:RDF");
                newRDF.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
                newRDF.appendChild(createNewDC(xmpDoc, metadatos));
                newRDF.appendChild(createNewPS(xmpDoc, metadatos));
                for(int i=0; i < xmpDoc.getChildNodes().getLength(); i++){
                    if(xmpDoc.getChildNodes().item(i).getNodeName().equals("x:xmpmeta"))xmpDoc.getChildNodes().item(i).appendChild(newRDF);
                }
                
                Metadata.insertXMP(fin, fos, XMLUtils.serializeToStringLS(xmpDoc, xmpDoc.getDocumentElement()));
            }
            

            fin.close();
            fos.close();
            fin = new FileInputStream(newpath1);
            String newpath2 = newpath1.split("\\.")[0] + 1 + "." + path.split("\\.")[1];
            fos = new FileOutputStream(newpath2);
            
            if(!type.equals("png"))Metadata.insertIPTC(fin, fos, iptcs, false);
            fin.close();
            fos.close();
            File f = new File(path);
            f.delete();
            File f2 = new File(newpath);
            f2.delete();
            File f3 = new File(newpath1);
            f3.delete();
            File f4 = new File(newpath2);
            f4.renameTo(new File(path));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Element createNewDC(Document xmpDoc, JSONObject metadatos){
        Element nuevoDC = xmpDoc.createElement("rdf:Description");
        nuevoDC.setAttribute("rdf:about", "");
        nuevoDC.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        if(!metadatos.get("author").toString().equals("")){
            Element dcCreator = xmpDoc.createElement("dc:creator");
            Element liElement = xmpDoc.createElement("rdf:li");
            liElement.setTextContent(metadatos.get("author").toString());
            Element seqElement = xmpDoc.createElement("rdf:Seq");
            seqElement.appendChild(liElement);
            dcCreator.appendChild(seqElement);
            nuevoDC.appendChild(dcCreator);
        }
        if(!metadatos.get("last save date time").toString().equals("")){
            Element dcDate = xmpDoc.createElement("dc:date");
            Element liElement1 = xmpDoc.createElement("rdf:li");
            liElement1.setTextContent(metadatos.get("last save date time").toString());
            Element seqElement1 = xmpDoc.createElement("rdf:Seq");
            seqElement1.appendChild(liElement1);
            dcDate.appendChild(seqElement1);
            nuevoDC.appendChild(dcDate);
        }
        if(!metadatos.get("comments").toString().equals("")){
            Element dcDescription = xmpDoc.createElement("dc:description");
            Element liElement2 = xmpDoc.createElement("rdf:li");
            liElement2.setAttribute("xml:lang", "x-default");
            liElement2.setTextContent(metadatos.get("comments").toString());
            Element altElement = xmpDoc.createElement("rdf:Alt");
            altElement.appendChild(liElement2);
            dcDescription.appendChild(altElement);
            nuevoDC.appendChild(dcDescription);
        }
        if(!metadatos.get("subject").toString().equals("")){
            Element dcSubject = xmpDoc.createElement("dc:subject");
            Element liElement3 = xmpDoc.createElement("rdf:li");
            liElement3.setTextContent(metadatos.get("subject").toString());
            Element bagElement = xmpDoc.createElement("rdf:Bag");
            bagElement.appendChild(liElement3);
            dcSubject.appendChild(bagElement);
            nuevoDC.appendChild(dcSubject);
        }
        if(!metadatos.get("title").toString().equals("")){
            Element dcTitle = xmpDoc.createElement("dc:title");
            Element liElement4 = xmpDoc.createElement("rdf:li");
            liElement4.setAttribute("xml:lang", "x-default");
            liElement4.setTextContent(metadatos.get("title").toString());
            Element altElement1 = xmpDoc.createElement("rdf:Alt");
            altElement1.appendChild(liElement4);
            dcTitle.appendChild(altElement1);
            nuevoDC.appendChild(dcTitle);
        }
        return nuevoDC;
    }

    public Element createNewPS(Document xmpDoc, JSONObject metadatos){
        Element nuevoPS = xmpDoc.createElement("rdf:Description");
        nuevoPS.setAttribute("rdf:about", "");
        nuevoPS.setAttribute("xmlns:photoshop", "http://ns.adobe.com/photoshop/1.0/");
        if(!metadatos.get("author").toString().equals("")){
            Element captionWriter = xmpDoc.createElement("photoshop:CaptionWriter");
            captionWriter.setTextContent(metadatos.get("author").toString());
            nuevoPS.appendChild(captionWriter);
        }
        if(!metadatos.get("create date time").toString().equals("")){
            Element dateCreated = xmpDoc.createElement("photoshop:DateCreated");
            dateCreated.setTextContent(metadatos.get("create date time").toString());
            nuevoPS.appendChild(dateCreated);
        }
        return nuevoPS;
    }

    
}
