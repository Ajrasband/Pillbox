package org.jaaa.pillbox;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MedicationsActivity extends Activity
{
    private static String[] dosageTypes = new String[] { "mg", "pills", "ml", "fl. oz" };

    private ExpandableListView.OnChildClickListener listener;
    private int day;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        if (FirebaseHelper.user == null)
        {
            return;
        }

        day = getIntent().getIntExtra("day", 0);

        listener = new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if (childPosition == 0)
                {
                    Intent i = new Intent(MedicationsActivity.this, AddDoctorActivity.class);
                    i.putExtra("edit", true);
                    i.putExtra("pos", groupPosition);
                    startActivityForResult(i, 1001);
                }
                else if (childPosition == 1)
                {
                    final DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");
                    Doc_List.docList.remove(groupPosition);
                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (Doctor d : Doc_List.docList)
                                {
                                    DatabaseReference push = ref.push();
                                    push.child("name").setValue(d.getName());
                                    push.child("type").setValue(d.getType());
                                    push.child("number").setValue(d.getNumber()).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            refreshList();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

                return false;
            }
        };
    }

    public void newMedicationClicked(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MedicationsActivity.this);
        final View root = getLayoutInflater().inflate(R.layout.medication_create_new, null);
        builder.setView(root);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                final String name = ((TextView)root.findViewById(R.id.editText4)).getText().toString();

                if (!name.isEmpty())
                {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationsActivity.this, android.R.layout.simple_spinner_item, dosageTypes);
                    final View root1 = getLayoutInflater().inflate(R.layout.medication_dosage_new, null);
                    ((Spinner)root1.findViewById(R.id.spinner)).setAdapter(adapter);
                    builder1.setView(root1);
                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            final double dose = Double.parseDouble(((EditText)root1.findViewById(R.id.editText10)).getText().toString());
                            final String unit = ((Spinner)root1.findViewById(R.id.spinner)).getSelectedItem().toString();

                            if (dose <= 0)
                            {
                                return;
                            }

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MedicationsActivity.this);
                            final View root2 = getLayoutInflater().inflate(R.layout.time_picker, null);
                            builder2.setView(root2);
                            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    int hour = ((TimePicker)root2.findViewById(R.id.timePicker2)).getCurrentHour();
                                    int minute = ((TimePicker)root2.findViewById(R.id.timePicker2)).getCurrentMinute();

                                    Medications.medications.get(day).add(new Medications(name, dose, unit, hour, minute));
                                    DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("med-info-" + day);
                                    DatabaseReference push = ref.push();
                                    push.child("name").setValue(name);
                                    push.child("dose").setValue(dose);
                                    push.child("unit").setValue(unit);
                                    push.child("hour").setValue(hour);
                                    push.child("minute").setValue(minute);

                                    refreshList();
                                }
                            });
                            builder2.setNegativeButton("Cancel", null);
                            builder2.create().show();
                        }
                    });
                    builder1.setNegativeButton("Cancel", null);
                    builder1.create().show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        refreshList();
    }

    public void refreshList()
    {
        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("med-info-" + day);
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (ArrayList<Medications> a : Medications.medications)
                {
                    a.clear();
                }

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    String name = d.child("name").getValue().toString();
                    String dose = d.child("dose").getValue().toString();
                    String unit = d.child("unit").getValue().toString();
                    String hour = d.child("hour").getValue().toString();
                    String minute = d.child("minute").getValue().toString();
                    Medications med = new Medications(name, Double.parseDouble(dose), unit, Integer.parseInt(hour), Integer.parseInt(minute));
                    Medications.medications.get(day).add(med);
                }

                ExpandableListView expandableList = (ExpandableListView)findViewById(R.id.list_view);
                MedicationExpandableListAdapter adapter = new MedicationExpandableListAdapter(MedicationsActivity.this, Medications.medications.get(day));
                expandableList.setAdapter(adapter);
                //expandableList.setOnChildClickListener(listener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("DoctorActivity", Log.getStackTraceString(databaseError.toException()));
            }
        });
    }
}
