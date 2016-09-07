package de.th_nuernberg.harwedu.labcert.javamail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPSendFailedException;

import javax.mail.AuthenticationFailedException;

import de.th_nuernberg.harwedu.labcert.CONFIG;

/**
 * Created by bestj on 22.08.2016.
 */
public class MailSenderAsync extends AsyncTask<String, Integer, String> {
    private static final String LOG_TAG = MailSenderAsync.class.getSimpleName();

    private String subject;
    private String body;
    private String senderaddress;
    private String recipientsaddress;

    private Context context;
    ProgressDialog prgDialog;

    private boolean fail = false;

    //public TaskCompleted delegate = null;

    public MailSenderAsync(Context context, String subject, String body, String senderaddress, String recipientsaddress) {
        this.context = context;
        this.subject = subject;
        this.body = body;
        this.senderaddress = senderaddress;
        this.recipientsaddress = recipientsaddress;
    }

    @Override
    protected void onPreExecute() {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Mail wird gesendet. Bitte warten...");
        prgDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(LOG_TAG, "Authentificat with server");
        MailSender sender = new MailSender(CONFIG.getUSERNAME(), CONFIG.getPASSWORD());
        Log.d(LOG_TAG, "Sending Mail");
        fail = !(sender.sendMail(subject, body, senderaddress, recipientsaddress));
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        if (prgDialog.isShowing())
            prgDialog.dismiss();
        if (fail)
            Toast.makeText(context, "Mail senden fehlgeschlagen!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Mail erfolgreich gesendet", Toast.LENGTH_SHORT).show();
    }
}