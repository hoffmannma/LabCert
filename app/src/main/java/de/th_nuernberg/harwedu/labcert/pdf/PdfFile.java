package de.th_nuernberg.harwedu.labcert.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Group;
import de.th_nuernberg.harwedu.labcert.objects.Progress;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
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
    private Context context = null;


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
    public void createPdf(Context context, Student student, Group group) throws
            FileNotFoundException, DocumentException {

        this.context = context;

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
        writeToPdf(pdfFile, student,group);
        toastMsg(context, "PDF " + fileName + " für " + student.getFirstname() + " " +
                student.getSurname() + " erstellt");
    }

    //This is to create Header and Footer for the PDF
    class MyFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        Font finalquote_font = FontFactory.getFont(FontFactory.HELVETICA,10, Font.NORMAL, BaseColor.GRAY);
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("this is a header", ffont);
            Phrase footer1 = new Phrase("Diese Bescheinigung wurde maschinell erstell und ist ohne Unterschrift gültig. Zusätze und Änderungen bedürfen",finalquote_font);
            Phrase footer2 = new Phrase( "der ausdrücklichen Bestätigung durch den Praktikumsbeauftragten und sind nur in Verbindung mit Unterschrift und", finalquote_font);
            Phrase footer3 = new Phrase( "Stempel gültig.", finalquote_font);

            /*            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 10, 0);*/
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footer1,
                    document.leftMargin(),document.bottom()+20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footer2,
                    document.leftMargin(),document.bottom()+10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footer3,
                    document.leftMargin(),document.bottom(), 0);
        }
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
    private void writeToPdf(File pdfFile, Student student,Group group) throws
            FileNotFoundException, DocumentException {
        DataSource datasource = new DataSource(context);

        ArrayList<Requirement> RequirementInformation = datasource.getGroupRequirements(group.getLab_name(), group.getGroup(), group.getTerm());

        String subjectName = "<Fachname>";
        String studentName = student.getSurname() + ", "+ student.getFirstname();
        String studentMatr = student.getMatr();
        String studentSubject = "<Studienfach>";

//Defines  Group
        String profName = "<Prof. Cool>";
        String[] reqArray = new String[3];
        String[][] reqNumberArray = new String[3][2];


        reqArray[0] = "<Anwesenheit>";
        reqArray[1] = "<Kurztest>";
        reqArray[2] = "<Testat>";

        for (int i = 0; i < reqArray.length ; i++) {
            reqNumberArray[i][0] = "<5>";
            reqNumberArray[i][1] = "<i>";
        }
//Defines for each requirement


//Defines Semester
        String semesterName = "Sommersemester 2016";
        String semesterDates = "15.03.2016-30.09.2016";

//Layout
//Font
        Font headline_font = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
        Font secondline_font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font text_font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
//PDF opening call
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, output);
        MyFooter event = new MyFooter();
        writer.setPageEvent(event);
        document.open();
//Insert OHM Picture

/*        Drawable d = getResources().getDrawable(R.drawable.myImage);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        Image img = Image.getInstance(stream.toByteArray());
        document.add(img);*/

//General information
        document.add(new Paragraph("Praktikumsleistung " + subjectName, headline_font));
        document.add(new Paragraph("für das " + semesterName + "(" + semesterDates + ")", secondline_font));
        document.add(new Phrase("\n"));

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table.addCell(new Phrase("Nachname, Vorname:",text_font));
        table.addCell(new Phrase(studentName,text_font));
        table.addCell(new Phrase("Matrikelnr.:",text_font));
        table.addCell(new Phrase(studentMatr,text_font));
        table.addCell(new Phrase("Studienfach:",text_font));
        table.addCell(new Phrase(studentSubject,text_font));
        table.addCell(new Phrase("Beauftragter:",text_font));
        table.addCell(new Phrase(profName,text_font));

        document.add(table);
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));


//For loop for each "requirement"
//Write a block with the kind of "Requirement" and the date

        PdfPTable table_2 = new PdfPTable(2);
        table_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);

/*        for (int i = 0; i < reqArray.length; i++)
        {
            table_2.addCell(new Phrase(reqArray[i],secondline_font));
            table_2.addCell(new Phrase("Datum",secondline_font));

            //int n = Integer.parseInt(reqNumberArray[i][1]);
            for (int j = 0; j < 3; j++) {
                table_2.addCell(new Phrase(reqArray[i] + "_" + j,text_font));
                table_2.addCell(new Phrase("<Durchführdatum>",text_font));
            }
            table_2.addCell(" ");
            table_2.addCell(" ");

        }*/

        for (Requirement requirement:RequirementInformation){
            table_2.addCell(new Phrase(requirement.getType(),secondline_font));
            table_2.addCell(new Phrase("Testiert am",secondline_font));
            ArrayList<Progress> requirement_inserts =
                    datasource.getProgress(group.getLab_name(), group.getGroup(), group.getTerm(), requirement.getType(), student.getMatr());

            for(int i=0; i < requirement_inserts.size(); i++)
            {
                table_2.addCell(new Phrase(requirement.getType() + "_" + i,text_font));
                table_2.addCell(new Phrase(requirement_inserts.get(i).getTs(),text_font));
            }
            table_2.addCell(" ");
            table_2.addCell(" ");
        }

        document.add(table_2);
//For loop final Overview

        PdfPTable table_final = new PdfPTable(3);
        table_final.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table_final.addCell(new Phrase("Leistungsnachweis",secondline_font));
        table_final.addCell(new Phrase("Vorgabe",secondline_font));
        table_final.addCell(new Phrase("Erfüllt",secondline_font));

        StringBuilder sb = new StringBuilder();

        for (Requirement requirement:RequirementInformation){
            table_final.addCell(new Phrase (requirement.getType(),text_font));
            table_final.addCell(new Phrase (requirement.getCount(),text_font));
            table_final.addCell(new Phrase (sb.toString(datasource.getSumScore(group.getLab_name(), group.getGroup(), group.getTerm(), student.getMatr(),requirement.getType())),text_font));

        }
        table_final.addCell(" ");
        table_final.addCell(" ");
        table_final.addCell(" ");
        document.add(table_final);

        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        //table_final.resetColumnCount(1);
//Time and place quote

/*        document.add(new Paragraph("Vorlesungsdauer von " + semesterStart +
        " bis " + semesterEnd, text_font));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
*/
//final quote

        document.close();
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