package com.github.dicomflow.androiddicomflow.util.criptografia;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameBuilder;
import org.spongycastle.asn1.x500.style.BCStyle;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.X509v1CertificateBuilder;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class EncriptaDecriptaRSA {

    public static final String ALGORITHM = "RSA";

    /**
     * Local da certificado no sistema de arquivos.
     */
    public static final String PATH_CERTIFICATE = "my_certificate.cer";
    /**
     * Local da chave privada no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PRIVADA = "my_private.key";

    /**
     * Local da chave pública no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PUBLICA = "my_public.key";
    private static final int VALIDITY_IN_DAYS = 365;


    /**
     * Create a random 1024 bit RSA key pair
     */
    private static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance(ALGORITHM, "SC");
        kpGen.initialize(1024, new SecureRandom());
        return kpGen.generateKeyPair();
    }

    //TODO remover isso aqui em breve
    /**
     * Gera a chave que contém um par de chave Privada e Pública usando 1025 bytes.
     * Armazena o conjunto de chaves nos arquivos private.key e public.key
     */
    public static void geraChave(Context context) {
        try {
            final KeyPair key = generateRSAKeyPair();
            saveKey(context, key.getPublic(), PATH_CHAVE_PUBLICA);
            saveKey(context, key.getPrivate(), PATH_CHAVE_PRIVADA);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static void saveKey(Context context, Key key, String path) throws IOException {
        File chavePublicaFile = new File(context.getFilesDir(), path);
        chavePublicaFile.createNewFile();
        ObjectOutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(chavePublicaFile));
        chavePublicaOS.writeObject(key);
        chavePublicaOS.close();
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

    //region auxiliares
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
    //endregion

    //region comentado
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



//    public void pbkey() {
//        // Read the CSR
//        FileReader fileReader = new FileReader("/path/to/your.csr");
//        PemReader pemReader = new PemReader(fileReader);
//
//        PKCS10CertificationRequest csr =
//                new PKCS10CertificationRequest(pemReader.readPemObject().getContent());
//
//        pemReader.close();
//        fileReader.close();
//
//        // Write the Public Key as a PEM-File
//        StringWriter output = new StringWriter();
//        PemWriter pemWriter = new PemWriter(output);
//
//        PemObject pkPemObject = new PemObject("PUBLIC KEY",
//                csr.getSubjectPublicKeyInfo().getEncoded());
//
//        pemWriter.writeObject(pkPemObject);
//        pemWriter.close();
//
//        System.out.println(output.getBuffer());
//
//        // Extract the Public Key as "RSAKeyParameters" so you can use for
//        // encryption/signing operations.
//        RSAKeyParameters pubkey =
//                (RSAKeyParameters) PublicKeyFactory.createKey(csr.getSubjectPublicKeyInfo());
//    }


//    public static void main(String[] args) throws Exception {
//        // Create the keys
//        KeyPair pair = Utils.generateRSAKeyPair();
//
//        // generate the certificate
//        X509Certificate cert = generateCertificate(null, null);
//
//        // show some basic validation
//        cert.checkValidity(new Date());
//        cert.verify(cert.getPublicKey());
//        System.out.println("valid certificate generated");
//    }
    //endregion

    //certificados

    /**
     * Esse metodo
     * @param user
     * @param context
     * @return
     * @throws NoSuchAlgorithmException
     * @throws OperatorCreationException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchProviderException
     */
    public static X509Certificate generateCertificate(FirebaseUser user, Context context) throws NoSuchAlgorithmException, OperatorCreationException, IOException, CertificateException, NoSuchProviderException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair keyPair = kpg.genKeyPair();

        Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + VALIDITY_IN_DAYS * 24 * 60 * 60 * 1000);

        X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        //cn nome e sobre nome
        //ou unidade organizacional
        //o empresa
        //l cidade
        //ST estado
        //C pais
        nameBuilder.addRDN(BCStyle.CN, user.getDisplayName());
        nameBuilder.addRDN(BCStyle.OU, user.getEmail());
        nameBuilder.addRDN(BCStyle.O, user.getEmail());
        nameBuilder.addRDN(BCStyle.L, "João Pessoa");
        nameBuilder.addRDN(BCStyle.ST, "PB");
        nameBuilder.addRDN(BCStyle.C, "BR");
        nameBuilder.addRDN(BCStyle.EmailAddress, user.getEmail());

        X500Name x500Name = nameBuilder.build();
        Random random = new Random();

        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        X509v1CertificateBuilder v1CertGen = new X509v1CertificateBuilder(x500Name
                , BigInteger.valueOf(random.nextLong())
                ,startDate
                ,endDate
                ,x500Name
                ,subjectPublicKeyInfo);

        // Prepare Signature:
        Security.addProvider(new BouncyCastleProvider());
        ContentSigner sigGen = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider("SC").build(keyPair.getPrivate());

        // Self sign :
        X509CertificateHolder x509CertificateHolder = v1CertGen.build(sigGen);

        // Retrieve the certificate from holder
        InputStream is1 = new ByteArrayInputStream(x509CertificateHolder.getEncoded());
        CertificateFactory cf = CertificateFactory.getInstance("X.509","SC");
        X509Certificate generatedCertificate = (X509Certificate) cf.generateCertificate(is1);

        //antes de terminar vou salvar a chave privada
        saveKey(context, keyPair.getPrivate(), PATH_CHAVE_PRIVADA);
        saveCertificate(generatedCertificate, context.getFilesDir());
        return generatedCertificate;
    }


    private static void saveCertificate(X509Certificate certificate, File contextgGetFilesDir) throws CertificateEncodingException, IOException {

        //exportando
        byte[] data = certificate.getEncoded();

        // Salva o certificado no arquivo
        File file = new File(contextgGetFilesDir, PATH_CERTIFICATE);
        file.createNewFile();
        ObjectOutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(file));
        chavePublicaOS.writeObject(data);
        chavePublicaOS.close();
    }

    /**
     * Esse metodo é repsonsavel por me retornar a chave publica de um certificado.
     * Esse certificado pode ser o meu proprio ou de um outro usuario por exemplo.
     * @param context
     * @param PATH_CERTIFICATE
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws CertificateException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static PublicKey getPublicKey(Context context, String PATH_CERTIFICATE) throws IOException, ClassNotFoundException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        // read certificate
        FileInputStream inputStream = context.openFileInput(PATH_CERTIFICATE);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // Retrieve the certificate from holder
        InputStream is1 = new ByteArrayInputStream((byte[]) objectInputStream.readObject());
        CertificateFactory cf = CertificateFactory.getInstance("X.509","SC");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(is1);

        // show some basic validation
        certificate.checkValidity(new Date());
        certificate.verify(certificate.getPublicKey());
        System.out.println("valid certificate generated");

        if (objectInputStream != null) objectInputStream.close();
        if (inputStream != null) inputStream.close();

        return certificate.getPublicKey();
    }


}