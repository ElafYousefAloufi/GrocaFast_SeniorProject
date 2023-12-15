package com.grocafast.grocafast.Community;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.grocafast.grocafast.LoginActivity;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {
    private final Set<WebSocket> connections = new HashSet<>();
    private ChatAdapter chatAdapter;
    private DatabaseReference messagesRef;
    private Activity activity;
    private ChatMessage chatMessage;
    String username;

    private volatile boolean isShuttingDown = false;

    public ChatServer(int port, ChatAdapter adapter, Activity activity,DatabaseReference messagesRef) {
        super(new InetSocketAddress(port));
        chatAdapter = adapter;
        this.messagesRef = messagesRef;
        this.activity=activity;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // Check if the server is currently shutting down.
        if (isShuttingDown) {
            // If the server is shutting down, close the connection and do nothing.
            conn.close();
        } else {
            // If the server is not shutting down, add the connection to the connections set.
            connections.add(conn);
            Log.d("Websocket","New client connected: " + conn.getRemoteSocketAddress());
            //System.out.println("New client connected: " + conn.getRemoteSocketAddress());
        }
    }


    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // Check if the server is shutting down
        if (isShuttingDown) {
            // If the server is shutting down, remove the WebSocket connection from the connections list
            connections.remove(conn);
        } else {
            // If the server is not shutting down, remove the WebSocket connection from the connections list and log a message with the Log.d() method
            connections.remove(conn);
            Log.d("Websocket", "Client disconnected: " + conn.getRemoteSocketAddress());
        }
    }
    public void shutdown() throws InterruptedException {
        // Set the isShuttingDown flag to true to indicate that the server is shutting down.
        isShuttingDown = true;

        // Close all connections to the server.
        for (WebSocket conn : connections) {
            conn.close();
        }

        // Stop the server.
        stop();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // Check if the server is currently shutting down.
        if (isShuttingDown) {
            // If the server is shutting down, do nothing.
            return;
        } else {
            // If the server is not shutting down, process the message.
            // Send the message to all other connected clients.
            for (WebSocket connection : connections) {
                if (connection !=conn) {
                    connection.send(message);
                }
            }
            username= LoginActivity.userName;

            // Create a new ChatMessage object with the message, sender, and timestamp.
            chatMessage = new ChatMessage( username+":", message, new Date().getTime());

            // Store the message in the Firebase Realtime Database.
            messagesRef.push().setValue(chatMessage);

            // Add the message to the chat adapter to display it in the UI.
            activity.runOnUiThread(() -> {
                chatAdapter.addMessage(chatMessage);
            });
        }
    }



    @Override
    public void onError(WebSocket conn, Exception ex) {
        // Check if the server is currently shutting down.
        if (isShuttingDown) {
            // If the server is shutting down, do nothing.
            return;
        } else {
            // If the server is not shutting down, print the error message.
            Log.e("Websocket","Error occurred: " + ex.getMessage());
        }
    }


    @Override
    public void onStart() {
        // Print a message to the console when the WebSocket server starts
        Log.d("Websocket","WebSocket server started");
    }

}

