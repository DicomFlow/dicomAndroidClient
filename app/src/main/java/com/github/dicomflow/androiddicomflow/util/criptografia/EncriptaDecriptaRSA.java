package com.github.dicomflow.androiddicomflow.util.criptografia;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class EncriptaDecriptaRSA {

    public static final String ALGORITHM = "RSA";

    /**
     * Local da chave privada no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PRIVADA = "my_private.key";

    /**
     * Local da chave pública no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PUBLICA = "my_public.key";

    /**
     * Gera a chave que contém um par de chave Privada e Pública usando 1025 bytes.
     * Armazena o conjunto de chaves nos arquivos private.key e public.key
     */
    public static void geraChave(Context context) {
        try {
//            KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
//            keyGen2.init(128);
//            SecretKey secretKey = keyGen2.generateKey();
//            secretKey.

            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File chavePrivadaFile = new File(context.getFilesDir(), PATH_CHAVE_PRIVADA);
            File chavePublicaFile = new File(context.getFilesDir(), PATH_CHAVE_PUBLICA);

            chavePrivadaFile.createNewFile();
            chavePublicaFile.createNewFile();

            // Salva a Chave Pública no arquivo
            ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
                    new FileOutputStream(chavePublicaFile));
            chavePublicaOS.writeObject(key.getPublic());
            chavePublicaOS.close();
            // Salva a Chave Privada no arquivo
            ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
                    new FileOutputStream(chavePrivadaFile));
            chavePrivadaOS.writeObject(key.getPrivate());
            chavePrivadaOS.close();

            /*FileOutputStream chavePublicaOS = context.openFileOutput(PATH_CHAVE_PUBLICA, Context.MODE_PRIVATE);
            chavePublicaOS.write(key.getPublic().getEncoded());
            chavePublicaOS.close();
            FileOutputStream chavePrivadaOS = context.openFileOutput(PATH_CHAVE_PRIVADA, Context.MODE_PRIVATE);
            chavePrivadaOS.write(key.getPrivate().getEncoded());
            chavePrivadaOS.close();*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifica se o par de chaves Pública e Privada já foram geradas.
     */
    public static boolean verificaSeExisteChavesNoSO(Context context) {

        File chavePrivada = new File(context.getFilesDir(), PATH_CHAVE_PRIVADA);
        File chavePublica = new File(context.getFilesDir(), PATH_CHAVE_PUBLICA);

        if (chavePrivada.exists() && chavePublica.exists()) {
            return true;
        }

        return false;
    }

    /**
     * Criptografa o texto puro usando chave pública.
     */
    public static byte[] criptografa(String texto, PublicKey chave) {
        byte[] cipherText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Criptografa o texto puro usando a chave Púlica
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            cipherText = cipher.doFinal(texto.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    private static String gerarHashMD5(String messageID) {

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(messageID.getBytes(),0,messageID.length());
            String hash = new BigInteger(1,m.digest()).toString(16);
            System.out.println("MD5: "+ hash);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getMyPrivateKey(Context context) throws IOException, ClassNotFoundException {
        PrivateKey privateKey = null;
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            inputStream = context.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PRIVADA);
            objectInputStream = new ObjectInputStream(inputStream);
            privateKey = (PrivateKey) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) objectInputStream.close();
            if (inputStream != null) inputStream.close();
        }
        return privateKey;
    }

    public static PublicKey getMyPublicKey(Context context) throws IOException, ClassNotFoundException {
        PublicKey publicKey = null;
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            inputStream = context.openFileInput(EncriptaDecriptaRSA.PATH_CHAVE_PUBLICA);
            objectInputStream = new ObjectInputStream(inputStream);
            publicKey = (PublicKey) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) objectInputStream.close();
            if (inputStream != null) inputStream.close();
        }
        return publicKey;
    }


    /**
     * Decriptografa o texto puro usando chave privada.
     */
    public static String decriptografa(byte[] texto, PrivateKey chave) {
        byte[] dectyptedText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, chave);
            dectyptedText = cipher.doFinal(texto);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }

    public static byte[] assinar(String messageID, PrivateKey privateKey) {
        byte[] cipherText = null;

        String hash = gerarHashMD5(messageID);

        try {
            // Criptografa o texto puro usando a chave Púlica
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            cipherText = cipher.doFinal(hash.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    public static boolean garantirAssinatura(byte[] texto, String messageID, PublicKey publicKey) {
        //descritografo primeiro
        byte[] dectyptedText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            dectyptedText = cipher.doFinal(texto);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String hash = gerarHashMD5(messageID);

        return new String(dectyptedText).equals(hash);
    }

//    /**
//     * Testa o Algoritmo
//     */
//    public static void main(String[] args) {
//
//        try {
//
//            // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
//            if (!verificaSeExisteChavesNoSO()) {
//                // Método responsável por gerar um par de chaves usando o algoritmo RSA e
//                // armazena as chaves nos seus respectivos arquivos.
//                geraChave();
//            }
//
//            final String msgOriginal = "Exemplo de mensagem";
//            ObjectInputStream inputStream = null;
//
//            // Criptografa a Mensagem usando a Chave Pública
//            inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA));
//            final PublicKey chavePublica = (PublicKey) inputStream.readObject();
//            final byte[] textoCriptografado = criptografa(msgOriginal, chavePublica);
//
//            // Decriptografa a Mensagem usando a Chave Pirvada
//            inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA));
//            final PrivateKey chavePrivada = (PrivateKey) inputStream.readObject();
//            final String textoPuro = decriptografa(textoCriptografado, chavePrivada);
//
//            // Imprime o texto original, o texto criptografado e
//            // o texto descriptografado.
//            System.out.println("Mensagem Original: " + msgOriginal);
//            System.out.println("Mensagem Criptografada: " +textoCriptografado.toString());
//            System.out.println("Mensagem Decriptografada: " + textoPuro);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Create a self-signed X.509 Certificate
//     * @param dn the X.509 Distinguished Name, eg "CN=Test, L=London, C=GB"
//     * @param pair the KeyPair
//     * @param days how many days from now the Certificate is valid for
//     * @param algorithm the signing algorithm, eg "SHA1withRSA"
//     */
//    X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
//            throws GeneralSecurityException, IOException
//    {
//        PrivateKey privkey = pair.getPrivate();
//        X509CertInfo info = new X509CertInfo();
//        Date from = new Date();
//        Date to = new Date(from.getTime() + days * 86400000l);
//        CertificateValidity interval = new CertificateValidity(from, to);
//        BigInteger sn = new BigInteger(64, new SecureRandom());
//        X500Name owner = new X500Name(dn);
//
//        info.set(X509CertInfo.VALIDITY, interval);
//        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
//        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
//        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
//        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
//        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
//        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
//        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));
//
//        // Sign the cert to identify the algorithm that's used.
//        X509CertImpl cert = new X509CertImpl(info);
//        cert.sign(privkey, algorithm);
//
//        // Update the algorith, and resign.
//        algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
//        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
//        cert = new X509CertImpl(info);
//        cert.sign(privkey, algorithm);
//        return cert;
//    }
}