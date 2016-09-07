package de.th_nuernberg.harwedu.labcert.papiTxt;

import android.os.Environment;

import com.itextpdf.text.DocumentException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.th_nuernberg.harwedu.labcert.objects.Group;
import de.th_nuernberg.harwedu.labcert.objects.Student;


public class TxtFile {


    /**
     * Created by Marius on 06.09.2016.
     * <p/>
     * <p/>
     * todo:
     * include txt function with button
     * control group functions with new objects
     * directory ? documents is ok or not
     * combine with email function automatic send to a mail ...
     */


    public void createTxt(Group group) throws
            FileNotFoundException, DocumentException {
        // Speicherort (Ordner) initialisieren
        File txtFile = null;
        // Zeitstempel erstellen
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//path should be considered todo
        String fileName = (group.getGroup() + "_" + timeStamp + ".txt");


        File txtFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        boolean isPresent = true;
        if (!txtFolder.exists()) {
            isPresent = txtFolder.mkdir();
        }
        if (isPresent) {
            txtFile = new File(txtFolder.getAbsolutePath(), fileName);
        } else {
            // Failure
        }


        for (Student student : group.getStudent()) {
            writeTxt(txtFile, student);
        }

    }

    public void writeTxt(File fileName, Student student) throws
            FileNotFoundException, DocumentException {
        String FinalResult = null;
        if (student.getProgress() == 100) {
            FinalResult = "m.E.";
        } else {
            FinalResult = "o.E.";
        }

        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(fileName, true));
            // TODO Fachnummer statt Laborname
            String finalString = student.getMatr() + "*" + student.getLabName() + "*" + FinalResult;
            buf.append(finalString);

            buf.newLine(); // test if functionable
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}