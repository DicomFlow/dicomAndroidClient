package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import android.util.Base64;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Complete;
import org.simpleframework.xml.core.Persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ricardobarbosa on 22/06/17.
 */

@Root
public class Data {
    @Attribute public final String filename;
    @Element(name = "bytes") public final String encoded;

    public Data(String filename) {
        this.filename = filename;
        this.encoded = encodeFileToBase64Binary(filename);
    }

    private String encodeFileToBase64Binary(String filename){
        String encodedfile = null;
        try {
            File file = new File(filename);
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeToString(bytes, Base64.CRLF);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }
}
