package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDoctorActivity extends Activity
{
    private boolean edit;
    private int pos;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actual_doctor_info);

        edit = getIntent().getBooleanExtra("edit", false);

        if (edit)
        {
            ((TextView)findViewById(R.id.add_doctor)).setText("Edit Doctor");
            pos = getIntent().getIntExtra("pos", 0);
        }
    }

    public void createDoctorClicked(View v)
    {
        final String name = ((EditText)findViewById(R.id.editText4)).getText().toString();
        final String type = ((EditText)findViewById(R.id.editText7)).getText().toString();
        final String number = ((EditText)findViewById(R.id.editText9)).getText().toString();

        final DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("doc-info");

        if (edit)
        {
            Doc_List.docList.remove(pos);
            Doc_List.docList.add(pos, new Doctor(name, type, number));

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
                                    finish();
                                }
                            });
                        }
                    }
                }
            });
        }
        else
        {
            DatabaseReference push = ref.push();
            push.child("name").setValue(name);
            push.child("type").setValue(type);
            push.child("number").setValue(number);
            finish();
        }
    }
}
