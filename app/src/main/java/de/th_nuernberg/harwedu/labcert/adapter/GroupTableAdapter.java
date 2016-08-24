package de.th_nuernberg.harwedu.labcert.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.objects.Group;

/**
 * Adapter für Gruppen
 * Laborname | Gruppennummer
 */
public class GroupTableAdapter extends BaseAdapter {

    public ArrayList<Group> groupList;
    Activity activity;

    public GroupTableAdapter(Activity activity, ArrayList<Group> groupList) {
        super();
        this.activity = activity;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView hLabTxt;
        TextView hGroupNoTxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_group, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.hLabTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_lab);
            holder.hGroupNoTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_group_no);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Group group = groupList.get(position);
        holder.hLabTxt.setText(group.getLab_name());
        holder.hGroupNoTxt.setText(group.getGroup());

        return convertView;
    }
}
