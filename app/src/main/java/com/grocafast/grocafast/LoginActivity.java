package com.grocafast.grocafast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    public static Items[] shopping_items = new Items[0];
    public static String usernameText,userName;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the top bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        // Assign variable IDs
        final EditText username = findViewById(R.id.username);
        button = (Button) findViewById(R.id.login);
        textView = (TextView) findViewById(R.id.signup);

        // Set click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered username
                usernameText = username.getText().toString();

                // Check if the username field is empty
                if (usernameText.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_LONG).show();
                } else {
                    // Check if the username exists in the database
                    databaseReference.child(usernameText).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if the username is "Admin"
                            if (usernameText.equalsIgnoreCase("Admin")) {
                                Toast.makeText(LoginActivity.this, "Welcome admin", Toast.LENGTH_LONG).show();
                                openActivityAdmin();
                            } else {
                                // Check if the username exists in the database
                                if (!snapshot.exists()) {
                                    Toast.makeText(LoginActivity.this, "Please enter a correct username", Toast.LENGTH_LONG).show();
                                } else {
                                    // Open the home activity
                                    openActivityHome();
                                    userName = snapshot.child("username").getValue(String.class);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        // Set click listener for the signup text
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the signup activity
                openActivitySignup();
            }
        });
    }

    // Open the home activity
    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Open the admin activity
    private void openActivityAdmin() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    // Open the signup activity
    private void openActivitySignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
