package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Service  {
    public final List<Action> ACTIONS = new ArrayList<Action>();

    public final Map<String, Action> STRING_ACTION_HASH_MAP = new HashMap<String, Action>();

    public final String name;
    public final String details;

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
}