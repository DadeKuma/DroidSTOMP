package com.github.dadekuma.droidstomp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.dadekuma.droidstomplib.connection.StompCallback;
import com.github.dadekuma.droidstomplib.connection.StompClient;

public class MainActivity extends AppCompatActivity {
    private StompClient stompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup chatLayout = findViewById(R.id.message_container);
        final EditText inputText = findViewById(R.id.message_input);
        final Button sendButton = findViewById(R.id.message_send);

        final String serverEndpoint = "ws://10.0.2.2:8080/server_endpoint";
        final String subscriptionTopic = "/topic/test";

        stompClient = new StompClient();
        stompClient.setStompCallback(new StompCallback() {
            @Override
            public void onMessageReceived(String topic, Long subscriptionId, String message) {
                //if we receive a message on subscriptionTopic we add
                //a text child view to chatLayout
                if(topic.equals(subscriptionTopic)){
                    addChatBox(chatLayout, message);
                }
            }
        });

        stompClient.connect(serverEndpoint);
        stompClient.subscribe(subscriptionTopic);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputText.getText().toString();
                stompClient.send("/app/chat", text);
                inputText.setText("");
            }
        });
    }

    //this function is used to add a TextView with the received message to our chatLayout
    private void addChatBox(final ViewGroup chatLayout, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView text = new TextView(getApplicationContext());
                text.setText(message);
                chatLayout.addView(text);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        stompClient.disconnect(); //remember to close the stomp client when you are done!
    }
}