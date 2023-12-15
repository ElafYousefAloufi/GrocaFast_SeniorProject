package com.grocafast.grocafast.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grocafast.grocafast.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatAdapter(Context context) {
        chatMessages = new ArrayList<>();
        this.context = context;
    }
    public ChatAdapter() {
        chatMessages = new ArrayList<>();

    }

    public void addMessage(ChatMessage chatMessage) {
        // Add the ChatMessage to the chatMessages list
        chatMessages.add(chatMessage);

        // Notify the RecyclerView that a new item has been inserted at the end of the list
        notifyItemInserted(chatMessages.size() - 1);
    }

    public void clearMessages() {
        // Clear all ChatMessage objects from the chatMessages list
        chatMessages.clear();

        // Notify the RecyclerView that all items have been removed from the list
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the chat_message_item layout and create a new View object
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        // Return a new ChatViewHolder object with the new View as a parameter
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        // Get the ChatMessage object at the specified position from the chatMessages list
        ChatMessage chatMessage = chatMessages.get(position);

        // Call the bind() method of the ChatViewHolder with the ChatMessage as a parameter
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        // Return the number of ChatMessage objects in the chatMessages list
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;
        private TextView senderTextView;
        private TextView timestampTextView;

        private TextView date;

        public ChatViewHolder(View itemView) {
            // Call the superclass constructor with the itemView parameter
            super(itemView);

            // Find and initialize the messageTextView, senderTextView, and timestampTextView Views from the chat_message_item layout
            messageTextView = itemView.findViewById(R.id.text_chat_message);
            senderTextView = itemView.findViewById(R.id.text_chat_user);
            timestampTextView = itemView.findViewById(R.id.text_chat_timestamp_other);
            date=itemView.findViewById(R.id.text_chat_date);
        }

        public void bind(ChatMessage chatMessage) {
            // Set the text of the messageTextView to the text of the ChatMessage
            messageTextView.setText(chatMessage.getText());

            // Set the text of the senderTextView to the sender of the ChatMessage
            senderTextView.setText(chatMessage.getSender());

            // Set the text of the timestampTextView to the formatted timestamp of the ChatMessage
            timestampTextView.setText(getFormattedTimestamp(chatMessage.getTimestamp()));


            date.setText(Date());
        }

        private String getFormattedTimestamp(long timestamp) {
            // Create a new SimpleDateFormat object with the specified format and default locale
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Format the timestamp as a Date object and return the formatted string
            return formatter.format(new Date(timestamp));
        }

        private String Date(){
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1; //Month in Calendar starts from 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String currentDate =  day+ "-" + month ;
            return currentDate;

        }
    }
}