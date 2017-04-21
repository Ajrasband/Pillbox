package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class MedicationsActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        ExpandableListView expandableList = (ExpandableListView)findViewById(R.id.list_view);
        MedicationExpandableListAdapter adapter = new MedicationExpandableListAdapter(this, null);
        expandableList.setAdapter(adapter);
    }
}
