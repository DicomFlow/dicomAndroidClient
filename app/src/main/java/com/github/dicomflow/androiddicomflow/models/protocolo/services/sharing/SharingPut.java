package com.github.dicomflow.androiddicomflow.models.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class SharingPut extends Sharing {

    @ElementList(name = "ulrs", inline = true) public final List<Url> urls;

    public SharingPut(List<Url> urls){
        this.urls = urls;
    }

    @Override
    public String getAction() {
        return "PUT";
    }

}
