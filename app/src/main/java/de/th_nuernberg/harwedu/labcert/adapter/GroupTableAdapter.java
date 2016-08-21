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
import de.th_nuernberg.harwedu.labcert.objects.Requirement;

/**
 * Created by Edu on 21.08.2016.
 */
public class GroupTableAdapter extends BaseAdapter{

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
        TextView hLabIdTxt;
        TextView hGroupNoTxt;
        TextView hSupervisorTxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_group, null);
            holder = new ViewHolder();
            holder.hLabTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_lab);
            holder.hLabIdTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_lab_id);
            holder.hGroupNoTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_group_no);
            holder.hSupervisorTxt = (TextView)
                    convertView.findViewById(R.id.textview_group_supervisor);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       Group group = groupList.get(position);
        //holder.hTypeTxt.setText(requirement.getReqType());
        holder.hLabTxt.setText(group.getLab());
        holder.hLabIdTxt.setText(String.valueOf(group.getLab_id()));
        holder.hGroupNoTxt.setText(String.valueOf(group.getGroup_id()));
        holder.hSupervisorTxt.setText(String.valueOf(group.getSupervisor()));
        /*
        holder.hGroupTxt.setText(String.valueOf(requirement.getGroup()));
        holder.hLabIdTxt.setText(String.valueOf(requirement.getLab_id()));
        holder.hTermTxt.setText(String.valueOf(requirement.getTerm()));
        */

        return convertView;
    }
}
