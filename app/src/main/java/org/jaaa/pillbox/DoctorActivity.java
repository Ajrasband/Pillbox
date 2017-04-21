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

        ExpandableListView expandableList = (ExpandableListView)findViewById(R.id.expandableListView);
        DoctorExpandableListAdapter adapter = new DoctorExpandableListAdapter(this, Doc_List.docList);
        expandableList.setAdapter(adapter);
    }
}
