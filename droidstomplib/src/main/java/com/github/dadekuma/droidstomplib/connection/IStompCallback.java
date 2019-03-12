package com.github.dadekuma.droidstomplib.connection;

public interface IStompCallback {
    void onConnectionOpened();
    void onConnectionClosed(int code, String reason);
    void onResponseReceived(String response);
    void onMessageReceived(String topic, Long subscriptionId, String message);
    boolean isConnected();
    boolean isConnecting();
}
