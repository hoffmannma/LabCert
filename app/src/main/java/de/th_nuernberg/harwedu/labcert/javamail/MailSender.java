package de.th_nuernberg.harwedu.labcert.javamail;

import android.util.Log;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPSendFailedException;
//import com.sun.mail.util.MailSSLSocketFactory;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import de.th_nuernberg.harwedu.labcert.CONFIG;


/**
 * Created by bestj on 19.08.2016.
 */
public class MailSender extends javax.mail.Authenticator {
    private static final String LOG_TAG = MailSender.class.getSimpleName();

    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSender(String user_, String password_) {
        final String user = user_;
        final String password = password_;

        //Set the socket factory to trust all hosts
        /*MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }*/

        //create the properties for the session
        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.imaps.socketFactory.class", "com.myapp.DummySSLSocketFactory");
        //props.put("mail.smtp.ssl.socketFactory", sf);
        props.setProperty("mail.smtp.port", "26"); //web.de = 587; standard-ssl = 465

        //session mit der Authentication erstellen
        Log.d(LOG_TAG, "Create Authentication with server (handshake)");
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }

    public synchronized boolean sendMail(String subject, String body, String sender, String recipients)
    {
        boolean finished = true;
        try{
            Log.d(LOG_TAG, "Create Message");
            MimeMessage message = new MimeMessage(session);
            Log.d(LOG_TAG, "Create Handler");
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            Log.d(LOG_TAG, "Create Transport");
            Transport transport = session.getTransport("smtps");

            Log.d(LOG_TAG, "Insert the Sender address to message");
            message.setFrom(new InternetAddress(sender));
            Log.d(LOG_TAG, "Insert date");
            message.setSentDate(new Date());
            Log.d(LOG_TAG, "Insert Subject to Message");
            message.setSubject(subject);
            Log.d(LOG_TAG, "Data Handler setting");
            message.setDataHandler(handler);
            Log.d(LOG_TAG, "Mail address of recipients");
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

            Log.d(LOG_TAG, "creating transport connection");
            transport.connect(CONFIG.MAILHOST, CONFIG.USERNAME, CONFIG.PASSWORD);
            Log.d(LOG_TAG, "sending message");
            transport.sendMessage(message, message.getAllRecipients());
            Log.d(LOG_TAG, "close transport connection");
            transport.close();
            Log.d(LOG_TAG, "Message sent");
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            finished = false;
        }
        return finished;
    }
}