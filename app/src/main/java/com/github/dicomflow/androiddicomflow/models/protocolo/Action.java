package com.github.dicomflow.androiddicomflow.models.protocolo;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public abstract class Action {
    @Element public String name;
    @Element public String description;
    @Element public String details;

    public final Service service;

    public Action(Service service, String name, String description, String details) {
        this.service = service;
        this.name = name;
        this.description = description;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }

//    <xs:complexType name="certificateRequest">
//      <xs:complexContent>
//        <xs:extension base="service">
//          <xs:sequence>
//              <xs:element name="domain" type="xs:string" minOccurs="0"/>
//              <xs:element name="mail" type="xs:string" minOccurs="0"/>
//              <xs:element name="port" type="xs:int" minOccurs="0"/>
//          </xs:sequence>
//        </xs:extension>
//      </xs:complexContent>
//    </xs:complexType>


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Service getService() {
        return service;
    }
}