package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import android.util.Base64;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

    public Data(
            @Attribute(name = "filename") String filename,
            @Element(name = "bytes") String encoded) {
            this.filename = filename;
            this.encoded = encoded;
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
