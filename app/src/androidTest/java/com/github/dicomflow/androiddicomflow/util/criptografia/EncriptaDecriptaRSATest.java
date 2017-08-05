package com.github.dicomflow.androiddicomflow.util.criptografia;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * Created by ricardobarbosa on 18/07/17.
 */
public class EncriptaDecriptaRSATest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.deleteFile(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
        appContext.deleteFile(EncriptaDecriptaRSA.PATH_CHAVE_PRIVADA);
    }

    @Test
    public void geraChave() throws Exception {
//        assertFalse(EncriptaDecriptaRSA.verificaSeExisteChavesNoSO());
        Context appContext = InstrumentationRegistry.getTargetContext();
        EncriptaDecriptaRSA.geraChave(appContext);
//        assertTrue(EncriptaDecriptaRSA.verificaSeExisteChavesNoSO());

        String msgOriginal = "1c6e4925-e071-4de0-b14b-9ee40b64ecf6";

        // Criptografa a Mensagem usando a Chave Pública
        FileInputStream inputStream = appContext.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        final PublicKey chavePublica = (PublicKey) objectInputStream.readObject();
        final byte[] textoCriptografado = EncriptaDecriptaRSA.criptografa(msgOriginal, chavePublica);


        // Decriptografa a Mensagem usando a Chave Pirvada
        FileInputStream inputStreamPrivada = appContext.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PRIVADA);
        ObjectInputStream objectInputStreamPrivada = new ObjectInputStream(inputStreamPrivada);
        final PrivateKey chavePrivada = (PrivateKey) objectInputStreamPrivada.readObject();
        final String textoPuro = EncriptaDecriptaRSA.decriptografa(textoCriptografado, chavePrivada);

        assertEquals("os valores nao sao iguais", msgOriginal, textoPuro);
    }


    @Test
    public void garantindoAssinatura() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        EncriptaDecriptaRSA.geraChave(appContext);

        String msgOriginal = "1c6e4925-e071-4de0-b14b-9ee40b64ecf6";

        // assino com a chave privada
        FileInputStream inputStreamPrivada = appContext.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PRIVADA);
        ObjectInputStream objectInputStreamPrivada = new ObjectInputStream(inputStreamPrivada);
        final PrivateKey chavePrivada = (PrivateKey) objectInputStreamPrivada.readObject();
        final byte[] assinatura = EncriptaDecriptaRSA.assinar(msgOriginal, chavePrivada);

        // ganranto assinatura com a chave publica
        FileInputStream inputStream = appContext.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        final PublicKey chavePublica = (PublicKey) objectInputStream.readObject();
        final boolean garantiu = EncriptaDecriptaRSA.garantirAssinatura(assinatura, msgOriginal, chavePublica);

        assertTrue("os valores nao sao iguais", garantiu);
    }

}