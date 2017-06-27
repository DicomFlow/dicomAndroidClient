package com.github.dicomflow.androiddicomflow.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class SharingPut extends Sharing {

    @ElementList(name = "ulrs", inline = true) public final List<Url> urls;

    public SharingPut(@Attribute(name = "from") String from, List<Url> urls){
        super("PUT", from);
        this.urls = urls;
    }

}
