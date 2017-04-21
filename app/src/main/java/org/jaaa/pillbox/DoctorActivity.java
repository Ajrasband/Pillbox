package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class DoctorActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        ExpandableListView expandableList = (ExpandableListView)findViewById(android.R.id.list);
        DoctorExpandableListAdapter adapter = new DoctorExpandableListAdapter(this, null);
        expandableList.setAdapter(adapter);
    }
}
