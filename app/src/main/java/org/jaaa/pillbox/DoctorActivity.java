package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        if (FirebaseHelper.user == null)
        {
            return;
        }

        Log.d("DoctorInfo", "Contacting database.");
        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d("DoctorActivity", "Data change!");
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("DoctorActivity", Log.getStackTraceString(databaseError.toException()));
            }
        });
    }
}
