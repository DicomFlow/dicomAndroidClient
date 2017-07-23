package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.util.criptografia.EncriptaDecriptaRSA;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public class MessageServiceSenderCriptografado extends MessageServiceSenderDecorator{

    public MessageServiceSenderCriptografado(Context context, MessageServiceSender messageServiceSenderDecorado) {
        super(context, messageServiceSenderDecorado);
    }

//    @Override
//    public MessageServiceSender withAttachments(String... attachments) throws Exception {
//
//
//        ArrayList<String> attachementsCriptografados = new ArrayList<>();
//
//        //preciso criar novos arquivos temporarios (cache)
//
//        //// TODO: 20/07/17 IMPORTANTE trocar pela chave publica do destinatario
//        PublicKey publicKeyDoDestinatario = EncriptaDecriptaRSA.getMyPublicKey(context);
//        for (String filePath : attachments) {
//
//            //pegar os bytes do original
//            File originalFile = new File(filePath);
//            FileInputStream inputStream = context.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
//            byte[] bytesOriginais = new byte[(int) originalFile.length()];
//            inputStream.read(bytesOriginais);
//
//            // 1. Generate a session key
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(128);
//            SecretKey sessionKey = keyGen.generateKey();
//
//            // 2. Encrypt the session key with the RSA public key
//            Cipher rsaCipher = Cipher.getInstance("RSA");
//            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKeyDoDestinatario);
//            byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());
//
//            // 3. Encrypt the data using the session key (unencrypted)
//            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            aesCipher.init(Cipher.ENCRYPT_MODE, sessionKey); //<-- sessionKey is the unencrypted session key.
//
//            // 4. Save the encrypted data along with the encryptedsession key (encryptedSessionKey).
//            // PLEASE NOTE THAT BECAUSE OF THE ENCRYPTION MODE (CBC),
//            // YOU ALSO NEED TO ALSO SAVE THE IV (INITIALIZATION VECTOR).
//            // aesCipher.aesCipher.getParameters().
//            //     getParametersSpec(IvParameters.class).getIV();
//            byte[] parameters = aesCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
//            byte[] bytesCriptografados = aesCipher.doFinal(bytesOriginais);
//
//            /*
//            final Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, publicKeyDoDestinatario);
//            byte[] bytesCriptografados = cipher.doFinal(bytesOriginais);
//            */
//
//            File fileCriptografadoNoCache = new File(context.getCacheDir(), filePath);
//            if (fileCriptografadoNoCache.getParentFile() != null) {
//                fileCriptografadoNoCache.getParentFile().mkdirs();
//            }
//            fileCriptografadoNoCache.createNewFile();
//            ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
//                    new FileOutputStream(fileCriptografadoNoCache));
//            chavePublicaOS.writeObject(bytesCriptografados);
//            chavePublicaOS.close();
//
//            attachementsCriptografados.add(fileCriptografadoNoCache.getAbsolutePath());
//        }
//
//        //depois de criar eu limpo os arquivos originais
////        attachments.clear();
//
//        //agora eu adiciono esses arquivos como anexos
//        MessageServiceSender messageServiceSender = null;
//        for(String s: attachementsCriptografados) {
//            messageServiceSender = messageServiceSenderDecorado.withAttachments(s);
//        }
//
//        //preciso tb excluir os arquivos apos o envio
//        //TODO excluir os arquivos apos o cache
//
//        return messageServiceSender;
//    }

    @Override
    public BackgroundMail send(String subject) throws Exception {

        //os anexos nesse momento sao somente os caminho dos arquivos
        List<String> attachementsOriginais = messageServiceSenderDecorado.attachments;
        ArrayList<String> attachementsCriptografados = new ArrayList<>();

        //preciso criar novos arquivos temporarios (cache)

        //// TODO: 20/07/17 IMPORTANTE trocar pela chave publica do destinatario
        PublicKey publicKeyDoDestinatario = EncriptaDecriptaRSA.getMyPublicKey(context);
        for (String filePath : attachementsOriginais) {

            //pegar os bytes do original
            File originalFile = new File(filePath);
            FileInputStream inputStream = context.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
            byte[] bytesOriginais = new byte[(int) originalFile.length()];
            inputStream.read(bytesOriginais);

            // 1. Generate a session key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey sessionKey = keyGen.generateKey();

            // 2. Encrypt the session key with the RSA public key
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKeyDoDestinatario);
            byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());

            // 3. Encrypt the data using the session key (unencrypted)
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, sessionKey); //<-- sessionKey is the unencrypted session key.

            // 4. Save the encrypted data along with the encryptedsession key (encryptedSessionKey).
            // PLEASE NOTE THAT BECAUSE OF THE ENCRYPTION MODE (CBC),
            // YOU ALSO NEED TO ALSO SAVE THE IV (INITIALIZATION VECTOR).
            // aesCipher.aesCipher.getParameters().
            //     getParametersSpec(IvParameters.class).getIV();
            byte[] parameters = aesCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            byte[] bytesCriptografados = aesCipher.doFinal(bytesOriginais);

            /*
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKeyDoDestinatario);
            byte[] bytesCriptografados = cipher.doFinal(bytesOriginais);
            */

            File fileCriptografadoNoCache = new File(context.getCacheDir(), filePath);
            if (fileCriptografadoNoCache.getParentFile() != null) {
                fileCriptografadoNoCache.getParentFile().mkdirs();
            }
            fileCriptografadoNoCache.createNewFile();
            ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
                    new FileOutputStream(fileCriptografadoNoCache));
            chavePublicaOS.writeObject(bytesCriptografados);
            chavePublicaOS.close();

            attachementsCriptografados.add(fileCriptografadoNoCache.getAbsolutePath());
        }

        //depois de criar eu limpo os arquivos originais
        messageServiceSenderDecorado.attachments.clear();

        //agora eu adiciono esses arquivos como anexos
        messageServiceSenderDecorado.attachments.addAll(attachementsCriptografados);

        //agora eu posso enviar o resul
        BackgroundMail backgroundMail = messageServiceSenderDecorado.send(subject);

        //preciso tb excluir os arquivos apos o envio
        //TODO excluir os arquivos apos o cache

        return backgroundMail;
    }

}
