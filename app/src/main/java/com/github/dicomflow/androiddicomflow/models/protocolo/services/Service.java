package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import android.support.annotation.NonNull;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Root(name = "service")
public abstract class Service  {
    /**
     * atributos
     */
    private String name = "";
    private String action;
    @Attribute public String version = "1.0";


    /**
     * elementos obrigatorios
     */
    @Element private String timeout;
    @Element private String timestamp;
    @Element private String messageID;

    public Service() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDhh:mm:ssZ");
        Date date = new Date();
        timestamp = dateFormat.format(date);
        messageID = UUID.randomUUID().toString();
        timeout = String.valueOf(date.getTime());
    }

    @Attribute public abstract String getName();
    @Attribute public abstract String getAction();

    /**
     * getters
     */


    public String getTimeout() {
        return timeout;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessageID() {
        return messageID;
    }

    /**
     * setters
     */
    @Attribute public void setName(String name) {
        this.name = name;
    }
    @Attribute public void setAction(String action) {
        this.action = action;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

}