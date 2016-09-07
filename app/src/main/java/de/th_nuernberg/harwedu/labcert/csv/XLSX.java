package de.th_nuernberg.harwedu.labcert.csv;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

import de.th_nuernberg.harwedu.labcert.CONFIG;
import de.th_nuernberg.harwedu.labcert.database.DataSource;

public class XLSX extends AsyncTask<String, Integer, String> {
    private static final String LOG_TAG = XLSX.class.getSimpleName();
    private Context context;
    private String file_name;
    ProgressDialog prgDialog;
    private boolean fail = false;

    public XLSX(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;
    }

    @Override
    protected void onPreExecute() {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Starte XLSX-Import...");
        prgDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(LOG_TAG, "XLSX-Datei wird abgerufen");
        InputStream stream = null;
        try {
            stream = context.getAssets().open(file_name);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "XLSX-Datei konnte nicht geöffnet werden. (if)");
            fail = true;
        }

        if (stream != null) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                //Anzahl der Datenblätter von importierter Datei ermitteln
                int sheets = workbook.getNumberOfSheets();
                XSSFSheet sheet = null;
                Log.d(LOG_TAG, "XLSX-Datei wird bearbeitet (" + sheets + " sheets)");
                //Die einzelnen Sheets durchgehen
                try {
                    for (int s = 0; s < sheets; s++) {
                        sheet = workbook.getSheetAt(s);
                        int rowCount = sheet.getPhysicalNumberOfRows();
                        int totalCols = 0;
                        int groups = 0;
                        Log.d(LOG_TAG, "Blatttitel: " + sheet.getRow(0).getCell(0).getStringCellValue());
                        String lab_id = sheet.getRow(0).getCell(0).getStringCellValue();
                        //Die einzelnen Spalten durchgehen
                        try {
                            for (int r = 0; r < rowCount; r++) {
                                Log.d("XLS-IMPORT", "bearbeite Zeile: " + r);
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

                                    supervisor = sheet.getRow(r - 2).getCell(1).getStringCellValue(); //TODO supervisor noch richtig beschneiden

                                    Log.d(LOG_TAG, "7: " + lab_id + " " + String.valueOf(++groups) + " " + CONFIG.TERM + " " + supervisor);
                                    //erstelle gruppe in Datenbank
                                    DataSource datasource = new DataSource(context);
                                    datasource.openW();
                                    datasource.createGroup(lab_id, String.valueOf(++groups), CONFIG.TERM, supervisor);
                                    datasource.close();

                                    Log.d(LOG_TAG, "Gruppe " + groups + ":");
                                }
                                //einzelnen Studenten erfassen
                                if (row.getCell(1).getCellType() == 0 && row.getCell(2).getCellType() == 1) {
                                    //Log.d("XLS-IMPORT", "Student-Data");
                                    String salut = row.getCell(2).getStringCellValue().trim();
                                    String surname = row.getCell(3).getStringCellValue().trim();
                                    String firstname = row.getCell(4).getStringCellValue().trim();
                                    int matr = (int) row.getCell(5).getNumericCellValue();
                                    String email = row.getCell(6).getStringCellValue().trim();

                                    Log.d(LOG_TAG, salut + " " + surname + " " + firstname + " (" + matr + "|" + email + ")");
                                    //Studentendaten in lokale Datenbank schreiben
                                    DataSource datasource = new DataSource(context);
                                    datasource.openW();
                                    datasource.createStudent(lab_id, String.valueOf(groups), CONFIG.TERM, salut, surname, firstname, String.valueOf(matr), email, "");
                                    datasource.close();
                                }
                            }
                        } catch (Exception e) {
                            Log.d(LOG_TAG, "Fehler innerhalb eines Sheets");
                            e.printStackTrace();
                            fail = true;
                        }
                        Log.d(LOG_TAG, "Blattname: " + sheet.getSheetName() + "; Columns:" + totalCols + "; Rows:" + sheet.getLastRowNum());
                        Log.d(LOG_TAG, "------------------------------------------------");
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Fehler beim öffnen der verschiedenen Sheets");
                    e.printStackTrace();
                    fail = true;
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, "Kopfdatenfehler");
                e.printStackTrace();
                fail = true;
            }
        } else {
            Log.d(LOG_TAG, "XLSX-Datei konnte nicht geöffnet werden. (if)");
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        if (prgDialog.isShowing())
            prgDialog.dismiss();
        if (fail)
            Toast.makeText(context, "XLSX-Import fehlgeschlagen!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "XLSX-Import erfolgreich!", Toast.LENGTH_SHORT).show();
    }
}
