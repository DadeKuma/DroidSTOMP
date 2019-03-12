package com.github.dadekuma.droidstomplib.connection;

import android.util.Log;

import com.github.dadekuma.droidstomplib.enums.StompCommandName;
import com.github.dadekuma.droidstomplib.enums.StompHeaderName;
import com.github.dadekuma.droidstomplib.payload.HttpHeader;
import com.github.dadekuma.droidstomplib.payload.StompFrame;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StompClient implements IStompClient {
    private Map<String, Long> subscriptions;
    private long subscriptionId;
    private IConnectionProvider connectionProvider;

    public StompClient() {
        this(new OkHttpConnectionProvider());
    }

    public StompClient(IConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        subscriptions = new HashMap<>();
    }

    @Override
    public void connect(String stompEndpoint) {
        connect(stompEndpoint, new LinkedList<HttpHeader>());
    }

    @Override
    public void connect(String stompEndpoint, Collection<HttpHeader> headers) {
        connectionProvider.connect(stompEndpoint, headers);
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.CONNECT)
                .addStompHeader(StompHeaderName.ACCEPT_VERSION, "1.0,1.1,2.0")
                .addStompHeader(StompHeaderName.HOST, "stomp.github.org");
        connectionProvider.send(stompFrame.build());
    }

    @Override
    public void send(String stompDestination, String message) {
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SEND)
                .addStompHeader(StompHeaderName.DESTINATION, stompDestination)
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "text/plain")
                .addBody(message);
        connectionProvider.send(stompFrame.build());
    }

    @Override
    public void sendJson(String stompDestination, String jsonMessage) {
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SEND)
                .addStompHeader(StompHeaderName.DESTINATION, stompDestination)
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "application/json;charset=utf-8")
                .addBody(jsonMessage);
        connectionProvider.send(stompFrame.build());
    }

    @Override
    public void subscribe(String topic) {
        subscriptionId += 1;
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.SUBSCRIBE)
                .addStompHeader(StompHeaderName.ID, String.valueOf(subscriptionId))
                .addStompHeader(StompHeaderName.DESTINATION, topic)
                .addStompHeader(StompHeaderName.ACK, "auto");
        connectionProvider.send(stompFrame.build());
        subscriptions.put(topic, subscriptionId);
    }

    @Override
    public void unsubscribe(String topic) {
        if (!subscriptions.containsKey(topic)) {
            Log.e("SUB_NOT_FOUND", "Subscription doesn't exist");
            return;
        }
        Long unsubscribeId = subscriptions.get(topic);
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.UNSUBSCRIBE)
                .addStompHeader(StompHeaderName.ID, String.valueOf(unsubscribeId));
        connectionProvider.send(stompFrame.build());
    }

    @Override
    public void disconnect() {
        unsubscribeAll();
        StompFrame stompFrame = new StompFrame();
        stompFrame.addStompCommand(StompCommandName.DISCONNECT)
                .addStompHeader(StompHeaderName.RECEIPT, String.valueOf(subscriptionId));
        connectionProvider.send(stompFrame.build());
    }

    public void unsubscribeAll() {
        for (String topic : subscriptions.keySet()) {
            unsubscribe(topic);
        }
    }

    @Override
    public void setStompCallback(IStompCallback stompCallback){
        connectionProvider.setStompCallback(stompCallback);
    }

    @Override
    public boolean isConnected() {
        return connectionProvider.isConnected();
    }

    @Override
    public boolean isConnecting() {
        return connectionProvider.isConnecting();
    }
}
