package com.github.dicomflow.androiddicomflow.util.criptografia;

import com.github.dicomflow.dicomflowjavalib.services.Service;

import org.apache.commons.codec.binary.Base64;

import java.security.PrivateKey;
import java.util.Map;

public class DecoradorServicoAssinado extends ServiceDecorator {

	private final PrivateKey privateKey;

	public DecoradorServicoAssinado(Service serviceDecorado, PrivateKey privateKey) {
        super(serviceDecorado);
		this.privateKey = privateKey;
	}


	private void assinarServico(PrivateKey privateKey) {

//		// Criptografa a Mensagem usando a Chave Pública
//		FileInputStream inputStream = appContext.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
//		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//		final PublicKey chavePublica = (PublicKey) objectInputStream.readObject();

	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = serviceDecorado.toMap();

		final byte[] assinatura = EncriptaDecriptaRSA.assinar(messageID, privateKey);
		String assinaturaBase64 = android.util.Base64.encodeToString(assinatura, android.util.Base64.DEFAULT); //deBase64String(assinatura);
		map.put("signing", assinaturaBase64);
		return map;
	}
}