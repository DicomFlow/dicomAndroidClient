package com.github.dicomflow.androiddicomflow.mail;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.request.RequestPut;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Credentials;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Patient;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Serie;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Study;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

/**
 * Created by Neto on 24/06/2017.
 */

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients, Service service) throws Exception {
        try{
            MimeMessage message = new MimeMessage(session);
            Multipart multipart = buildServiceBodyPart(service);
            message.setContent(multipart);

            //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            //message.setDataHandler(handler);

            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);

            if (recipients.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            } else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            }
            Transport.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Multipart buildServiceBodyPart(Service service) throws Exception {
        Multipart multipart = new MimeMultipart();

        String str = DicomFlowXmlSerializer.serialize(service);

        MimeBodyPart attachment0 = new MimeBodyPart();
        attachment0.setContent(str.toString(),"text/xml; charset=UTF-8");

        multipart.addBodyPart(attachment0);
        return multipart;
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

}
