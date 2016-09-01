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
 * Semester | Laborname | Gruppe | Typ | Anzahl
 */
public class ImportRequirementAdapter extends BaseAdapter {

    public ArrayList<Requirement> requirementList;
    Activity activity;

    public ImportRequirementAdapter(Activity activity, ArrayList<Requirement> requirementList) {
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
        TextView hTermTxt;
        TextView hLabTxt;
        TextView hGrpTxt;
        TextView hTypeTxt;
        TextView hCountTxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_req_checkable, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.hTermTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_term);
            holder.hLabTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_lab);
            holder.hGrpTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_grp);
            holder.hTypeTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_type);
            holder.hCountTxt = (TextView)
                    convertView.findViewById(R.id.textview_item_count);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Requirement requirement = requirementList.get(position);
        holder.hTermTxt.setText(requirement.getTerm());
        holder.hLabTxt.setText(String.valueOf(requirement.getLab_name()));
        holder.hGrpTxt.setText(String.valueOf(requirement.getGroup()));
        holder.hTypeTxt.setText(requirement.getType());
        holder.hCountTxt.setText(String.valueOf(requirement.getCount()));

        return convertView;
    }
}
