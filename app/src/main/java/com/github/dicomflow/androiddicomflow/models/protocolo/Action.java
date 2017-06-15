package com.github.dicomflow.androiddicomflow.models.protocolo;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

public abstract class Action {
    public final String name;
    public final String description;
    public final String details;

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
}