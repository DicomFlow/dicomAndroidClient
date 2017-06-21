package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Root
public abstract class Service  {
    public final List<Action> ACTIONS = new ArrayList<Action>();

    public final Map<String, Action> STRING_ACTION_HASH_MAP = new HashMap<String, Action>();

    @Element private String name;
    @Attribute private String details;

    public Service(String name, String details) {
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }

    protected void addAction(Action action) {
        ACTIONS.add(action);
        STRING_ACTION_HASH_MAP.put(action.name, action);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}