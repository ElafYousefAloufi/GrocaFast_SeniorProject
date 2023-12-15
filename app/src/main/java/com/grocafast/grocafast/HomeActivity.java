package com.grocafast.grocafast;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.grocafast.grocafast.Community.CommunityActivity;

public class HomeActivity  extends AppCompatActivity {

    private CardView createList, searchItem,viewHistory,community;
    private ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //hide top bar
        getSupportActionBar().hide();

        //display xml home page
        setContentView(R.layout.activity_home);

        //assign variables ID's
        createList =  findViewById(R.id.createlist);
        searchItem =  findViewById(R.id.searchitem);
        viewHistory = findViewById(R.id.ListHistory);
        community = findViewById(R.id.community);

        back = (ImageView) findViewById(R.id.back);

        //on click button action
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });

        //on click button action
        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityList();
            }
        });

        //on click button action
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySearch();
            }
        });
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHistory();
            }
        });

        //on click button action
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCommunity();
            }
        });
    }

    private void openActivityHistory() {
        Intent intent = new Intent(this, History_Lists.class);
        startActivity(intent);
    }

    //open list page
    private void openActivityList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    //open search page
    private void openActivitySearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //open login page
    private void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    //open community page
    private void openActivityCommunity() {
        Intent intent = new Intent(this, CommunityActivity.class);
        startActivity(intent);
    }
}
