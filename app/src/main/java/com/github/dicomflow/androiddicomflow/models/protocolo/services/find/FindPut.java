package com.github.dicomflow.androiddicomflow.models.protocolo.services.find;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.sharing.Sharing;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Search;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class FindPut extends Find {

    @Element public final int priority;
    @ElementList(inline = true) public final List<Search> searches;
    @Element public final String timezone;

    public FindPut(int priority, List<Search> searches, String timezone) {
        super("PUT");
        this.priority = priority;
        this.searches = searches;
        this.timezone = timezone;
    }

}
