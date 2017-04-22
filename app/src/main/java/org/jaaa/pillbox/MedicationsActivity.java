package org.jaaa.pillbox;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

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
    }

    public void newMedicationClicked(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MedicationsActivity.this);
        final AlertDialog dialog;
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
                            double dose = Double.parseDouble(((EditText)root1.findViewById(R.id.editText10)).getText().toString());
                            String unit = ((Spinner)root1.findViewById(R.id.spinner)).getSelectedItem().toString();

                            if (dose <= 0)
                            {
                                return;
                            }

                            Medications.medications.get(day).add(new Medications(name, dose, unit));
                            DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("med-info-" + day);
                            DatabaseReference push = ref.push();
                            push.child("name").setValue(name);
                            push.child("dose").setValue(dose);
                            push.child("unit").setValue(unit);

                            refreshList();
                        }
                    });
                    builder1.setNegativeButton("Cancel", null);
                    dialog.cancel();
                    dialog.dismiss();
                    builder1.create().show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        dialog = builder.create();
        dialog.show();
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
                    Medications med = new Medications(name, Double.parseDouble(dose), unit);
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
