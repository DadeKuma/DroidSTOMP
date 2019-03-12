package com.github.dadekuma.droidstomplib.connection;

import com.github.dadekuma.droidstomplib.enums.StompHeaderName;
import com.github.dadekuma.droidstomplib.payload.StompFrame;
import com.github.dadekuma.droidstomplib.payload.StompParser;

import java.util.Map;

public abstract class StompCallback implements IStompCallback{
    private boolean isConnected, isConnecting;

    @Override
    public void onConnectionOpened() {
        isConnecting = true;
    }

    @Override
    public void onConnectionClosed(int code, String reason) {
        isConnected = isConnecting = false;
    }

    @Override
    public void onResponseReceived(String response) {
        StompFrame stompFrame = StompParser.parse(response);
        switch (stompFrame.getStompCommand()){
            case CONNECTED:
                isConnected = true;
                break;
            case MESSAGE:
                handleSubscriptionMessage(stompFrame);
                break;
        }
    }

    private void handleSubscriptionMessage(StompFrame stompFrame){
        Map<StompHeaderName, String> headers = stompFrame.getStompHeaders();
        String topic = headers.get(StompHeaderName.DESTINATION);
        long subscriptionId = Long.parseLong(headers.get(StompHeaderName.SUBSCRIPTION));
        String body = stompFrame.getStompBody();
        onMessageReceived(topic, subscriptionId, body);
    }

    @Override
    public abstract void onMessageReceived(String topic, Long subscriptionId, String message);

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean isConnecting() {
        return isConnecting;
    }
}
