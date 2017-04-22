package org.jaaa.pillbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MedicationExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private ArrayList<Medications> list;

    public MedicationExpandableListAdapter(Context context, ArrayList<Medications> list)
    {
        this.context = context;
        this.list = list;
    }

    public View getChildView(int groupPos, int childPos, boolean isLast, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.medication_sub, parent, false);
        }

        if (childPos == 0)
        {
            ((ImageView) v.findViewById(R.id.imageView)).setImageResource(android.R.drawable.ic_input_add);
            ((TextView) v.findViewById(R.id.textView2)).setText("Edit");
        }
        else if (childPos == 1)
        {
            ((ImageView) v.findViewById(R.id.imageView)).setImageResource(android.R.drawable.ic_delete);
            ((TextView) v.findViewById(R.id.textView2)).setText("Delete");
        }

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public int getGroupCount()
    {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.medication_group, parent, false);

            Medications m = list.get(groupPos);
            double dosage = m.getDosage();
            String name = m.getMedName();
            String unit = m.getDosageType();
            String time = "";

            DateFormat format = new SimpleDateFormat("HH:mm");
            DateFormat format1 = new SimpleDateFormat("hh:mm a");
            Date date;

            try
            {
                date = format.parse(m.getHour() + ":" + m.getMinutes());
                time = format1.format(date);
            }
            catch (ParseException e)
            {
                Log.d("MedicationList", Log.getStackTraceString(e));
            }

            String title = String.format("Name: %s%nDosage: %.1f %s%nTime: %s", name, dosage, unit, time);
            ((TextView) v.findViewById(R.id.textView)).setText(title);
        }

        return v;
    }
}
