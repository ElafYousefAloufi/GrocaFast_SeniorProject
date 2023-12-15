package com.grocafast.grocafast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocafast.grocafast.ui.CustomAdapter;
import com.grocafast.grocafast.ui.CustomAdapter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import static com.grocafast.grocafast.LoginActivity.shopping_items;
import static com.grocafast.grocafast.LoginActivity.usernameText;


public class ListActivity  extends AppCompatActivity {

    ArrayList<DataList> dataList,dataList2;
    private static CustomAdapter adapter;
    private static CustomAdapter2 adapter2;
    public ArrayList<String> items,items2;

    private ArrayAdapter<String> itemsAdapter,itemsAdapter2;
    private ListView listView,listView2;
    private Button button, toSearchItem;
    private ImageView back, next;
    DatabaseReference items_reference = FirebaseDatabase.getInstance().getReference("Items");
    //private Button toSearchItem;
    DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Users").child(usernameText);
    DatabaseReference user_lists_reference = user_reference.child("lists");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list);

        //assign variables ID's
        listView = findViewById(R.id.listView);
        dataList = new ArrayList<>();
        adapter = new CustomAdapter(dataList,getApplicationContext());
        listView.setAdapter(adapter);

        listView2 = findViewById(R.id.listView2);
        dataList2 = new ArrayList<>();
        adapter2 = new CustomAdapter2(dataList2,getApplicationContext());
        listView2.setAdapter(adapter2);

        //items list arrayList
        items = new ArrayList<>();
        items2 = new ArrayList<>();

        button = findViewById(R.id.button);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);


        //on click button action call open home page method
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        //on click icon action call open path page method
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityPath();
            }
        });

        //on click button action
       button.setOnClickListener(new View.OnClickListener(){
         @Override
         public void onClick(View view) {
            addItem(view);
            }
       });

       // getting history
        final int[] all_items_count = {0};
        items_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                all_items_count[0] = (int) dataSnapshot.getChildrenCount();
                String[] items_names = new String[all_items_count[0]] ;
                int[] items_repeatation = new int[all_items_count[0]] ;

                // add all items names and initial count zero

                items_reference.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         int counter = 0;
                         for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                             items_names[counter] = (String) snapshot.getKey();
//                             Log.d("TAGX1", "Value is: " + snapshot.child(snapshot.getKey()));
                             items_repeatation[counter] = 0 ;
                             counter++;
                         }

                         // count and populate
                         int finalCounter = counter;
                         user_lists_reference.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                     Log.d("TAGX1", "Value is: " + snapshot.child("items").getValue().toString());
                                     if (snapshot.hasChild("items")) {
                                         String items_string = snapshot.child("items").getValue().toString();
                                         List<String> items_string_list = Arrays.asList(items_string.substring(1, items_string.length() - 1).split(", "));
                                         for (int i = 0; i < items_string_list.size(); i++) {
                                             for (int j = 0; j < finalCounter; j++) {
                                                 if (items_string_list.get(i).equals(items_names[j])) {
                                                     items_repeatation[j]++;
                                                 }
                                             }
//                                          Log.d("TAGX1", "Value is: " + items_string_list.get(i));
                                         }
                                     }
                                 }

                                 String top[] = GetTOPThreeItems(items_repeatation,items_names);

                                 if (!top[0].equals("")) addItem2(top[0]);
                                 if (!top[1].equals("")) addItem2(top[1]);
                                 if (!top[2].equals("")) addItem2(top[2]);
                                 Log.d("TAGX1", "Value is: " + top[0]);
                                 Log.d("TAGX1", "Value is: " + top[1]);
                                 Log.d("TAGX1", "Value is: " + top[2]);
                             }
                             @Override
                             public void onCancelled(DatabaseError databaseError) {

                             }
                         });

                     }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                dataList.remove(position);
                adapter= new CustomAdapter(dataList,getApplicationContext());
                listView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int c=0;c<items.size();c++) {
                    if (items.get(c).equals(items2.get(position))) {
                        Toast.makeText(getApplicationContext(), "Items already exist", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                dataList.add(new DataList(items2.get(position)));
                adapter= new CustomAdapter(dataList,getApplicationContext());
                listView.setAdapter(adapter);
                items.add(items2.get(position));
                Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
            }
        });

//        Log.d("TAGX1", String.valueOf(shopping_items.length));
//        Log.d("TAGX1", String.valueOf(shopping_items));
        if (shopping_items.length>0) {
            for (int i = 0; i < shopping_items.length ; i++) {
//                Log.d("TAGX1", String.valueOf(shopping_items[i].itemName));
                dataList.add(new DataList(shopping_items[i].itemName));
                adapter= new CustomAdapter(dataList,getApplicationContext());
                listView.setAdapter(adapter);
                items.add(shopping_items[i].itemName);
            }
        }

    }

    public static String[] GetTOPThreeItems(int[] items_repeatation, String[] items_names){
        String[] top = new String[3];
        top[0]="";top[1]="";top[2]="";
        int[] topcount = new int[3];
        topcount[0]=0;topcount[1]=0;topcount[2]=0;

        for (int j=0; j< items_repeatation.length ; j++) {
//            Log.d("TAGX1", "Value is: " + items_repeatation[j] + " " + items_names[j]);
            if (items_repeatation[j] > topcount[0]){
                top[0] = items_names[j];
                topcount[0] = items_repeatation[j];
            }
        }
        for (int j=0; j< items_repeatation.length ; j++) {
            if (items_repeatation[j] > topcount[1] && !items_names[j].equals(top[0])){
                top[1] = items_names[j];
                topcount[1] = items_repeatation[j];
            }
        }
        for (int j=0; j< items_repeatation.length ; j++) {
            if (items_repeatation[j] > topcount[2] && !items_names[j].equals(top[0]) && !items_names[j].equals(top[1])){
                top[2] = items_names[j];
                topcount[2] = items_repeatation[j];
            }
        }
        return top;
    }

    //add item to the array list
    private void addItem(View view) {
        EditText input= findViewById(R.id.editText);
        String itemText= input.getText().toString();
        if(!(itemText.equals(""))){
            for (int c=0;c<items.size();c++) {
                if (items.get(c).equals(itemText)) {
                    Toast.makeText(getApplicationContext(), "Items already exist", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            dataList.add(new DataList(itemText));
            adapter= new CustomAdapter(dataList,getApplicationContext());
            listView.setAdapter(adapter);
            items.add(itemText);
//            itemsAdapter.add(itemText);
            input.setText("");
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter an item...", Toast.LENGTH_LONG).show();
        }
    }

    private void addItem2(String itemName) {
        if(!(itemName.equals(""))){
            dataList2.add(new DataList(itemName));
            adapter2= new CustomAdapter2(dataList2,getApplicationContext());
            listView2.setAdapter(adapter2);
            items2.add(itemName);
//            itemsAdapter.add(itemText);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter an item...", Toast.LENGTH_LONG).show();
        }

    }

    //open home page
    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //open search page
    private void openActivitySearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //open path page
    private void openActivityPath() {
        if (items.size() == 0) {
            Toast.makeText(ListActivity.this, "No Items Selected", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ShortestPath.class);
        items_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int valid_count = 0;
                for (int c=0;c<items.size();c++) {
                    int finalX = c;
                    if (snapshot.hasChild(items.get(finalX).toLowerCase())) { //
                        valid_count += 1;
                    }
                }
                shopping_items = new Items[valid_count];
                int finalX = 0;
                for (int c=0;c<items.size();c++) {
                    if (snapshot.hasChild(items.get(finalX).toLowerCase())) { //
                        int X =  snapshot.child(items.get(finalX).toLowerCase()).child("x").getValue(int.class);
                        int Y =  snapshot.child(items.get(finalX).toLowerCase()).child("y").getValue(int.class);
                        shopping_items[finalX] = new Items(items.get(finalX).toLowerCase());
                        shopping_items[finalX].setx(X);
                        shopping_items[finalX].sety(Y);

                        Log.d("TAGX1", "Value is: " + X);
                        Log.d("TAGX1", "Value is: " + Y);
                        Log.d("TAGX1", "Value is: " + shopping_items[finalX].itemName);
                        Log.d("TAGX1", "Value is: " + valid_count);
                        Log.d("TAGX1", "Value is: " + shopping_items.length);

                        finalX += 1;
                    }
                }
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}