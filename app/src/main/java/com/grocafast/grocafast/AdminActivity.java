package com.grocafast.grocafast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    private CardView add, statistics;
    private ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Hide the action bar
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_admin);

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });

        statistics = findViewById(R.id.statFeature);
        add = findViewById(R.id.addFeature);

        // On click button action for statistics
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityStatistics();
            }
        });

        // On click button action for add
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddItem();
            }
        });
    }

    private void openActivityLogin() {
        try {
            // Open LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openActivityStatistics() {
        try {
            // Open Statistics activity
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openActivityAddItem() {
        try {
            // Open AddActivity
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
