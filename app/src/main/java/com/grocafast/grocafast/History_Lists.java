package com.grocafast.grocafast;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.grocafast.grocafast.LoginActivity.shopping_items;
import static com.grocafast.grocafast.LoginActivity.usernameText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocafast.grocafast.ui.CustomAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History_Lists extends AppCompatActivity {

    private ImageView back;
    ;
    DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Users").child(usernameText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // hide top bar
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_history_lists);

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);

        List<String> list = new ArrayList<>();
        List<String> list_numbers = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        DatabaseReference user_lists_reference = user_reference.child("lists");
        final int[] size = {0};

        user_lists_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Add the "date" value from each snapshot to the list
                        list.add(0, (String) snapshot.child("date").getValue());
                        // Add the key of each snapshot to the list_numbers
                        list_numbers.add(snapshot.getKey());
                    }
                    arrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                try {
                    throw databaseError.toException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Get the selected item text from ListView
                    String selectedItemKey = list_numbers.get(position);

                    // Open List_Items activity and pass the selected list key
                    Intent intent = new Intent(History_Lists.this, List_Items.class);
                    intent.putExtra("list_key", selectedItemKey);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openActivityHome() {
        try {
            // Open HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
