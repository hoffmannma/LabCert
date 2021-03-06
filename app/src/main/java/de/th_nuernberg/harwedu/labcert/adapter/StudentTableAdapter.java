package de.th_nuernberg.harwedu.labcert.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.objects.Student;

/**
 * Adapter für Studenten
 * Nachname | Vorname | Fortschritt in Prozent
 */
public class StudentTableAdapter extends BaseAdapter {

    public ArrayList<Student> studentList;
    Activity activity;

    public StudentTableAdapter(Activity activity, ArrayList<Student> studentList) {
        super();
        this.activity = activity;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView hSurnameTxt;
        TextView hFirstnameTxt;
        TextView hProgressTxt;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_student, parent, false);
            holder = new ViewHolder();
            holder.hSurnameTxt = (TextView)
                    convertView.findViewById(R.id.textview_row_surname);
            holder.hFirstnameTxt = (TextView)
                    convertView.findViewById(R.id.textview_row_firstname);
            holder.hProgressTxt = (TextView)
                    convertView.findViewById(R.id.textview_row_progress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Student student = studentList.get(position);
        holder.hSurnameTxt.setText(String.valueOf(student.getSurname()));
        holder.hFirstnameTxt.setText(student.getFirstname());
        holder.hProgressTxt.setText(String.valueOf(student.getProgress() + "%"));

        return convertView;
    }

}
