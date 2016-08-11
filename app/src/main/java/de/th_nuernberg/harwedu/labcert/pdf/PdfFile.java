package de.th_nuernberg.harwedu.labcert.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

    /**
     * PDF File erstellen
     *
     * @param context
     * @param student
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void createPdf(Context context, Student student) throws
            FileNotFoundException, DocumentException {

        // Speicherort (Ordner) initialisieren
        File pdfFile = null;

        // Zeitstempel erstellen
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        String fileName = (student.getMatr() +"_"+ timeStamp + ".pdf");
        // falls möglich: externen Speicher verwenden
        if (isExternalStorageWritable()) {
            pdfFile = new File(getExtStorageDir(context), (fileName));
        }

        // pdf mit Daten füllen
        writeToPdf(pdfFile, student);
        toastMsg(context, "PDF " + fileName + " für " + student.getFirstname() + " " +
                student.getSurname() + " erstellt");
    }

    /**
     * PDF beschreiben
     *
     * @param pdfFile
     * @param student
     * @throws FileNotFoundException
     * @throws DocumentException
     *
     * TODO
     * Struktur / Inhalt ...
     *
     */
    private void writeToPdf(File pdfFile, Student student) throws
            FileNotFoundException, DocumentException{

        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph(student.getSurname()));
        document.add(new Paragraph(student.getFirstname()));
        document.add(new Paragraph(student.getCommentStudent()));
        document.add(new Paragraph("BEISPIEL"));
        document.close();
    }

    /**
     * Zugriff auf externen Speicher möglich?
     * @return true / false
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Externen Speicher reservieren, ggf. Ordner erstellen
     *
     * @param context
     * @return
     */
    private File getExtStorageDir(Context context) {
        // Privates Directory der App wählen
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "pdf");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        else Log.e(LOG_TAG, "Directory created!");
        return file;
    }

    /**
     * Toast: PDF mit Namen ... erstellt
     * @param context
     * @param msg
     */
    private void toastMsg(Context context, String msg)
    {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
