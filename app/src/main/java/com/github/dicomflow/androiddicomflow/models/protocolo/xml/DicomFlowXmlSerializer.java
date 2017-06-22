package com.github.dicomflow.androiddicomflow.models.protocolo.xml;

import android.os.Environment;
import android.util.Log;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ricardobarbosa on 20/06/17.
 */

public class DicomFlowXmlSerializer {

    public static String serialize(Service service) {
        Serializer serializer = new Persister();

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "DicomFiles");
            root.mkdirs();

            File xmlFile = new File(root, String.format("%s.xml", service.getName().toLowerCase()));
            if (xmlFile.exists ()) xmlFile.delete();

            Log.d("File", xmlFile.getAbsolutePath());
            xmlFile.createNewFile();

            FileWriter writer = new FileWriter(xmlFile);
            serializer.write(service, writer);
            writer.flush();
            writer.close();

            FileReader reader = new FileReader(xmlFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String sCurrentLine = null;
            StringBuilder conteudo = new StringBuilder("");
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                conteudo.append(sCurrentLine);
                conteudo.append("\n");
                conteudo.append("\t");
                if(sCurrentLine.startsWith("<\\"));
                    conteudo.deleteCharAt(conteudo.length()-1);
            }

            return conteudo.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "???";
    }
}
