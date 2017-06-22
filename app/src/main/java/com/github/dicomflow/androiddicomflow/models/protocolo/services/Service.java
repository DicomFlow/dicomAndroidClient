package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Root(name = "service")
public class Service  {

    @Attribute
    public final String name;
    @Attribute
    public final String action;
    @Attribute
    public final String version;

    @Element
    public final String timeout;
    @Element
    public final String timestamp;
    @Element
    public final String messageID;

    public Service(
            @Attribute(name = "name") String name,
            @Attribute(name = "action") String action,
            @Attribute(name = "version") String version,
            @Element(name = "timeout") String timeout,
            @Element(name = "timestamp") String timestamp,
            @Element(name = "messageID")String messageID) {
        this.name = name;
        this.action = action;
        this.version = version;
        this.timeout = timeout;
        this.timestamp = timestamp;
        this.messageID = messageID;
    }

    public Service(String name, String action) {
        this.version = "1.0";
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDhh:mm:ssZ");
        Date date = new Date();

        this.name = name;
        this.action = action;
        this.timestamp = dateFormat.format(date);
        this.messageID = UUID.randomUUID().toString();
        this.timeout = String.valueOf(date.getTime());
    }

//    public Service(String name, String action) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDhh:mm:ssZ");
//        Date date = new Date();
//
//        this.name = name;
//        this.action = action;
//
//        timestamp = dateFormat.format(date);
//        messageID = UUID.randomUUID().toString();
//        timeout = String.valueOf(date.getTime());
//    }

}