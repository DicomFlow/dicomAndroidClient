package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import com.github.dicomflow.androiddicomflow.util.FileUtil;

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
            this.encoded = FileUtil.encodeFileToBase64Binary(encoded);
    }

}
