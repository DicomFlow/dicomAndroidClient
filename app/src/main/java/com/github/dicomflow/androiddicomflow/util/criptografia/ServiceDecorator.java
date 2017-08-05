package com.github.dicomflow.androiddicomflow.util.criptografia;

import com.github.dicomflow.dicomflowjavalib.services.Service;

abstract class ServiceDecorator extends Service {

    protected Service serviceDecorado;

	public ServiceDecorator(Service serviceDecorado) {
		super(serviceDecorado.toMap());
		this.serviceDecorado = serviceDecorado;
	}

}