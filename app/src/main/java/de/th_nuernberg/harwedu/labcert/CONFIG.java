package de.th_nuernberg.harwedu.labcert;


import android.content.Context;
import android.os.Environment;

import java.io.File;

import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.DbHelper;

public class CONFIG {
    private static Context context; //getActivity()
    //updateable
    private static String EMAIL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String TERM = "WS1617";

    //not updateable
    private static final String MAILHOST = "my.ohmportal.de";
    private static final String DirectoryPDF = Environment.getExternalStorageDirectory() + "/Documents";

    public static void updateConfig(Context context) {
        DataSource datasource = new DataSource(context);
        datasource.openR();

        USERNAME = datasource.getSetting(DbHelper.SETTING_MAIL_USERNAME);
        PASSWORD = datasource.getSetting(DbHelper.SETTING_MAIL_PASSWORD);
        EMAIL = USERNAME + "@th-nuernberg.de";
        TERM = datasource.getSetting(DbHelper.SETTING_TERM);

        datasource.close();
    }

    public static Context getContext() { return context; }

    public static String getEMAIL() { return EMAIL; }

    public static String getUSERNAME() { return USERNAME; }

    public static String getPASSWORD() { return PASSWORD; }

    public static String getTERM() { return TERM; }

    public static String getMAILHOST() { return MAILHOST; }

    public static String getDirectoryPDF() { return DirectoryPDF; }

    public static void setContext(Context context) {
        CONFIG.context = context;
    }
}
