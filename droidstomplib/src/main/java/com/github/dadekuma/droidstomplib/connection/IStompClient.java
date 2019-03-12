package com.github.dadekuma.droidstomplib.connection;

import com.github.dadekuma.droidstomplib.payload.HttpHeader;

import java.util.Collection;

public interface IStompClient {
    void setStompCallback(IStompCallback stompCallback);
    void connect(String stompEndpoint);
    void connect(String stompEndpoint, Collection<HttpHeader> headers);
    void send(String stompDestination, String message);
    void sendJson(String stompDestination, String jsonMessage);
    void subscribe(String topic);
    void unsubscribe(String topic);
    void disconnect();
    boolean isConnected();
    boolean isConnecting();
}
