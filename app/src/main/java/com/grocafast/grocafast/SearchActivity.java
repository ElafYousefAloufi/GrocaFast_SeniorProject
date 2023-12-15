package com.grocafast.grocafast;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class SearchActivity extends AppCompatActivity {
    DatabaseReference reference;
    private ListView listData;
    private AutoCompleteTextView txtSearch;
    private ImageView back, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        // Initialize the Firebase database reference
        reference = FirebaseDatabase.getInstance().getReference("Items");

        // Get references to the UI elements
        listData = (ListView) findViewById(R.id.listData);
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        back = (ImageView) findViewById(R.id.back);

        // Set click listener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome(); // Open the HomeActivity
            }
        });

        // ValueEventListener to retrieve data from Firebase database
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    readData(snapshot); // Read and display the data from the snapshot
                    populateSearch(snapshot); // Populate the search suggestions list
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        reference.addListenerForSingleValueEvent(event); // Attach the ValueEventListener to the database reference
    }

    // Populate the search suggestions list with item names from the database
    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> itemNames = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                String itemName = ds.child("itemName").getValue(String.class);
                itemNames.add(itemName);
            }

            try {
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, itemNames);
                txtSearch.setAdapter(arrayAdapter);
                txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            String name = txtSearch.getText().toString().trim();
                            searchItem(name); // Search for the selected item
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(SearchActivity.this, "No found data", Toast.LENGTH_LONG).show();
        }
    }

    // Read the data from the snapshot and display it in the ListView
    private void readData(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            ArrayList<String> listitems = new ArrayList<>();
            for (DataSnapshot ds : snapshot.getChildren()) {
                // Read item details from the snapshot and create an Items object
                Items item = new Items(ds.child("itemName").getValue(String.class), ds.child("category").getValue(String.class), ds.child("aisleNumber").getValue(String.class));
                listitems.add(item.getItemName());
            }

            try {
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listitems);
                listData.setAdapter(arrayAdapter);

                listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            openMap(adapterView.getItemAtPosition(i).toString()); // Open the map for the selected item
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Item not found..!", Toast.LENGTH_LONG).show();
        }
    }

    // Search for the item in the database
    private void searchItem(String name) {
        Query query = reference.orderByChild("itemName").equalTo(name.trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        ArrayList<String> listitems = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            // Read item details from the snapshot and create an Items object
                            Items item = new Items(ds.child("itemName").getValue(String.class), ds.child("category").getValue(String.class), ds.child("aisleNumber").getValue(String.class));
                            listitems.add(item.getItemName());
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listitems);
                        listData.setAdapter(arrayAdapter);

                        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    openMap(adapterView.getItemAtPosition(i).toString()); // Open the map for the selected item
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Item not found..!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Open the map activity with the selected aisle number
    private void openMap(String i) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String aisleNum = snapshot.child(i).child("aisleNumber").getValue(String.class);
                    openActivityMap(aisleNum); // Open the MapActivity with the aisle number
                    Toast.makeText(SearchActivity.this, "Aisle number: " + snapshot.child(i).child("aisleNumber").getValue(String.class), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Open the HomeActivity
    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Open the MapActivity with the selected aisle number
    private void openActivityMap(String x) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("aisleNumber", x);
        startActivity(intent);
    }
}
