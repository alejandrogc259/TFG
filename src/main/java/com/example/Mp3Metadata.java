package com.example;

import java.io.File;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;

public class Mp3Metadata {
    public void getMetadata(String path) throws Exception{
        File file = new File(path);
            MP3File mp3file = (MP3File)AudioFileIO.read(file);
            Tag tag = mp3file.getTag();
            ID3v24Tag v24tag = mp3file.getID3v2TagAsv24();
            if(mp3file.hasID3v2Tag()){
                System.out.println("TIENE COSAS");
                System.out.println(v24tag.getFirst(FieldKey.YEAR));
                System.out.println(v24tag.getFirst(ID3v24Frames.FRAME_ID_ENCODEDBY));
                System.out.println(v24tag.getFirst(FieldKey.ALBUM));
                tag.setField(FieldKey.ALBUM, "ALBUMPRUEBA");
                AudioFileIO.write(mp3file);
            }
            Iterator<TagField> iterator = tag.getFields();
            while(iterator.hasNext()){
                System.out.println(iterator.next());
            }
            TagField binaryField = tag.getFirstField(FieldKey.YEAR);
            System.out.println(binaryField);
    }
}
