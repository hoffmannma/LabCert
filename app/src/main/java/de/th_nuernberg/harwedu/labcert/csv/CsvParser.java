package de.th_nuernberg.harwedu.labcert.csv;

/**
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.th_nuernberg.harwedu.labcert.CONFIG;
import de.th_nuernberg.harwedu.labcert.database.DataSource;

public class CsvParser {
    private static final String LOG_TAG = CsvParser.class.getSimpleName();
    Context context;
    String file_name;
    ArrayList<HashMap<String, String>> CSVData;

    public CsvParser(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;
    }

    public ArrayList<HashMap<String, String>> ReadCSV() throws IOException {

        InputStream instream = context.getAssets().open(file_name);
        InputStreamReader instreamreader = new InputStreamReader(instream);
        BufferedReader br = new BufferedReader(instreamreader);
        String line;
        String csvSplit = ",";

        //br.readLine();
        CSVData = new ArrayList<>();

        while ((line = br.readLine()) != null) {

            String[] row = line.split(csvSplit);
            HashMap<String, String> hm = new HashMap<>();

            for (int i = 0; i < row.length; i++) {
                hm.put("list_row_student[" + i + "]", row[i]);
            }
            CSVData.add(hm);
        }
        return CSVData;
    }

    public void XLSXImport() {
        boolean fail = false;
        Log.d(LOG_TAG, "XLSX-Datei wird abgerufen");
        InputStream stream = null;
        try {
            stream = context.getAssets().open(file_name);
        } catch (IOException e) {
            e.printStackTrace();
            fail = true;
        }
        if (stream != null) {
            try {
                DataSource datasource = new DataSource(context);
                datasource.openW();

                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                //Anzahl der Datenblätter von importierter Datei ermitteln
                int sheets = workbook.getNumberOfSheets();
                XSSFSheet sheet = null;
                Log.d(LOG_TAG, "XLSX-Datei wird bearbeitet (" + sheets + " sheets)");
                //Die einzelnen Sheets durchgehen
                for (int s = 0; s < sheets; s++) {
                    sheet = workbook.getSheetAt(s);
                    int rowCount = sheet.getPhysicalNumberOfRows();
                    int totalCols = 0;
                    int groups = 0;
                    Log.d(LOG_TAG, "Blatttitel: " + sheet.getRow(0).getCell(0).getStringCellValue());
                    String lab_id = sheet.getRow(0).getCell(0).getStringCellValue();
                    //Die einzelnen Spalten durchgehen
                    for (int r = 0; r < rowCount; r++) {
                        //Log.d("XLS-IMPORT", "bearbeite Zeile: " + r);
                        Row row = sheet.getRow(r);
                        String supervisor = "";
                        int cellsCount = row.getPhysicalNumberOfCells();
                        //max-Anzahl an benutzten Spalten ermitteln
                        if (totalCols < cellsCount) {
                            totalCols = cellsCount;
                        }
                        //Log.d("XLS-IMPORT", "" + sheet.getRow(r+1).getCell(1).getCellType()); //0 = Integer; 1=String; 3=Leer
                        //Gruppentrigger
                        if (row.getCell(1).getCellType() == 1 && row.getCell(1).getStringCellValue().equals("Num")) {
                            //String supervisor_ = sheet.getRow(r-3).getCell(1).getStringCellValue();
                            //supervisor = supervisor_.substring(supervisor.indexOf(": ") + 2, supervisor.indexOf(")") - 1);
                            supervisor = sheet.getRow(r-3).getCell(1).getStringCellValue();

                            //erstelle gruppe in Datenbank
                            datasource.createGroup(lab_id, String.valueOf(groups), CONFIG.TERM, supervisor);

                            Log.d(LOG_TAG, "Gruppe " + ++groups + ":");
                        }
                        //einzelnen Studenten erfassen
                        if (row.getCell(1).getCellType() == 0 && row.getCell(2).getCellType() == 1) {
                            //Log.d("XLS-IMPORT", "Student-Data");
                            String salut = row.getCell(2).getStringCellValue().trim();
                            String surname = row.getCell(3).getStringCellValue().trim();
                            String firstname = row.getCell(4).getStringCellValue().trim();
                            int matr = (int)row.getCell(5).getNumericCellValue();
                            String email = row.getCell(6).getStringCellValue().trim();
                            Log.d(LOG_TAG, salut + " " + surname + " " + firstname + " (" + matr + "|" + email + ")");
                            //Studentendaten in lokale Datenbank schreiben
                            datasource.createStudent(lab_id, String.valueOf(groups), CONFIG.TERM, salut, surname, firstname, String.valueOf(matr), email, "");
                        }
                    }
                    datasource.close();
                    Log.d(LOG_TAG, "Blattname: " + sheet.getSheetName() + "; Columns:" + totalCols + "; Rows:" + sheet.getLastRowNum());
                    Log.d(LOG_TAG, "------------------------------------------------");
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, "XLSX-Datei konnte nicht geöffnet werden. (try)");
                Log.d(LOG_TAG, e.getMessage());
                e.printStackTrace();
                fail = true;
            }
        } else {
            Log.d(LOG_TAG, "XLSX-Datei konnte nicht geöffnet werden. (if)");
        }
    }
}