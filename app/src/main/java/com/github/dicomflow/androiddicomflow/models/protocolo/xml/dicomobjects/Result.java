package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 22/06/17.
 */

@Root
public class Result {
    public final IResult iResult;
    @Element public final Completed completed;
    @Element public final Data data;
    @Element public final String report;
    @ElementList(name = "objects" ) public final List<DicomObject> objects;
    @Element public final String originalMessageID;
    @ElementList(name = "services") public final List<ServiceDescriptor> serviceDescriptors;
    @Element public final String timestamp;
    @ElementList(name = "urls") public final List<Url> urls;

    public Result(IResult iResult,
                  Completed completed,
                  Data data,
                  String report,
                  List<DicomObject> objects,
                  List<ServiceDescriptor> serviceDescriptors,
                  String timestamp,
                  List<Url> urls) {
        this.iResult = iResult;
        this.completed = completed;
        this.data = data;
        this.report = report;
        this.objects = objects;
        this.originalMessageID = iResult.getOriginalMessageID();
        this.serviceDescriptors = serviceDescriptors;
        this.timestamp = timestamp;
        this.urls = urls;
    }

    public interface IResult {
        String getOriginalMessageID();
    }
}
