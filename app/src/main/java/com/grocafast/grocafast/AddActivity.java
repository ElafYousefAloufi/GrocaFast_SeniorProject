package com.grocafast.grocafast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.grocafast.grocafast.databinding.ActivityAddBinding;
import com.grocafast.grocafast.databinding.ActivityAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    private String itemName, category, aisleNumber;
    private EditText editText;
    private ActivityAddBinding activityAddBinding;
    FirebaseDatabase db;
    DatabaseReference reference;
    private ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Hide top bar
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_admin);

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAdmin();
            }
        });

        //**
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(activityAddBinding.getRoot());

        activityAddBinding.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    itemName = activityAddBinding.itemName.getText().toString().toLowerCase();
                    category = activityAddBinding.category.getText().toString().toLowerCase();
                    aisleNumber = activityAddBinding.aisleNumber.getText().toString();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");
                    if (!itemName.isEmpty() && !category.isEmpty() && !aisleNumber.isEmpty()) {

                        Items items = new Items(itemName, category, aisleNumber);
                        databaseReference.child(itemName).setValue(items).addOnCompleteListener(new OnCompleteListener<Void>() {

                            // Reset data fields to empty
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                try {
                                    activityAddBinding.itemName.setText("");
                                    activityAddBinding.category.setText("");
                                    activityAddBinding.aisleNumber.setText("");

                                    Toast.makeText(AddActivity.this, "The item is added successfully", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openActivityAdmin() {
        try {
            // Open AdminActivity
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
