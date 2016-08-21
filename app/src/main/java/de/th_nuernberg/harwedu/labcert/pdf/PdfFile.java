package de.th_nuernberg.harwedu.labcert.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Created by Edu on 05.06.2016.
 */

/**
 * TODO
 * - Api 15+ Kompatibilität (aktuell 19+)
 * - Pfad anpassen
 * - internen Speicher verwenden, falls externer nicht verfügbar
 */


public class PdfFile {

    private static final String LOG_TAG = "PdfFile";
    private static final String DIRECTORY_OLD_API =
            "/Android/data/de.th_nuernberg.harwedu.labcert/files/";

    public PdfFile() {
    }

    public void sendPdf(Student student) {

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

        String fileName = (student.getMatr() + "_" + timeStamp + ".pdf");
        // falls möglich: externen Speicher verwenden

        /*
        if (isExternalStorageWritable()) {
            pdfFile = new File(getExtStorageDir(context), (fileName));
        }
        */
        File pdfFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        boolean isPresent = true;
        if (!pdfFolder.exists()) {
            isPresent = pdfFolder.mkdir();
        }
        if (isPresent) {
            pdfFile = new File(pdfFolder.getAbsolutePath(), fileName);
        } else {
            // Failure
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
     * @throws DocumentException     TODO
     *                               Struktur / Inhalt ...
     */
    private void writeToPdf(File pdfFile, Student student) throws
            FileNotFoundException, DocumentException {
        //Defines  Student
        String subjectName = "<Fachname>";
        String studentName = "<Nachname, Vorname>";
        String studentMatr = "<Matrikelnummer>";
        String studentSubject = "<Studienfach>";

//Defines  Group
        String profName = "<Prof. Cool>";
        String[] reqArray = new String[3];
        String[][] reqNumberArray = new String[3][2];

        reqArray[0] = "<Anwesenheit>";
        reqArray[1] = "<Kurztest>";
        reqArray[2] = "<Testat>";

        for (int i = 0; i < reqArray.length - 1; i++) {
            reqNumberArray[i][0] = "5";
            reqNumberArray[i][1] = "i";
        }
//Defines Semester
        String semesterName = "Sommersemester 2016";
        String semesterDates = "15.03.2016-30.09.2016";
        String semesterStart = "15.03.2016"; //Vorlesungsbegin und Ende
        String semesterEnd = "08.07.2016";
//Layout
//Font
        Font headline_font = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC);
        Font secondline_font = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC);
        Font text_font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        Font finalquote_font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL); //Grau *
//PDF opening call
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        PdfWriter.getInstance(document, output);
        document.open();

//General information
        document.add(new Paragraph("Praktikumsleistung " + subjectName, headline_font));
        document.add(new Paragraph("für das " + semesterName + "(" + semesterDates + ")", secondline_font));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table.addCell("Nachname, Vorname:");
        table.addCell(studentName);
        table.addCell("Matrikelnr.:");
        table.addCell(studentMatr);
        table.addCell("Studienfach:");
        table.addCell(studentSubject);
        table.addCell("Beauftragter:");
        table.addCell(profName);

        document.add(table);
        document.add(new Paragraph(" ", text_font));
        document.add(new Paragraph(" ", text_font));
        document.add(new Paragraph(" ", text_font));

        //table.resetColumnCount(1);      //muss noch geklärt werden geht nicht

//For loop for each "requirement"
//Write a block with the kind of "Requirement" and the date

        PdfPTable table_2 = new PdfPTable(2);
        table_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        for (int i = 0; i < reqArray.length; i++) {
            table_2.addCell(reqArray[i]);
            table_2.addCell("Datum");

            //int n = Integer.parseInt(reqNumberArray[i][1]);
            for (int j = 0; j < 3; j++) {
                table_2.addCell(reqArray[i] + "_" + j);
                table_2.addCell("Durchfuehrdatum");
            }
            document.add(table_2);
            document.add(new Paragraph("", text_font));
            document.add(new Paragraph("", text_font));
            document.add(new Paragraph("", text_font));

            //table.resetColumnCount(1);


        }
//For loop final Overview

        PdfPTable table_final = new PdfPTable(3);
        table_final.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table_final.addCell("Leistungsnachweis");
        table_final.addCell("Vorgabe");
        table_final.addCell("Erfüllt");

        for (int i = 0; i < reqArray.length; i++) {
            table_final.addCell(reqArray[i]);
            table_final.addCell(reqNumberArray[i][0]);
            table_final.addCell(reqNumberArray[i][1]);

        }
        document.add(table_final);
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        table_final.resetColumnCount(1);
//Time and place quote

/*        document.add(new Paragraph("Vorlesungsdauer von " + semesterStart +
        " bis " + semesterEnd, text_font));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
*/

//final quote
        document.add(new Paragraph("Diese Bescheinigung wurde maschinell erstell und ist ohne" +
                "Unterschrift gültig. Zusätze und Änderungen bedürfen der ausdrücklichen " +
                "Bestätigung durch den Praktikumsbeauftragten und sind nur in Verbindung mit " +
                "Unterschrift und Stempel gültig.", finalquote_font));


        document.close();


        /*

        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph(student.getSurname()));
        document.add(new Paragraph(student.getFirstname()));
        document.add(new Paragraph(student.getCommentStudent()));
        document.add(new Paragraph("BEISPIEL"));
        document.close();
        */
    }


    /**
     * Zugriff auf externen Speicher möglich?
     *
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
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            file = new File(String.valueOf(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));
        } else file = new File(context.getFilesDir(), "pdf");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        } else Log.e(LOG_TAG, "Directory created!");
        return file;
    }

    /**
     * Toast: PDF mit Namen ... erstellt
     *
     * @param context
     * @param msg
     */
    private void toastMsg(Context context, String msg) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
