package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import android.util.Base64;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 22/06/17.
 */

@Root
public class Data {
    @Attribute public final String filename;
    @Element(name = "bytes", required = false) public final String bytes;

    public Data(
            @Attribute(name = "filename") String filename,
            @Element(name = "bytes", required = false) String encoded) {
        if (filename != null && !filename.isEmpty()) {
            this.filename = filename;
            this.bytes = encodeFileToBase64Binary(filename);
        }else {
            this.filename = "";
            this.bytes = encoded != null ? encoded : "";
        }

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", filename);
        map.put("encoded", bytes);
        return map;
    }

}
