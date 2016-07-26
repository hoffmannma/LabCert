package de.th_nuernberg.harwedu.labcert.interfaces;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edu on 25.07.2016.
 */
public interface TaskCompleted {
    void onTaskComplete(ArrayList<HashMap<String, String>> result);
}
