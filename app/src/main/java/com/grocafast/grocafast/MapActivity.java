package com.grocafast.grocafast;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    private String aisleNumber;
    private ImageView map;
    private ImageView back;

    public MapActivity() {

    }

    public MapActivity(String aisleNumber) {
        this.aisleNumber = aisleNumber;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);

        back = (ImageView) findViewById(R.id.back);

        // Set onClickListener for the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        try {
            getMap(); // Load the map image
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, "Error occurred while loading the map", Toast.LENGTH_LONG).show();
        }
    }

    // Load the map image based on the aisle number
    public void getMap() {
        try {
            String aisleNumber = getIntent().getStringExtra("aisleNumber");
            map = (ImageView) findViewById(R.id.themap);

            switch (aisleNumber) {
                case "1":
                    map.setImageResource(R.drawable.first_map);
                    break;
                case "2":
                    map.setImageResource(R.drawable.second_map);
                    break;
                case "3":
                    map.setImageResource(R.drawable.third_map);
                    break;
                case "4":
                    map.setImageResource(R.drawable.first_map);
                    break;
                case "5":
                    map.setImageResource(R.drawable.second_map);
                    break;
                case "6":
                    map.setImageResource(R.drawable.third_map);
                    break;
                case "7":
                    map.setImageResource(R.drawable.first_map);
                    break;
                case "8":
                    map.setImageResource(R.drawable.second_map);
                    break;
                default:
                    Toast.makeText(MapActivity.this, "Item does not exist", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, "Error occurred while loading the map", Toast.LENGTH_LONG).show();
        }
    }

    // Open the home activity
    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
