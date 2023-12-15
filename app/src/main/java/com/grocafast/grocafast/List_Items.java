package com.grocafast.grocafast;

import static com.grocafast.grocafast.LoginActivity.shopping_items;
import static com.grocafast.grocafast.LoginActivity.usernameText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class List_Items extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        try {
            // Hide top bar
            getSupportActionBar().hide();

            DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Users").child(usernameText);
            DatabaseReference items_reference = FirebaseDatabase.getInstance().getReference("Items");
            String list_key = getIntent().getStringExtra("list_key");

            ListView listView = (ListView) findViewById(R.id.listView);

            for (int i = 0; i < listView.getChildCount(); i++) {
                try {
                    // Set text color of list view items to black
                    ((TextView) listView.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<String> list = new ArrayList<>(); // Create a new ArrayList to store the items
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter); // Set the adapter for the ListView

// Reference to the "items" node under the specific user list
            DatabaseReference user_lists_reference = user_reference.child("lists").child(list_key).child("items");

            user_lists_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            // Add items to the list
                            list.add((String) snapshot.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    arrayAdapter.notifyDataSetChanged(); // Update the ListView to display the items
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the case where the data retrieval is cancelled
                }
            });

            Button editListButton = (Button) findViewById(R.id.editList);
            editListButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(List_Items.this, ListActivity.class);
                    items_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                shopping_items = new Items[list.size()];
                                int finalX = 0;
                                for (int c = 0; c < list.size(); c++) {
                                    // Check if the item exists in the database
                                    if (snapshot.hasChild(list.get(finalX).toLowerCase())) {
                                        int X = snapshot.child(list.get(finalX).toLowerCase()).child("x").getValue(int.class);
                                        int Y = snapshot.child(list.get(finalX).toLowerCase()).child("y").getValue(int.class);
                                        shopping_items[finalX] = new Items(list.get(finalX).toLowerCase());
                                        shopping_items[finalX].setx(X);
                                        shopping_items[finalX].sety(Y);
                                        finalX += 1;
                                    }
                                }
                                // Start the ListActivity to edit the list
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle the error case if data retrieval is cancelled
                        }
                    });
                }
            });

            Button deleteListButton = (Button) findViewById(R.id.deleteList);
            deleteListButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DatabaseReference list_reference = user_reference.child("lists").child(list_key);
                    try {
                        // Remove the list from the database
                        list_reference.removeValue();
                        // Go back to the History_Lists activity
                        Intent intent = new Intent(List_Items.this, History_Lists.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
