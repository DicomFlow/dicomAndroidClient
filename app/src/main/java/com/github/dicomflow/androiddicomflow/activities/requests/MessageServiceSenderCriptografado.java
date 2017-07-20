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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public class MessageServiceSenderCriptografado extends MessageServiceSenderDecorator{

    public MessageServiceSenderCriptografado(Context context, MessageServiceSender messageServiceSenderDecorado) {
        super(context, messageServiceSenderDecorado);
    }

    @Override
    public BackgroundMail send(String subject) throws Exception {
        BackgroundMail backgroundMail = messageServiceSenderDecorado.send(subject);

        //os anexos nesse momento sao somente os caminho dos arquivos
        List<String> attachementsOriginais = backgroundMail.getAttachments();
        ArrayList<String> attachementsCriptografados = new ArrayList<>();

        //preciso criar novos arquivos temporarios (cache)

        PublicKey publicKeyDoDestinatario = null;
        for (String filePath : attachementsOriginais) {

            //pegar os bytes do original
            File originalFile = new File(filePath);
            FileInputStream inputStream = context.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
            byte[] bytesOriginais = new byte[(int) originalFile.length()];
            inputStream.read(bytesOriginais);

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKeyDoDestinatario);
            byte[] bytesCriptografados = cipher.doFinal(bytesOriginais);


            File fileCriptografadoNoCache = new File(context.getCacheDir(), filePath);
            fileCriptografadoNoCache.createNewFile();
            ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
                    new FileOutputStream(fileCriptografadoNoCache));
            chavePublicaOS.writeObject(bytesCriptografados);
            chavePublicaOS.close();

            attachementsCriptografados.add(fileCriptografadoNoCache.getAbsolutePath());
        }

        //depois de criar eu limpo os arquivos originais
        backgroundMail.getAttachments().clear();

        //agora eu adiciono esses arquivos como anexos
        withAttachments(attachementsCriptografados);

        //preciso tb excluir os arquivos apos o envio
        //TODO excluir os arquivos apos o cache

        return backgroundMail;
    }

}
