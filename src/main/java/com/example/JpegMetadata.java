package com.example;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

public class JpegMetadata {
    public void getMetadata(String path) throws Exception{
        File origen = new File(path);
        final ImageMetadata meta = Imaging.getMetadata(origen);
        final JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) meta;
        if(jpegImageMetadata != null){
            for(TiffField t: jpegImageMetadata.getExif().getAllFields()){
                System.out.println(t.getTagName() + " " + t.getValueDescription());
            }
        }
        TiffOutputSet outputSet = new TiffOutputSet();
        final double longitude = -74.0;
        final double latitude = 40 + 43 / 60.0;
        outputSet.setGpsInDegrees(longitude, latitude);
        TiffOutputDirectory dir = outputSet.getOrCreateExifDirectory();
        dir.add(ExifTagConstants.EXIF_TAG_OWNER_NAME, "EJEMPLONAME");
    }

    public void deleteMetadata(String path) throws Exception{
        String newpath = path.split("\\.")[0] + "2" + ".jpg";
        File newfile = new File(newpath);
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(newfile);
        OutputStream os = new BufferedOutputStream(fos);
        new ExifRewriter().removeExifMetadata(file, os);
    }
}
