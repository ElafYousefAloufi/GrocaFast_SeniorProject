package com.grocafast.grocafast.Community;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocafast.grocafast.HomeActivity;
import com.grocafast.grocafast.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class CommunityActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;
    private ChatServer chatServer;
    private WebSocketClient chatWebSocket;
    private DatabaseReference messagesRef;
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        // Hide the action bar
        getSupportActionBar().hide();

        // Initialize UI elements by finding their respective views by ID
        back = (ImageView) findViewById(R.id.back);
        recyclerView = findViewById(R.id.chat_list);
        messageEditText = findViewById(R.id.Chat_box);
        sendButton = findViewById(R.id.send);

        // Set up Firebase by calling the setupFirebase() method
        setupFirebase();

        // Create a new ChatAdapter object and set it as the adapter for the recyclerView
        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        // Set a LinearLayoutManager as the layout manager for the recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a new ChatServer object and start it
        chatServer = new ChatServer(8080, chatAdapter, this, messagesRef);
        chatServer.start();

        // Connect to the WebSocket server by calling the connectWebSocket() method
        connectWebSocket();


        // Set an OnClickListener for the sendButton
        sendButton.setOnClickListener(view -> {
            // Get the message from the messageEditText and check if it is not empty
            String message = messageEditText.getText().toString();
            if (!message.isEmpty()) {
                try {
                    // Send the message to the WebSocket server by calling the chatWebSocket.send() method
                    chatWebSocket.send(message);
                } catch (WebsocketNotConnectedException exception) {
                    // Handle the WebsocketNotConnectedException by logging the error message
                    Toast.makeText(CommunityActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

                }

                // Create a new ChatMessage object with the message data
                ChatMessage chatMessage = new ChatMessage("Me:", message, new Date().getTime());

                // Add the ChatMessage to the chatAdapter
                chatAdapter.addMessage(chatMessage);

                // Clear the messageEditText
                messageEditText.setText("");
            } else {
                // Display a Toast message if the messageEditText is empty
                Toast.makeText(CommunityActivity.this, "There is nothing to send, please write the message", Toast.LENGTH_LONG).show();
            }
        });

        // Set an OnClickListener for the back button
        back.setOnClickListener(view -> {
            // Call the openActivityHome() method when the back button is clicked
            openActivityHome();
        });

    }
    private void setupFirebase() {
        // Get a reference to the "Community" node in the Firebase Realtime Database
        messagesRef = FirebaseDatabase.getInstance().getReference("Community");

        // Attach a ValueEventListener to the "Community" node to listen for changes in the data
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Loop through each child node of the "Community" node
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    // Get the message data as a Map
                    Map<String, Object> messageData = (Map<String, Object>) messageSnapshot.getValue();

                    // Check if the "sender" field is null before calling toString()
                    String sender = (messageData.get("sender") != null) ? messageData.get("sender").toString() : "unknown";

                    // Check if the "text" field is null before calling toString()
                    String text = (messageData.get("text") != null) ? messageData.get("text").toString() : "";

                    // Check if the "timestamp" field is null before parsing it as a long
                    long timestamp = (messageData.get("timestamp") != null) ? Long.parseLong(messageData.get("timestamp").toString()) : 0;

                    // Create a new ChatMessage object with the message data
                    ChatMessage chatMessage = new ChatMessage(sender, text, timestamp);

                    // Add the ChatMessage to the chatAdapter
                    chatAdapter.addMessage(chatMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while retrieving the messages
                Log.e("Firebase", "Error retrieving messages", error.toException());
            }
        });
    }


    private void connectWebSocket() {
        // Create a new URI object for the WebSocket server
        URI uri = URI.create("ws://localhost:8080");

        // Create a new WebSocketClient using the URI object and override the onOpen(), onMessage(), onClose(), and onError() methods
        chatWebSocket = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                // Handle WebSocket open event
                Log.d("WebSocket", "WebSocket connection opened");
            }

            @Override
            public void onMessage(String message) {
                // Handle incoming text message
                // Use runOnUiThread() to update the chat user interface from a background thread
                runOnUiThread(() -> {
                    // Create a new ChatMessage object with the message data
                    ChatMessage chatMessage = new ChatMessage("Server", message, new Date().getTime());

                    // Add the ChatMessage to the chatAdapter
                    chatAdapter.addMessage(chatMessage);
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                // Handle WebSocket closed event
                Log.d("WebSocket", "WebSocket connection closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                // Handle WebSocket error event
                Log.e("WebSocket", "WebSocket error: " + ex.getMessage());
            }
        };

        // Establish a WebSocket connection by calling the connect() method
        chatWebSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Check if chatWebSocket is not null before calling chatWebSocket.close()
        if (chatWebSocket != null) {
            // Close the WebSocket connection by calling the close() method
            chatWebSocket.close();
        }
    }

    public void openActivityHome() {
        // Shut down the chatServer before opening the HomeActivity
        try {
            chatServer.shutdown();
        } catch (InterruptedException e) {
            // Throw a RuntimeException if the chatServer.shutdown() method is interrupted
            throw new RuntimeException(e);
        }

        // Create a new Intent object for the HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);

        // Start the HomeActivity by calling the startActivity() method with the Intent object as a parameter
        startActivity(intent);
    }
}