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

public class CsvParser {

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

}