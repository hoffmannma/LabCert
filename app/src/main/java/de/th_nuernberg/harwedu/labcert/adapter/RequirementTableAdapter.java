package de.th_nuernberg.harwedu.labcert.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;

/**
 * Created by Edu on 17.08.2016.
 */
public class RequirementTableAdapter extends BaseAdapter {

    public ArrayList<Requirement> requirementList;
    Activity activity;

    public RequirementTableAdapter(Activity activity, ArrayList<Requirement> requirementList) {
        super();
        this.activity = activity;
        this.requirementList = requirementList;
    }

    @Override
    public int getCount() {
        return requirementList.size();
    }

    @Override
    public Object getItem(int position) {
        return requirementList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView hTypeTxt;
        TextView hNameTxt;
        TextView hCountTxt;
        TextView hGroupTxt;
        TextView hLabIdTxt;
        TextView hTermTxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_req, null);
            holder = new ViewHolder();
            /*holder.hTypeTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_type);*/
            holder.hNameTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_name);
            holder.hCountTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_count);
            /*
            holder.hGroupTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_group);
            holder.hLabIdTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_lab_id);
            holder.hTermTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_term);
            */
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Requirement requirement = requirementList.get(position);
        //holder.hTypeTxt.setText(requirement.getReqType());
        holder.hNameTxt.setText(String.valueOf(requirement.getReqName()));
        holder.hCountTxt.setText(String.valueOf(requirement.getCount()));
        /*
        holder.hGroupTxt.setText(String.valueOf(requirement.getGroup()));
        holder.hLabIdTxt.setText(String.valueOf(requirement.getLab_id()));
        holder.hTermTxt.setText(String.valueOf(requirement.getTerm()));
        */

        return convertView;
    }
}
