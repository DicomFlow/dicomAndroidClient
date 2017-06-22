package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Root(name = "service")
public abstract class Service  {

    @Attribute
    public final String name;
    @Attribute
    public final String action;
    @Attribute
    public final String version = "1.0";

    @Element
    public final String timeout;
    @Element
    public final String timestamp;
    @Element
    public final String messageID;

    public Service() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDhh:mm:ssZ");
        Date date = new Date();

        name = getName();
        action = getAction();
        timestamp = dateFormat.format(date);
        messageID = UUID.randomUUID().toString();
        timeout = String.valueOf(date.getTime());
    }

    public abstract String getName();

    public abstract String getAction();

}