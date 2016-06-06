package de.th_nuernberg.harwedu.labcert.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.th_nuernberg.harwedu.labcert.database.Student;

/**
 * Created by Edu on 05.06.2016.
 */

/**
 *  TODO
 * - Api 15+ Kompatibilität (aktuell 19+)
 * - Pfad anpassen
 * - internen Speicher verwenden, falls externer nicht verfügbar
 *
 */


public class PdfFile {

    private static final String LOG_TAG = "PdfFile";

    public PdfFile() {
    }

    public void sendPdf(Student student){

    }

    public void createPdf(Context context, Student student) throws
            FileNotFoundException, DocumentException {

        // Speicherort (Ordner) initialisieren
        File pdfFile = null;

        // Zeitstempel erstellen
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        // falls möglich: externen Speicher verwenden
        if (isExternalStorageWritable()) {
            pdfFile = new File(getExtStorageDir(context),
                    (student.getMatr() +"_"+ timeStamp + ".pdf"));
        }

        // pdf mit Daten füllen
        writeToPdf(pdfFile, student);
    }

    private void writeToPdf(File pdfFile, Student student) throws
            FileNotFoundException, DocumentException{

        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph(student.getSurname()));
        document.add(new Paragraph(student.getFirstname()));
        document.add(new Paragraph(student.getCommentStudent()));
        document.close();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getExtStorageDir(Context context) {
        // Privates Directory der App wählen
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "pdf");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        else Log.e(LOG_TAG, "Directory created!");
        return file;
    }

}
