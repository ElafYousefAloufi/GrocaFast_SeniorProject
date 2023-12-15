package com.grocafast.grocafast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.grocafast.grocafast.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private TextView textView;
    private ActivitySignupBinding activitySignupBinding;
    private String firstName, lastName, username;
    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        activitySignupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(activitySignupBinding.getRoot());

        // Sign up button click listener
        activitySignupBinding.sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = activitySignupBinding.firstName.getText().toString();
                lastName = activitySignupBinding.lastName.getText().toString();
                username = activitySignupBinding.username.getText().toString();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty()) {
                    try {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        // Check if username already exists
                        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(SignupActivity.this, "This username is already registered", Toast.LENGTH_LONG).show();
                                    activitySignupBinding.username.setText("");
                                } else {
                                    Users users = new Users(firstName, lastName, username);
                                    // Create a new user account
                                    databaseReference.child(username).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            activitySignupBinding.firstName.setText("");
                                            activitySignupBinding.lastName.setText("");
                                            activitySignupBinding.username.setText("");
                                            Toast.makeText(SignupActivity.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                                            openActivityLogin();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        textView = (TextView) findViewById(R.id.back);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });
    }

    // Open the login activity
    private void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
