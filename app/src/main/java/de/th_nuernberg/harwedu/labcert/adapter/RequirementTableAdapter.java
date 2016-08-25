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
 * Adapter für Anforderungen
 * Typ | Anzahl
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
        TextView hCountTxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_req, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.hTypeTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_type);
            holder.hCountTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_count);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Requirement requirement = requirementList.get(position);
        holder.hTypeTxt.setText(requirement.getType());
        holder.hCountTxt.setText(String.valueOf(requirement.getCount()));


        return convertView;
    }
}
