package org.jaaa.pillbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorActivity extends Activity
{
    private ExpandableListView.OnChildClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        if (FirebaseHelper.user == null)
        {
            return;
        }

        listener = new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if (childPosition == 0)
                {
                    Intent i = new Intent(DoctorActivity.this, AddDoctorActivity.class);
                    i.putExtra("edit", true);
                    i.putExtra("pos", groupPosition);
                    startActivityForResult(i, 1001);
                }
                else if (childPosition == 1)
                {

                }

                return false;
            }
        };
    }

    public void addDoctorClicked(View v)
    {
        startActivity(new Intent(this, AddDoctorActivity.class));
    }

    @Override
    public void onActivityResult(int request, int result, Intent data)
    {
        if (request == 1001)
        {
            refreshList();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        refreshList();
    }

    public void refreshList()
    {
        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Doc_List.docList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    String name = d.child("name").getValue().toString();
                    String type = d.child("type").getValue().toString();
                    String number = d.child("number").getValue().toString();
                    Doctor doctor = new Doctor(name, type, number);
                    Doc_List.addDoc(doctor);
                }

                ExpandableListView expandableList = (ExpandableListView)findViewById(R.id.expandableListView);
                DoctorExpandableListAdapter adapter = new DoctorExpandableListAdapter(DoctorActivity.this, Doc_List.docList);
                expandableList.setAdapter(adapter);
                expandableList.setOnChildClickListener(listener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("DoctorActivity", Log.getStackTraceString(databaseError.toException()));
            }
        });
    }
}
