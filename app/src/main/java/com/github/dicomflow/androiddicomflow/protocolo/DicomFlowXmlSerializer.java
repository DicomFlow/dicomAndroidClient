package com.github.dicomflow.androiddicomflow.protocolo;

import android.os.Environment;
import android.util.Log;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestResult;

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

    public static Service deserialize(String filepath) throws Exception {
        Serializer serializer = new Persister();
        File file = new File(filepath);
        return serializer.read(getXmlClass(filepath), file);
    }

    public static Class<? extends Service> getXmlClass(String filepath) throws IOException {

        FileReader reader = new FileReader(filepath);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String firstLine = bufferedReader.readLine().toLowerCase();

        if (firstLine.contains("certificate")) {

        } else if (firstLine.contains("storage")) {

        } else if (firstLine.contains("find")) {

        } else if (firstLine.contains("sharing")) {

        } else if (firstLine.contains("request")) {

            if (firstLine.contains("put")) {
                return RequestPut.class;
            } else if (firstLine.contains("result")) {
                return RequestResult.class;
            }

        } else if (firstLine.contains("discovery")) {

        }

        return null;
    }

    public static String serialize(Service service) throws Exception {
        Serializer serializer = new Persister();

        File root = new File(Environment.getExternalStorageDirectory(), "DicomFiles");
        root.mkdirs();

        File xmlFile = new File(root, String.format("%s_%s.xml", service.name.toLowerCase(), service.action));
        if (xmlFile.exists()) xmlFile.delete();

        Log.d("File", xmlFile.getAbsolutePath());
        xmlFile.createNewFile();

        FileWriter writer = new FileWriter(xmlFile);
        serializer.write(service, writer);
        writer.flush();
        writer.close();

        //TODO NAO REMOVER ISSO ATE O FINAL DO PROJETO PODE SER UTIL
        FileReader reader = new FileReader(xmlFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String sCurrentLine = null;
        StringBuilder conteudo = new StringBuilder("");
        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            conteudo.append(sCurrentLine);
            conteudo.append("\n");
            conteudo.append("\t");
            if (sCurrentLine.startsWith("<\\")) ;
            conteudo.deleteCharAt(conteudo.length() - 1);
        }
        reader.close();
        bufferedReader.close();

        return xmlFile.getAbsolutePath();
    }

    public static String serialize(Object o) {
        Serializer serializer = new Persister();

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "DicomFiles");
            root.mkdirs();

            File xmlFile = new File(root, String.format("%s.xml", o.getClass().getSimpleName()));
            if (xmlFile.exists ()) xmlFile.delete();

            Log.d("File", xmlFile.getAbsolutePath());
            xmlFile.createNewFile();

            FileWriter writer = new FileWriter(xmlFile);
            serializer.write(o, writer);
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

        return "?????";
    }


}
