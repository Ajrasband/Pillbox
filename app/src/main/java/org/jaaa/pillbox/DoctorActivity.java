package org.jaaa.pillbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
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

        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
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

    public void addDoctorClicked(View v)
    {
        startActivity(new Intent(this, AddDoctorActivity.class));
    }

    @Override
    public void onStop()
    {
        super.onStop();

        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");

        for (Doctor d : Doc_List.docList)
        {
            DatabaseReference push = ref.push();
            push.child("name").setValue(d.getName());
            push.child("type").setValue(d.getType());
            push.child("number").setValue(d.getNumber());
        }
    }
}
