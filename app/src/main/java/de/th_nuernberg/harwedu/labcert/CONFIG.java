package de.th_nuernberg.harwedu.labcert;


import android.content.Context;

import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.DbHelper;

public class CONFIG {
    public static Context context; //getActivity()
    //updateable
    public static String EMAIL;
    public static String USERNAME;
    public static String PASSWORD;
    public static String TERM = "WS1617";

    //not updateable
    public static final String MAILHOST ="my.ohmportal.de";

    public static void updateConfig(Context context) {
        DataSource datasource = new DataSource(context);
        datasource.openR();

        USERNAME = datasource.getSetting(DbHelper.SETTING_MAIL_USERNAME);
        PASSWORD = datasource.getSetting(DbHelper.SETTING_MAIL_PASSWORD);
        EMAIL = USERNAME + "@th-nuernberg.de";
        TERM = datasource.getSetting(DbHelper.SETTING_TERM);

        datasource.close();
    }
}
